/**
 * 
 */
package com.ansys.cluster.monitor.data.factory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONException;
import org.jdom2.Document;
import org.jdom2.Element;

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

		Document doc = new Document();

		// root element
		doc.setRootElement(new Element("Clusters"));

		Element rootElement = doc.getRootElement();

		// Time Stamp Element
		rootElement.addContent(timeStamp());

		// Queues Element
		rootElement.addContent(createQueuesElm(cluster));

		return doc;
	}

	public Element timeStamp() {

		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		String formattedDateTime = currentDateTime.format(formatter);

		Element dateStampElement = new Element("TimeStamp");
		dateStampElement.addContent(formattedDateTime);

		return dateStampElement;
	}

	public Element createQueuesElm(Cluster cluster) {

		AnsQueue masterQueue = cluster.getMasterQueue().get(SGE_DataConst.mqEntryQueues);

		Element queuesElem = new Element("Queues");

		SortedMap<String, ClusterNodeAbstract> queues = masterQueue.getNodes();

		for (Entry<String, ClusterNodeAbstract> queue : queues.entrySet()) {
			logger.finer("Creating Queue branch " + queue.getValue());

			Element queueElem = new Element("Queue");
			queueElem.setAttribute("name", queue.getKey());
			AnsQueue subQueue = (AnsQueue) queue.getValue();

			// Element hostsElem = new Element("Hosts");

			SortedMap<String, ClusterNodeAbstract> hosts = subQueue.getHosts();

			Element hostsElem = createHostsElem(hosts);

			queueElem.addContent(hostsElem);

			queuesElem.addContent(queueElem);

		}

		return queuesElem;

	}

	public Element createQueueElm(AnsQueue queue) {

		logger.finer("Creating Queue branch " + queue.getName());

		Element queueElem = new Element("Queue");
		queueElem.setAttribute("name", queue.getName());

		SortedMap<String, ClusterNodeAbstract> hosts = queue.getHosts();

		queueElem.addContent(createHostsElem(hosts));

		return queueElem;
	}

	public Element createHostsElem(SortedMap<String, ClusterNodeAbstract> hosts) {

		Element hostsElem = new Element("Hosts");
		for (Entry<String, ClusterNodeAbstract> hostEntry : hosts.entrySet()) {

			Host host = (Host) hostEntry.getValue();
			Element hostElem = createHostElem(host);
			hostsElem.addContent(hostElem);

		}

		return hostsElem;
	}

	public Element createHostElem(Host host) {
		Element hostElem = new Element("Host");
		hostElem.setAttribute("name", host.getName());

		Element hostExclusiveElem = new Element("Exclusive");
		hostExclusiveElem.setText(String.valueOf(host.isExclusive()));
		hostElem.addContent(hostExclusiveElem);

		Element hostStateCodeElem = new Element("State");
		hostStateCodeElem.setText(host.getStateCode());
		hostElem.addContent(hostStateCodeElem);

		Element slotsElm = new Element("Slots");
		Element resElem = new Element("Reserved");
		resElem.setText(String.valueOf(host.getSlotReserved()));
		slotsElm.addContent(resElem);

		Element usedElem = new Element("Used");
		usedElem.setText(String.valueOf(host.getSlotUsed()));
		slotsElm.addContent(usedElem);

		Element totalElem = new Element("Total");
		totalElem.setText(String.valueOf(host.getSlotTotal()));
		slotsElm.addContent(totalElem);

		hostElem.addContent(slotsElm);

		Element memoryElem = new Element("Memory");
		Element memTotalElem = new Element("Total");
		memTotalElem.setText(String.valueOf(host.getMemTotal()));
		memoryElem.addContent(memTotalElem);

		Element memUsedElem = new Element("Used");
		memUsedElem.setText(String.valueOf(host.getMemUsedNum()));
		memoryElem.addContent(memUsedElem);

		hostElem.addContent(memoryElem);

		hostElem.addContent(createJobsElement(host));

		return hostElem;

	}

	public Element createJobsElement(Host host) {

		ArrayList<Job> lstJobs = host.getListJob();

		Element jobsElem = new Element("Jobs");

		for (Job job : lstJobs) {

			jobsElem.addContent(createJobElement(job));

		}

		return jobsElem;
	}

	public Element createJobElement(Job job) {

		Element jobElem = new Element("Job");
		jobElem.setAttribute("job_id", job.getIdentifier());

		Element jobNameElem = new Element("Name");
		jobNameElem.setText(job.getName());

		Element jobOwnerElem = new Element("owner");
		jobOwnerElem.setText(job.getOwner());
		jobElem.addContent(jobOwnerElem);

		Element jobSlotsElem = new Element("slots");
		jobSlotsElem.setText(String.valueOf(job.getSlots()));
		jobElem.addContent(jobSlotsElem);

		Element jobStateElem = new Element("state");
		jobStateElem.setText(job.getJobState());
		jobElem.addContent(jobStateElem);

		Element jobExclusiveElem = new Element("Exclusive");
		jobExclusiveElem.setText(String.valueOf(job.isExclusive()));
		jobElem.addContent(jobExclusiveElem);

		Element jobTargetQueueElem = new Element("Target_Queue");
		jobTargetQueueElem.setText(job.getTargetQueue());
		jobElem.addContent(jobTargetQueueElem);

		return jobElem;

	}

	public static void writeToFile(String strFile, Document doc) throws  IOException {

		writeToFile(new File(strFile), doc);
	}

	public static void writeToFile(File file, Document doc) throws IOException {

		XMLOutputter xmlOutput = new XMLOutputter();

		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.output(doc, new FileWriter(file));

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
