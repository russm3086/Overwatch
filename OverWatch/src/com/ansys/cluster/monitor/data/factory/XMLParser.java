/**
 * 
 */
package com.ansys.cluster.monitor.data.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;

import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.data.Job;
import com.ansys.cluster.monitor.data.JobMessage;
import com.ansys.cluster.monitor.data.NodeProp;
import com.ansys.cluster.monitor.data.Quota;
import com.ansys.cluster.monitor.data.interfaces.ParserAbstract;
import com.ansys.cluster.monitor.main.SGE_DataConst;
import com.ansys.cluster.monitor.net.Payload;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * @author rmartine
 *
 */
public class XMLParser extends ParserAbstract {
	private String sourceClass = this.getClass().getName();
	private Logger logger = Logger.getLogger(sourceClass);

	public XMLParser(Payload payload, SGE_MonitorProp mainProps) {
		super(payload, mainProps);
	}

	public XMLParser(Payload payloadJobs, Payload payLoadDetailedJobs, SGE_MonitorProp mainProps) {
		super(payloadJobs, payLoadDetailedJobs, mainProps);
	}

	public LinkedList<Quota> createQuotaMap(String userName) {
		logger.entering(sourceClass, "createQuotaMap");

		HashMap<String, LinkedList<Quota>> quotaMap = createQuotaMap();
		LinkedList<Quota> list = quotaMap.get(userName);

		if (list == null)
			list = new LinkedList<Quota>();

		logger.exiting(sourceClass, "createQuotaMap");
		return list;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, LinkedList<Quota>> createQuotaMap() {
		logger.entering(sourceClass, "createQuotaMap");
		HashMap<String, LinkedList<Quota>> map = new HashMap<String, LinkedList<Quota>>();

		logger.fine("Parsing Quota stream");
		Element root = payload.getDocObject().getRootElement();

		logger.finer("Processing element " + root.getName());
		List<Element> elmList = root.getChildren();

		for (Element elem : elmList) {
			logger.finer("Creating Quota object " + elem.getName());

			Quota quota = createQuota(elem);

			if (map.containsKey(quota.getUser())) {

				logger.finest("Adding " + quota.getQuotaName() + " to user " + quota.getUser());
				map.get(quota.getUser()).add(quota);
			} else {

				logger.finest("Creating a new map element " + quota.getQuotaName() + " to user " + quota.getUser());
				LinkedList<Quota> list = new LinkedList<Quota>();
				list.add(quota);
				map.put(quota.getUser(), list);
			}
		}

		logger.exiting(sourceClass, "createQuotaMap");
		return map;
	}

	/**
	 * 
	 * @param elem
	 * @return
	 */
	public Quota createQuota(Element elem) {
		logger.entering(sourceClass, "createQuota");
		Quota quota = new Quota();
		quota.setQuotaName(elem.getAttributeValue("name"));

		List<Element> subElmList = elem.getChildren();
		for (Element subElem : subElmList) {

			switch (subElem.getName()) {

			case "limit":

				quota.setResource(subElem.getAttribute("resource").getValue());
				quota.setLimit(Integer.parseInt(subElem.getAttribute("limit").getValue()));
				quota.setUsage(Integer.parseInt(subElem.getAttribute("value").getValue()));

				break;

			case "users":
				quota.setUser(subElem.getText());
				break;

			case "queues":
				quota.setQueues(subElem.getText());
				break;
			}
		}

		logger.exiting(sourceClass, "createQuota");
		return quota;
	}

	public HashMap<String, Host> createHostsMap() {
		logger.entering(sourceClass, "createHostMap");
		HashMap<String, Host> map = new HashMap<String, Host>();
		Element root = payload.getDocObject().getRootElement();
		List<Element> elmList = root.getChildren();

		for (Element elem : elmList) {
			logger.finer("Processing host xml" + elem.getAttributeValue(SGE_DataConst.attribName));
			NodeProp prop = createHostMap(elem);
			prop.setZoneID(mainProps.getClusterZoneId());
			Host host = new Host(prop);
			logger.finer("Created host " + host.getName());
			map.put(host.getName(), host);
		}

		logger.exiting(sourceClass, "createHostMap");
		return map;
	}

	private NodeProp createHostMap(Element element) {
		logger.entering(sourceClass, "createHostMap", element);
		NodeProp prop = new NodeProp();

		if (element.getName().equalsIgnoreCase(SGE_DataConst.hostname)) {
			prop.putLog(element.getName(), element.getAttributeValue(SGE_DataConst.attribName));
		}

		List<Element> elmList = element.getChildren();
		for (Element subElem : elmList) {
			String key = subElem.getAttributeValue(SGE_DataConst.attribName);
			Object value = subElem.getValue();

			if (subElem.getChildren().size() > 0) {
				processQueue(subElem, prop);
			} else {
				prop.putLog(key, value);
			}
		}
		logger.exiting(sourceClass, "createHostMap", prop);
		return prop;
	}

	private void processQueue(Element elem, NodeProp nodeProp) {
		logger.entering(sourceClass, "processQueue");
		String key = elem.getName();
		String value = elem.getAttributeValue(SGE_DataConst.attribName);
		if (!matchSettings(mainProps.getClusterQueueOmissions(), value)) {
			nodeProp.putLog(key, value);

			for (Element subElem : elem.getChildren()) {
				key = subElem.getAttributeValue(SGE_DataConst.attribName);
				value = subElem.getValue();
				nodeProp.putLog(key, value);
			}
		}
		logger.exiting(sourceClass, "processQueue", nodeProp);
	}

	@Override
	public HashMap<Integer, Job> createJobsMap() {
		// TODO Auto-generated method stub

		logger.fine("Creating summary job objects");
		HashMap<Integer, Job> mapSumJobs = createSummaryJobs(payloadJobs.getDocObject());

		logger.fine("Creating detailed job objects");
		HashMap<Integer, NodeProp> mapDetailedNodeProps = new HashMap<Integer, NodeProp>();

		try {
			mapDetailedNodeProps = createDetailedJobsProp(payLoadDetailedJobs.getDocObject(), mainProps);

		} catch (java.lang.NullPointerException e) {

			logger.warning("Error caught: " + e.getStackTrace());
		}

		logger.fine("Merging detailed and summary job objects");
		HashMap<Integer, Job> mapJobs = mergeSummaryDetailedJobs(mapSumJobs, mapDetailedNodeProps);

		logger.entering(sourceClass, "createJobs");
		return mapJobs;
	}

	public HashMap<Integer, Job> createSummaryJobs(Document document) {
		logger.entering(sourceClass, "createSummaryJobs", document);
		HashMap<Integer, Job> map = new HashMap<Integer, Job>();
		Element jobInfoRoot = document.getRootElement();
		List<Element> listElm = jobInfoRoot.getChildren();
		for (Element elem : listElm) {

			for (Element subElem : elem.getChildren()) {
				logger.finer("Creating job object " + subElem);
				NodeProp prop = createJobProp(subElem);
				prop.setZoneID(mainProps.getClusterZoneId());
				Job job = new Job(prop);
				logger.finer("Created job object " + job.getJobNumber());
				map.put(job.getJobNumber(), job);
			}
		}
		logger.exiting(sourceClass, "createSummaryJobs", map);
		return map;
	}

	private NodeProp createJobProp(Element element) {
		logger.entering(sourceClass, "createDetailedJobProp", element);
		NodeProp prop = new NodeProp();
		Object value = null;
		String key = null;
		logger.finest("Processing element: " + element);

		List<Element> listElm = element.getChildren();

		for (Element elem : listElm) {
			key = elem.getName();
			logger.finest("Retrieving element " + key);

			if (!matchSettings(mainProps.getJobDetailOmissions(), key)) {

				if (elem.getChildren().size() > 0) {

					if (matchSettings(mainProps.getClusterDataJobList(), key)) {
						value = processList(elem);
					} else if (elem.getChild(SGE_DataConst.xml_Element) == null) {
						value = createJobProp(elem);
					} else {
						Element child = elem.getChild(SGE_DataConst.xml_Element);
						value = createJobProp(child);
					}

				} else {
					value = elem.getValue();
				}

				prop.putLog(key, value);
			}
		}

		logger.exiting(sourceClass, "createDetailedJobProps");
		return prop;
	}

	private ArrayList<NodeProp> processList(Element element) {
		logger.entering(sourceClass, "processEnvList", element);

		ArrayList<NodeProp> listProp = new ArrayList<NodeProp>();
		List<Element> listElem = element.getChildren();

		for (Element elem : listElem) {

			NodeProp nodeProp = createJobProp(elem);
			listProp.add(nodeProp);
		}

		logger.exiting(sourceClass, "processEnvList", listProp);
		return listProp;
	}

	public HashMap<Integer, NodeProp> createDetailedJobsProp(Document document, SGE_MonitorProp mainProp) {

		Element root = document.getRootElement();
		Element djobInfoElm = root.getChild(SGE_DataConst.djob_info);

		logger.finer("Create detailed Job Properties Object");
		HashMap<Integer, NodeProp> hashMapProp = createDetailedJobsProp(djobInfoElm, mainProp);

		logger.info("Parsed " + hashMapProp.size() + " detailed jobs");

		logger.finer("Retrieving " + SGE_DataConst.msg);
		Element messageElm = root.getChild(SGE_DataConst.msg);

		logger.finer("Create detailed Job Message Properties Object");
		ArrayList<JobMessage> listJobMsg = createJobMessages(messageElm);

		logger.finer("Addind Job messages to detailed Job");
		addJobMsgsToJobs(listJobMsg, hashMapProp);

		return hashMapProp;
	}

	private HashMap<Integer, NodeProp> createDetailedJobsProp(Element djobInfoElm, SGE_MonitorProp mainProp) {
		logger.entering(sourceClass, "createDetailedJobProps");
		HashMap<Integer, NodeProp> hashMapProp = new HashMap<Integer, NodeProp>();

		logger.finer("Parsing " + djobInfoElm);
		if (djobInfoElm != null) {
			List<Element> listElm = djobInfoElm.getChildren();

			for (Element elem : listElm) {

				NodeProp prop = createJobProp(elem);

				logger.finer("Created detail job prop " + prop.getJobName());

				prop.setJobIdleThreshold(mainProp.getJobIdleThreshold());

				hashMapProp.put(prop.getJobNumber(), prop);
			}
		}
		logger.exiting(sourceClass, "createDetailedJobsProp");
		return hashMapProp;
	}

	private ArrayList<JobMessage> createJobMessages(Element messagesElm) {
		logger.entering(sourceClass, "createJobMessages", messagesElm);
		ArrayList<JobMessage> listJobMsg = new ArrayList<JobMessage>();
		Element element = messagesElm.getChild(SGE_DataConst.xml_Element);
		Element subElement = element.getChild("SME_message_list");

		if (subElement != null) {
			List<Element> listElem = subElement.getChildren();

			for (Element elem : listElem) {

				JobMessage jobMsg = createJobMessage(elem);

				if (jobMessageFilter(jobMsg)) {
					jobMessageQuotaTranslator(jobMsg);
					listJobMsg.add(jobMsg);
				}
			}
		}
		logger.exiting(sourceClass, "createJobMessages", listJobMsg);
		return listJobMsg;
	}

	private void jobMessageQuotaTranslator(JobMessage jobMsg) {
		// TODO Create Props entry
		Pattern pattern = Pattern
				.compile("cannot run because it exceeds limit \\\"(.*)///.*///.*\\\" in rule \\\"(.*)_.*_.*\\\"");
		Matcher matcher = pattern.matcher(jobMsg.getMessage());

		while (matcher.find()) {

			jobMsg.setMessage("User: " + matcher.group(1) + " has exceeded quota: " + matcher.group(2));
		}
	}

	private boolean jobMessageFilter(JobMessage jobMsg) {
		// TODO Create Props entry
		boolean result = true;
		List<String> lstRegex = new ArrayList<String>();
		lstRegex.add("cannot run in queue .* because it is not contained in its hard queue list \\(-q\\)");
		lstRegex.add(
				"\\(-l exclusive=true\\) cannot run in queue .* because exclusive resource \\(exclusive\\) is already in use");
		lstRegex.add("\\(-l exclusive=true\\) cannot run in queue .* because it offers only qc:exclusive=false");

		for (String regex : lstRegex) {

			if (jobMsg.getMessage().matches(regex)) {

				result = false;
				break;
			}
		}

		return result;
	}

	private JobMessage createJobMessage(Element messageElm) {
		logger.entering(sourceClass, "createJobMessage", messageElm);

		// Element element = messageElm.getChild(SGE_DataConst.xml_Element);
		Element element = messageElm;
		NodeProp prop = new NodeProp();
		JobMessage jobMsg = null;

		String value = element.getChild(SGE_DataConst.msg_msg_number).getValue();
		prop.putLog(SGE_DataConst.msg_msg_number, value);

		value = element.getChild(SGE_DataConst.msg_msg).getValue();
		prop.putLog(SGE_DataConst.msg_msg, value);

		Element jobList = element.getChild(SGE_DataConst.msg_job_list);
		Element jlSub = jobList.getChild(SGE_DataConst.xml_Element);

		List<Element> jobIdList = jlSub.getChildren();
		LinkedHashSet<Integer> list = new LinkedHashSet<Integer>();

		for (Element jobId : jobIdList) {
			list.add(Integer.parseInt(jobId.getValue()));
		}

		prop.putLog(SGE_DataConst.msg_job_list, list);

		jobMsg = new JobMessage(prop);

		logger.exiting(sourceClass, "createJobMessage", jobMsg);
		return jobMsg;
	}

}
