/**
 * 
 */
package com.russ.test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.ansys.cluster.monitor.data.AnsQueue;
import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.data.Job;
import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.ansys.cluster.monitor.data.factory.ClusterFactory;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.main.Main;
import com.ansys.cluster.monitor.net.Connector;
import com.ansys.cluster.monitor.net.DataCollector;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.settings.SystemSettings;

import org.jdom2.JDOMException;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author rmartine
 *
 */
public class Test {

	private static String sourceClass = Test.class.getName();
	private static final Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws ConfigurationException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws JDOMException
	 * @throws JSONException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public static void main(String[] args) throws URISyntaxException, JSONException, IOException, 
			ParserConfigurationException, TransformerException, JDOMException {
		// TODO Auto-generated method stub

		SGE_MonitorProp mainProps = new SGE_MonitorProp();

		String propsFilePath = "C:\\Users\\rmartine\\git\\localMonitor\\monitor\\res\\etc\\settings.properties";

		String propComments = SGE_DataConst.app_name + " v. " + SGE_DataConst.app_version;
		String token = "\\.";

		try {

			SystemSettings systemSettings = new SystemSettings();
			mainProps = Main.loadDefaultProps(propsFilePath, SGE_DataConst.app_version, propComments, token,
					systemSettings);

			logger.info("**Loading log manager**");
			systemSettings.loadManager(propsFilePath);

		} catch (IOException e) {

			logger.log(Level.INFO, "Could not load properties file " + propsFilePath, e);

		}

		Connector conn = new Connector();
		DataCollector dc = new DataCollector(mainProps, conn);
		int index = 0;

		Cluster cluster = ClusterFactory.createCluster(dc, mainProps.getClusterName(index), index, mainProps);
		Document clusterDoc = createCLusterXML(cluster);

		writeToFile("C:\\Users\\rmartine\\git\\localMonitor\\monitor\\res\\test.xml", clusterDoc);

	}

	public static Document createCLusterXML(Cluster cluster) throws ParserConfigurationException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		// root element
		Element rootElement = doc.createElement("Clusters");
		doc.appendChild(rootElement);

		// Time Stamp Element
		rootElement.appendChild(timeStamp(doc));

		rootElement.appendChild(createQueuesElm(doc, cluster));

