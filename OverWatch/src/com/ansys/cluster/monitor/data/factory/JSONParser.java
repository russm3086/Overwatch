/**
 * 
 */
package com.ansys.cluster.monitor.data.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.data.Job;
import com.ansys.cluster.monitor.data.JobMessage;
import com.ansys.cluster.monitor.data.NodeProp;
import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.ansys.cluster.monitor.data.interfaces.ParserAbstract;
import com.ansys.cluster.monitor.net.Payload;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * @author rmartine
 *
 */
public class JSONParser extends ParserAbstract {
	private String sourceClass = this.getClass().getName();
	private Logger logger = Logger.getLogger(sourceClass);

	/**
	 * @param payloadHosts
	 * @param mainProps
	 */
	public JSONParser(Payload payloadHosts, SGE_MonitorProp mainProps) {
		super(payloadHosts, mainProps);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param payloadJobs
	 * @param payLoadDetailedJobs
	 * @param mainProps
	 */
	public JSONParser(Payload payloadJobs, Payload payLoadDetailedJobs, SGE_MonitorProp mainProps) {
		super(payloadJobs, payLoadDetailedJobs, mainProps);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, Host> createHostsMap() {
		logger.entering(sourceClass, "createHostsMap");
		HashMap<String, Host> map = new HashMap<String, Host>();

		JSONObject jsonObject = payloadHosts.getJsoObject();
		JSONObject jsonQhost = (JSONObject) jsonObject.get(SGE_DataConst.json_qhost);
		JSONArray jsonHosts = (JSONArray) jsonQhost.get(SGE_DataConst.json_host_name);

		for (int i = 0; i < jsonHosts.length(); i++) {

			JSONObject jsoSubHosts = jsonHosts.getJSONObject(i);
			Host host = createNode(jsoSubHosts);

			if (!host.getNodeProp().getHostname().equalsIgnoreCase("global")) {
				map.put(host.getNodeProp().getHostname(), host);
				logger.finest("Adding host " + host + " to queue");
			}
		}

		logger.exiting(sourceClass, "createQueue", map);
		return map;
	}

	private Host createNode(JSONObject jsonObject) {
		logger.entering(sourceClass, "createHost", jsonObject);

		NodeProp nodeProp = new NodeProp();
		jsoParse(jsonObject, nodeProp);
		parseQueue(jsonObject, nodeProp);
		Host host = new Host(nodeProp);

		logger.finer("Host " + host.getName() + " created.");
		logger.exiting(sourceClass, "createHost", host);
		return host;
	}

	private void jsoParse(JSONObject jsObject, NodeProp nodeProp) {
		logger.entering(sourceClass, "jsoParse");

		logger.finer("Processing json object " + jsObject.getString(SGE_DataConst.json_key_name));
		nodeProp.setLogProperty(SGE_DataConst.hostname, jsObject.getString(SGE_DataConst.json_key_name));
		JSONArray jsaHostValue = (JSONArray) jsObject.getJSONArray(SGE_DataConst.json_host_value);
		jsaParse(jsaHostValue, nodeProp);

		logger.exiting(sourceClass, "jsoParse");

	}

	private void jsaParse(JSONArray jsonArray, NodeProp nodeProp) {

		logger.entering(sourceClass, "jsaParse");
		for (int i = 0; i < jsonArray.length(); i++) {

			String key = jsonArray.getJSONObject(i).getString("@name");
			String value = (String) jsonArray.getJSONObject(i).optString("$");
			nodeProp.setLogProperty(key, value);

		}

		logger.exiting(sourceClass, "jsaParse");
	}

	private void parseQueue(JSONObject jsObject, NodeProp nodeProp) {

		parseElement(jsObject, "queue", "queuevalue", nodeProp);

	}

	private void parseElement(JSONObject jsObject, String element, String subElement, NodeProp nodeProp) {
		logger.entering(element, "parseElement");

		if (!jsObject.isNull(element)) {

			logger.finest("Processing element: " + element);
			Object object = jsObject.get(element);

			if (object instanceof JSONObject) {
				JSONObject jsSubObject = jsObject.getJSONObject(element);
				nodeProp.put(element, jsSubObject.get("@name"));

				JSONArray jsaSubArray = jsSubObject.getJSONArray(subElement);
				jsaParse(jsaSubArray, nodeProp);

			} else if (object instanceof JSONArray) {

				JSONArray jsaSubArray = jsObject.getJSONArray(element);

				for (int x = 0; x < jsaSubArray.length(); x++) {
					JSONObject jsSubObject = jsaSubArray.getJSONObject(x);
					parseElement(jsSubObject, element, subElement, nodeProp);

				}
			}
		}

		logger.exiting(element, "parseElement");
	}

	@Override
	public HashMap<Integer, Job> createJobsMap() {
		// TODO Auto-generated method stub

		logger.fine("Creating summary job objects");
		HashMap<Integer, Job> mapSumJobs = createSummaryJobs(payloadJobs.getJsoObject());

		logger.fine("Creating detailed job objects");
		HashMap<Integer, NodeProp> mapDetailedNodeProps = createDetailedJobsProp(payLoadDetailedJobs.getJsoObject(),
				mainProps);

		logger.fine("Merging detailed and summary job objects");
		HashMap<Integer, Job> mapJobs = mergeSummaryDetailedJobs(mapSumJobs, mapDetailedNodeProps);

		logger.entering(sourceClass, "createJobs");
		return mapJobs;
	}

	private HashMap<Integer, Job> createSummaryJobs(JSONObject jsoObject) {
		logger.entering(sourceClass, "createSummaryJobs");
		HashMap<Integer, Job> hashMap = new HashMap<Integer, Job>();

		logger.finer("Retrieving " + SGE_DataConst.json_job_info);
		JSONObject jsoJobInfo = jsoObject.getJSONObject(SGE_DataConst.json_job_info);

		logger.finer("Retrieving " + SGE_DataConst.json_queue_info);
		JSONObject jsoQueueList = jsoJobInfo.getJSONObject(SGE_DataConst.json_queue_info);

		logger.finer("Retrieving " + SGE_DataConst.json_job_list);
		JSONArray jsaJobList = jsoQueueList.getJSONArray(SGE_DataConst.json_job_list);

		processJobList(jsaJobList, hashMap);

		logger.finer("Retrieving " + SGE_DataConst.json_queue_info);
		jsoQueueList = jsoJobInfo.getJSONObject(SGE_DataConst.json_job_info);

		logger.finer("Retrieving " + SGE_DataConst.json_job_list);
		jsaJobList = jsoQueueList.getJSONArray(SGE_DataConst.json_job_list);

		processJobList(jsaJobList, hashMap);

		logger.exiting(sourceClass, "createSummaryJobs", hashMap);
		return hashMap;
	}

	public void processJobList(JSONArray jsaJobList, HashMap<Integer, Job> hashMap) {
		logger.entering(sourceClass, "processJobList");

		Iterator<Object> itJsaJobList = jsaJobList.iterator();

		while (itJsaJobList.hasNext()) {
			JSONObject jsoJob = (JSONObject) itJsaJobList.next();

			Job job = createJob(jsoJob);

			logger.finest("Storing job #: " + job.getJobNumber() + " " + job);
			hashMap.put(job.getJobNumber(), job);
		}

		logger.exiting(sourceClass, "processJobList");

	}

	public Job createJob(JSONObject jsoJob) {
		logger.entering(sourceClass, "createJob", jsoJob);
		NodeProp nodeProp = new NodeProp();

		for (String jobKey : jsoJob.keySet()) {

			String value = null;
			Object objElement = jsoJob.get(jobKey);

			String instance = objElement.getClass().getSimpleName();

			switch (instance) {

			case SGE_DataConst.json_object:

				JSONObject jobElement = (JSONObject) objElement;
				value = jobElement.optString(SGE_DataConst.json_string_key);

				break;

			case SGE_DataConst.json_string:

				value = (String) objElement;

				break;
			}

			nodeProp.setLogProperty(jobKey, value);
		}

		Job job = new Job(nodeProp);
		logger.exiting(sourceClass, "createJobs", job);

		return job;
	}

	private HashMap<Integer, NodeProp> createDetailedJobsProp(JSONObject jsoDetailedJobs, SGE_MonitorProp mainProp) {

		logger.finer("Retrieving " + SGE_DataConst.json_job_detailed_info);
		JSONObject detailedJobInfo = jsoDetailedJobs.getJSONObject(SGE_DataConst.json_job_detailed_info);

		logger.finer("Retrieving " + SGE_DataConst.json_djob_info);
		JSONObject job_info = detailedJobInfo.getJSONObject(SGE_DataConst.json_djob_info);

		logger.finer("Create detailed Job Properties Object");
		HashMap<Integer, NodeProp> hashMap = createDetailedJobProps(job_info, mainProp);

		logger.finer("Retrieving " + SGE_DataConst.json_message);
		JSONObject jsoMsgs = detailedJobInfo.getJSONObject(SGE_DataConst.json_message);

		logger.finer("Create detailed Job Message Properties Object");
		ArrayList<JobMessage> listJobMsg = createJobMessages(jsoMsgs);

		logger.finer("Addind Job messages to detailed Job");
		addJobMsgsToJobs(listJobMsg, hashMap);

		return hashMap;
	}

	private HashMap<Integer, NodeProp> createDetailedJobProps(JSONObject job_info, SGE_MonitorProp mainProp) {
		logger.entering(sourceClass, "createDetailedJobProps", job_info);
		HashMap<Integer, NodeProp> hashMapProp = new HashMap<Integer, NodeProp>();

		logger.finer("Retrieving detail job element");
		JSONArray jsonArrayElement = job_info.getJSONArray(SGE_DataConst.json_element);

		Iterator<Object> itJsaElements = jsonArrayElement.iterator();

		logger.finer("Iterating through job elements");
		while (itJsaElements.hasNext()) {

			JSONObject jsoElement = (JSONObject) itJsaElements.next();

			NodeProp prop = createDetailedJobProp(jsoElement);

			prop.setJobIdleThreshold(mainProp.getJobIdleThreshold());

			hashMapProp.put(prop.getJobNumber(), prop);
		}

		logger.exiting(sourceClass, "createProps", hashMapProp);
		return hashMapProp;
	}

	private NodeProp createDetailedJobProp(JSONObject jsoElement) {
		logger.entering(sourceClass, "createDetailedJobProp", jsoElement);
		NodeProp prop = new NodeProp();

		logger.finest("Processing element: " + jsoElement);
		Iterator<?> keys = jsoElement.keys();

		while (keys.hasNext()) {
			String key = (String) keys.next();

			logger.finest("Retrieving element " + key);

			JSONObject value = (JSONObject) jsoElement.get(key);

			String subElement = findSubElement(value);

			if (subElement != null) { // task_id_range, Events, grl
				Object sub = null;
				Object objElement = value.get(subElement);
				String instance = objElement.getClass().getSimpleName();

				switch (instance) {

				case SGE_DataConst.json_object:
					logger.finest("Processing " + SGE_DataConst.json_object);
					JSONObject jsoSub = (JSONObject) objElement;
					sub = createDetailedJobProp(jsoSub);

					break;

				case SGE_DataConst.json_array:
					logger.finest("Processing " + SGE_DataConst.json_object);
					JSONArray jsaSub = (JSONArray) objElement;
					sub = createDetailedJobProp(jsaSub);
					break;
				}

				prop.put(key, sub);
			} else {

				prop.put(key, value.optString(SGE_DataConst.json_string_key));

			}

			logger.finest("Saving property Key: " + key + " Value: " + value);
		}

		logger.exiting(sourceClass, "createDetailedJobProps", prop);
		return prop;
	}

	private static String findSubElement(JSONObject value) {
		String result = null;

		for (String elem : SGE_DataConst.json_sub_array) {
			if (value.has(elem)) {

				result = elem;
			}
		}
		return result;
	}

	private ArrayList<NodeProp> createDetailedJobProp(JSONArray jsaElements) {
		logger.entering(sourceClass, "createDetailedJobProp", jsaElements);
		ArrayList<NodeProp> list = new ArrayList<NodeProp>();

		logger.finest("Processing element: " + jsaElements);
		Iterator<Object> itJsaElmList = jsaElements.iterator();

		while (itJsaElmList.hasNext()) {
			JSONObject jsaElement = (JSONObject) itJsaElmList.next();
			Iterator<?> keys = jsaElement.keys();
			NodeProp subProp = new NodeProp();

			while (keys.hasNext()) {
				String key = (String) keys.next();
				JSONObject value = jsaElement.getJSONObject(key);

				logger.finest("Saving property Key: " + key + " Value: " + value);
				subProp.put(key, value.optString(SGE_DataConst.json_string_key));
			}
			list.add(subProp);
		}

		logger.exiting(sourceClass, "createDetailedJobProp", list);
		return list;
	}

	private ArrayList<JobMessage> createJobMessages(JSONObject jsoMsgs) {
		logger.entering(sourceClass, "createJobMessages", jsoMsgs);
		ArrayList<JobMessage> listJobMsg = new ArrayList<JobMessage>();

		JSONObject jsoElement = jsoMsgs.getJSONObject(SGE_DataConst.json_element);
		JSONObject jsoMsgList = jsoElement.getJSONObject(SGE_DataConst.json_message_list);
		JSONArray jsaElem = jsoMsgList.getJSONArray(SGE_DataConst.json_element);

		Iterator<Object> itJsaElem = jsaElem.iterator();

		while (itJsaElem.hasNext()) {

			JSONObject jsoMsg = (JSONObject) itJsaElem.next();
			JobMessage jobMsg = createJobMessage(jsoMsg);
			listJobMsg.add(jobMsg);

		}

		logger.exiting(sourceClass, "createJobMessages", listJobMsg);
		return listJobMsg;
	}

	private  JobMessage createJobMessage(JSONObject jsoMsg) {
		logger.entering(sourceClass, "JobMessage");
		NodeProp prop = createMsgProp(jsoMsg);

		JobMessage jobMsg = new JobMessage(prop);

		logger.exiting(sourceClass, "JobMessage", jobMsg);
		return jobMsg;

	}

	private NodeProp createMsgProp(JSONObject jsoMsg) {
		NodeProp prop = new NodeProp();
		Iterator<?> keys = jsoMsg.keys();
		Object value = null;
		while (keys.hasNext()) {
			String key = (String) keys.next();
			JSONObject jsoValue = (JSONObject) jsoMsg.get(key);

			if (jsoValue.has(SGE_DataConst.json_element)) {
				Object objElement = jsoValue.get(SGE_DataConst.json_element);
				String instance = objElement.getClass().getSimpleName();

				switch (instance) {

				case SGE_DataConst.json_array:
					JSONArray jsaMsgList = (JSONArray) objElement;
					Iterator<Object> itMsgList = jsaMsgList.iterator();
					LinkedHashSet<Integer> list = new LinkedHashSet<Integer>();
					while (itMsgList.hasNext()) {
						JSONObject jsoNumList = (JSONObject) itMsgList.next();
						Iterator<?> jsoNumListKeys = jsoNumList.keys();
						while (jsoNumListKeys.hasNext()) {

							String jsoNumListKey = (String) jsoNumListKeys.next();
							JSONObject jsoNumber = jsoNumList.getJSONObject(jsoNumListKey);
							list.add(Integer.valueOf(jsoNumber.getInt(SGE_DataConst.json_string_key)));
						}
					}

					value = list;

					break;

				case SGE_DataConst.json_object:
					JSONObject jsoMsgItem = (JSONObject) objElement;
					Map<String, Object> map = jsoMsgItem.toMap();
					@SuppressWarnings("unchecked")
					Map<String, Object> subMap = (Map<String, Object>) map.get("ULNG_value");
					Integer jobId = (Integer) subMap.get(SGE_DataConst.json_string_key);
					LinkedHashSet<Integer> listObj = new LinkedHashSet<Integer>();
					listObj.add(jobId);

					value = listObj;
					// prop.put(key, jobId);

				}
			} else {

				value = jsoValue.optString(SGE_DataConst.json_string_key);
			}

			logger.finer("Saving property Key: " + key + " Value: " + value);
			prop.put(key, value);
		}

		return prop;
	}

	
}
