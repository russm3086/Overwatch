/**
 * 
 */
package com.ansys.cluster.monitor.data.factory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom2.JDOMException;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ansys.cluster.monitor.data.AnsQueue;
import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.data.Job;
import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.net.Connector;
import com.ansys.cluster.monitor.net.DataCollector;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * @author rmartine
 *
 */
public class Exporter {

	private static String sourceClass = Exporter.class.getName();
	private static Logger logger = Logger.getLogger(sourceClass);
	private SGE_MonitorProp mainProps;
	private String strFile;

	/**
	 * @return the strFile
	 */
	public String getStrFile() {
		return strFile;
	}

	/**
	 * @param strFile the strFile to set
	 */
	public void setStrFile(String strFile) {
		this.strFile = strFile;
	}

	/**
	 * 
	 */
	public Exporter(SGE_MonitorProp mainProps, String strFile) {
		// TODO Auto-generated constructor stub
		this.mainProps = mainProps;
		this.strFile = strFile;
	}

	public void Export() throws JSONException, IOException, URISyntaxException, JDOMException, InterruptedException,
			TransformerException, ParserConfigurationException {

		Connector conn = new Connector(mainProps);
		DataCollector dc = new DataCollector(mainProps, conn);
		int index = mainProps.getClusterIndex();

		logger.info("Creating cluster object");
		Cluster cluster = ClusterFactory.createCluster(dc, mainProps.getClusterName(index), index, mainProps);

		logger.info("Creating XML");
		Document clusterDoc = createCLusterXML(cluster);

		logger.info("Exporting to " + getStrFile());
		writeToFile(getStrFile(), clusterDoc);

	}

	public Document createCLusterXML(Cluster cluster) throws ParserConfigurationException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		// root element
		Element rootElement = doc.createElement("Clusters");
		doc.appendChild(rootElement);

		// Time Stamp Element
		rootElement.appendChild(timeStamp(doc));

		// Queues Element
		rootElement.appendChild(createQueuesElm(doc, cluster));

		return doc;
	}

	public Element timeStamp(Document doc) {

		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		String formattedDateTime = currentDateTime.format(formatter);

		Element dateStampElement = doc.createElement("TimeStamp");
		dateStampElement.setTextContent(formattedDateTime);

		return dateStampElement;
	}

	public Element createQueuesElm(Document doc, Cluster cluster) {

		AnsQueue masterQueue = cluster.getMasterQueue().get(SGE_DataConst.mqEntryQueues);

		Element queuesElem = doc.createElement("Queues");

		SortedMap<String, ClusterNodeAbstract> queues = masterQueue.getNodes();

		for (Entry<String, ClusterNodeAbstract> queue : queues.entrySet()) {
			logger.finer("Creating Queue branch " + queue.getValue());

			Element queueElem = doc.createElement("Queue");
			queueElem.setAttribute("name", queue.getKey());
			AnsQueue subQueue = (AnsQueue) queue.getValue();

			// Element hostsElem = doc.createElement("Hosts");

			SortedMap<String, ClusterNodeAbstract> hosts = subQueue.getHosts();

			Element hostsElem = createHostsElem(doc, hosts);

			queueElem.appendChild(hostsElem);

			queuesElem.appendChild(queueElem);

		}

		return queuesElem;

	}

	public Element createQueueElm(Document doc, AnsQueue queue) {

		logger.finer("Creating Queue branch " + queue.getName());

		Element queueElem = doc.createElement("Queue");
		queueElem.setAttribute("name", queue.getName());

		SortedMap<String, ClusterNodeAbstract> hosts = queue.getHosts();

		queueElem.appendChild(createHostsElem(doc, hosts));

		return queueElem;
	}

	public Element createHostsElem(Document doc, SortedMap<String, ClusterNodeAbstract> hosts) {

		Element hostsElem = doc.createElement("Hosts");
		for (Entry<String, ClusterNodeAbstract> hostEntry : hosts.entrySet()) {

			Host host = (Host) hostEntry.getValue();
			Element hostElem = createHostElem(doc, host);
			hostsElem.appendChild(hostElem);

		}

		return hostsElem;
	}

	public Element createHostElem(Document doc, Host host) {
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
		resElem.setTextContent(String.valueOf(host.getSlotReserved()));
		slotsElm.appendChild(resElem);

		Element usedElem = doc.createElement("Used");
		usedElem.setTextContent(String.valueOf(host.getSlotUsed()));
		slotsElm.appendChild(usedElem);

		Element totalElem = doc.createElement("Total");
		totalElem.setTextContent(String.valueOf(host.getSlotTotal()));
		slotsElm.appendChild(totalElem);

		hostElem.appendChild(slotsElm);

		Element memoryElem = doc.createElement("Memory");
		Element memTotalElem = doc.createElement("Total");
		memTotalElem.setTextContent(String.valueOf(host.getMemTotal()));
		memoryElem.appendChild(memTotalElem);

		Element memUsedElem = doc.createElement("Used");
		memUsedElem.setTextContent(String.valueOf(host.getMemUsedNum()));
		memoryElem.appendChild(memUsedElem);

		hostElem.appendChild(memoryElem);

		hostElem.appendChild(createJobsElement(doc, host));

		return hostElem;

	}

	public Element createJobsElement(Document doc, Host host) {

		ArrayList<Job> lstJobs = host.getListJob();

		Element jobsElem = doc.createElement("Jobs");

		for (Job job : lstJobs) {

			jobsElem.appendChild(createJobElement(doc, job));

		}

		return jobsElem;
	}

	public Element createJobElement(Document doc, Job job) {

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

	public void writeToFile(String strFile, Document doc) throws TransformerException {

		writeToFile(new File(strFile), doc);
	}

	public void writeToFile(File file, Document doc) throws TransformerException {

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(file);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		// Beautify the format of the resulted XML
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.transform(source, result);

	}

	/**
	 * @return the mainProps
	 */
	public SGE_MonitorProp getMainProps() {
		return mainProps;
	}

	/**
	 * @param mainProps the mainProps to set
	 */
	public void setMainProps(SGE_MonitorProp mainProps) {
		this.mainProps = mainProps;
	}

}