		return doc;
	}

	public static Element timeStamp(Document doc) {

		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		String formattedDateTime = currentDateTime.format(formatter);

		Element dateStampElement = doc.createElement("TimeStamp");
		dateStampElement.setTextContent(formattedDateTime);

		return dateStampElement;
	}

	public static Element createQueuesElm(Document doc, Cluster cluster) {

		AnsQueue masterQueue = cluster.getMasterQueue().get(SGE_DataConst.mqEntryQueues);

		Element queuesElem = doc.createElement("Queues");

		SortedMap<String, ClusterNodeAbstract> queues = masterQueue.getNodes();
		for (Entry<String, ClusterNodeAbstract> queue : queues.entrySet()) {
			logger.finer("Creating Queue branch " + queue.getValue());

			Element queueElem = doc.createElement("Queue");
			queueElem.setAttribute("name", queue.getKey());
			AnsQueue subQueue = (AnsQueue) queue.getValue();

			Element hostsElem = doc.createElement("Hosts");

			SortedMap<String, ClusterNodeAbstract> hosts = subQueue.getHosts();
			for (Entry<String, ClusterNodeAbstract> hostEntry : hosts.entrySet()) {
				Element hostElem = doc.createElement("Host");
				hostElem.setAttribute("name", hostEntry.getKey());

				Host host = (Host) hostEntry.getValue();

				Element hostExclusiveElem = doc.createElement("Exclusive");
				hostExclusiveElem.setTextContent(String.valueOf(host.isExclusive()));
				hostElem.appendChild(hostExclusiveElem);

				Element hostStateCodeElem = doc.createElement("State");
				hostStateCodeElem.setTextContent(host.getStateCode());
				hostElem.appendChild(hostStateCodeElem);

				Element slotsElm = doc.createElement("Slots");
				Element resElem = doc.createElement("Reserved");
				resElem.setTextContent(String.valueOf(host.getSlotRes()));
				slotsElm.appendChild(resElem);

				Element usedElem = doc.createElement("Used");
				usedElem.setTextContent(String.valueOf(host.getSlotUsed()));
				slotsElm.appendChild(usedElem);

				Element totalElem = doc.createElement("Total");
				totalElem.setTextContent(String.valueOf(host.getSlotTotal()));
				slotsElm.appendChild(totalElem);

				Element memoryElem = doc.createElement("Memory");

				Element memTotalElem = doc.createElement("Total");
				memTotalElem.setTextContent(String.valueOf(host.getMemTotal()));
				memoryElem.appendChild(memTotalElem);

				Element memUsedElem = doc.createElement("Used");
				memUsedElem.setTextContent(String.valueOf(host.getMemUsedNum()));
				memoryElem.appendChild(memUsedElem);

				ArrayList<Job> lstJobs = host.getListJob();

				Element jobsElem = doc.createElement("Jobs");

				for (Job job : lstJobs) {

					Element jobElem = doc.createElement("Job");
					jobElem.setAttribute("job_id", job.getIdentifier());

					Element jobNameElem = doc.createElement("Name");
					jobNameElem.setTextContent(job.getName());

					Element jobOwnerElem = doc.createElement("owner");
					jobOwnerElem.setTextContent(job.getOwner());
					jobElem.appendChild(jobOwnerElem);

					Element jobSlotsElem = doc.createElement("slots");
					jobSlotsElem.setTextContent(String.valueOf(job.getSlots()));
					jobElem.appendChild(jobSlotsElem);

					Element jobStateElem = doc.createElement("state");
					jobStateElem.setTextContent(job.getJobState());
					jobElem.appendChild(jobStateElem);

					Element jobExclusiveElem = doc.createElement("Exclusive");
					jobExclusiveElem.setTextContent(String.valueOf(job.isExclusive()));
					jobElem.appendChild(jobExclusiveElem);

					Element jobTargetQueueElem = doc.createElement("Target_Queue");
					jobTargetQueueElem.setTextContent(job.getTargetQueue());
					jobElem.appendChild(jobTargetQueueElem);

					jobsElem.appendChild(jobElem);

				}

				hostElem.appendChild(slotsElm);
				hostElem.appendChild(memoryElem);
				hostElem.appendChild(jobsElem);

				hostsElem.appendChild(hostElem);
			}

			queueElem.appendChild(hostsElem);

			queuesElem.appendChild(queueElem);

		}

		return queuesElem;

	}

	public static Element createQueueElm(Document doc, AnsQueue queue) {

		logger.finer("Creating Queue branch " + queue.getName());

		Element queueElem = doc.createElement("Queue");
		queueElem.setAttribute("name", queue.getName());

		SortedMap<String, ClusterNodeAbstract> hosts = queue.getHosts();

		queueElem.appendChild(createHostsElem(doc, hosts));

		return queueElem;
	}

	public static Element createHostsElem(Document doc, SortedMap<String, ClusterNodeAbstract> hosts) {

		Element hostsElem = doc.createElement("Hosts");
		for (Entry<String, ClusterNodeAbstract> hostEntry : hosts.entrySet()) {

			Host host = (Host) hostEntry.getValue();
			hostsElem.appendChild(createHostElem(doc, host));

		}

		return hostsElem;

	}

	public static Element createHostElem(Document doc, Host host) {
		Element hostElem = doc.createElement("Host");
		hostElem.setAttribute("name", host.getName());

		Element hostExclusiveElem = doc.createElement("Exclusive");
		hostExclusiveElem.setTextContent(String.valueOf(host.isExclusive()));
		hostElem.appendChild(hostExclusiveElem);

		Element hostStateCodeElem = doc.createElement("State");
		hostStateCodeElem.setTextContent(host.getStateCode());
		hostElem.appendChild(hostStateCodeElem);

		Element slotsElm = doc.createElement("Slots");
		Element resElem = doc.createElement("Reserved");
		resElem.setTextContent(String.valueOf(host.getSlotRes()));
		slotsElm.appendChild(resElem);

		Element usedElem = doc.createElement("Used");
		usedElem.setTextContent(String.valueOf(host.getSlotUsed()));
		slotsElm.appendChild(usedElem);

		Element totalElem = doc.createElement("Total");
		totalElem.setTextContent(String.valueOf(host.getSlotTotal()));
		slotsElm.appendChild(totalElem);

		Element memoryElem = doc.createElement("Memory");

		Element memTotalElem = doc.createElement("Total");
		memTotalElem.setTextContent(String.valueOf(host.getMemTotal()));
		memoryElem.appendChild(memTotalElem);

		Element memUsedElem = doc.createElement("Used");
		memUsedElem.setTextContent(String.valueOf(host.getMemUsedNum()));
		memoryElem.appendChild(memUsedElem);

		hostElem.appendChild(createJobsElement(doc, host));

		return hostElem;

	}

	public static Element createJobsElement(Document doc, Host host) {

		ArrayList<Job> lstJobs = host.getListJob();

		Element jobsElem = doc.createElement("Jobs");

		for (Job job : lstJobs) {

			jobsElem.appendChild(createJobElement(doc, job));

		}

		return jobsElem;
	}

	public static Element createJobElement(Document doc, Job job) {

		Element jobElem = doc.createElement("Job");
		jobElem.setAttribute("job_id", job.getIdentifier());

		Element jobNameElem = doc.createElement("Name");
		jobNameElem.setTextContent(job.getName());

		Element jobOwnerElem = doc.createElement("owner");
		jobOwnerElem.setTextContent(job.getOwner());
		jobElem.appendChild(jobOwnerElem);

		Element jobSlotsElem = doc.createElement("slots");
		jobSlotsElem.setTextContent(String.valueOf(job.getSlots()));
		jobElem.appendChild(jobSlotsElem);

		Element jobStateElem = doc.createElement("state");
		jobStateElem.setTextContent(job.getJobState());
		jobElem.appendChild(jobStateElem);

		Element jobExclusiveElem = doc.createElement("Exclusive");
		jobExclusiveElem.setTextContent(String.valueOf(job.isExclusive()));
		jobElem.appendChild(jobExclusiveElem);

		Element jobTargetQueueElem = doc.createElement("Target_Queue");
		jobTargetQueueElem.setTextContent(job.getTargetQueue());
		jobElem.appendChild(jobTargetQueueElem);

		return jobElem;

	}

	public static void writeToFile(String strFile, Document doc) throws TransformerException {

		writeToFile(new File(strFile), doc);

	}

	public static void writeToFile(File file, Document doc) throws TransformerException {

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(file);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		// Beautify the format of the resulted XML
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.transform(source, result);

	}

}
