/**
 * 
 */
package com.ansys.cluster.monitor.data.interfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.data.Job;
import com.ansys.cluster.monitor.data.JobMessage;
import com.ansys.cluster.monitor.data.NodeProp;
import com.ansys.cluster.monitor.net.Payload;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * @author rmartine
 *
 */
public abstract class ParserAbstract {
	private String sourceClass = this.getClass().getName();
	private Logger logger = Logger.getLogger(sourceClass);
	protected Payload payloadHosts;
	protected SGE_MonitorProp mainProps;
	protected Payload payloadJobs;
	protected Payload payLoadDetailedJobs;

	public ParserAbstract(Payload payloadHosts, SGE_MonitorProp mainProps) {
		setPayloadHosts(payloadHosts);
		setMainProps(mainProps);
	}

	public ParserAbstract(Payload payloadJobs, Payload payLoadDetailedJobs, SGE_MonitorProp mainProps) {
		setPayLoadDetailedJobs(payLoadDetailedJobs);
		setPayloadJobs(payloadJobs);
		setMainProps(mainProps);
	}

	public abstract HashMap<String, Host> createHostsMap();

	public abstract HashMap<Integer, Job> createJobsMap();

	public Payload getPayloadHosts() {
		return payloadHosts;
	}

	public void setPayloadHosts(Payload payloadHosts) {
		this.payloadHosts = payloadHosts;
	}

	public Payload getPayloadJobs() {
		return payloadJobs;
	}

	public void setPayloadJobs(Payload payloadJobs) {
		this.payloadJobs = payloadJobs;
	}

	public SGE_MonitorProp getMainProps() {
		return mainProps;
	}

	public void setMainProps(SGE_MonitorProp mainProps) {
		this.mainProps = mainProps;
	}

	public Payload getPayLoadDetailedJobs() {
		return payLoadDetailedJobs;
	}

	public void setPayLoadDetailedJobs(Payload payLoadDetailedJobs) {
		this.payLoadDetailedJobs = payLoadDetailedJobs;
	}

	protected boolean matchSettings(String srcQueue, String comparisonQueue) {
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(srcQueue.split(" ")));
		logger.finer("Verify setting: " + comparisonQueue);
		boolean result = list.contains(comparisonQueue);
		logger.finer("Verificaiton result: " + result);
		return result;
	}

	protected HashMap<Integer, Job> mergeSummaryDetailedJobs(HashMap<Integer, Job> mapJobs,
			HashMap<Integer, NodeProp> mapDetailedNodeProps) {
		logger.entering(sourceClass, "mergeSummaryDetailedJobs");

		for (Integer jobId : mapJobs.keySet()) {

			logger.finest("Processing job id: " + jobId);
			NodeProp prop = mapDetailedNodeProps.get(jobId);

			if (prop != null) {

				Job job = mapJobs.get(jobId);
				logger.finest("Merging job " + jobId);
				job.mergeData(prop);
				mapDetailedNodeProps.remove(jobId);

			} else {
				logger.severe("Did not find detail job information for job " + jobId);
			}
		}

		if (logger.isLoggable(Level.SEVERE)) {
			if (mapDetailedNodeProps.size() > 0) {
				StringBuffer sb = new StringBuffer();
				for (Integer key : mapDetailedNodeProps.keySet()) {
					sb.append(key + " ");
				}
				logger.severe("Could not find job summaries for the following job details: " + sb);
			}
		}

		logger.exiting(sourceClass, "mergeSummaryDetailedJobs");
		return mapJobs;
	}

	protected void addJobMsgsToJobs(ArrayList<JobMessage> listJobMsg, HashMap<Integer, NodeProp> hashMap) {
		logger.entering(sourceClass, "addJobMsgsToJobs");
		logger.info("Parsing " + listJobMsg.size() + " job message list");
		for (JobMessage jobMsg : listJobMsg) {

			logger.finer("Inspecting job message " + jobMsg.getMessageNumber());
			@SuppressWarnings("unchecked")
			LinkedHashSet<Integer> hashList = (LinkedHashSet<Integer>) jobMsg.getJobList();
			Iterator<Integer> iter = hashList.iterator();

			while (iter.hasNext()) {

				Integer jobId = iter.next();
				NodeProp prop = hashMap.get(jobId);

				if (prop != null) {

					logger.finer("Found job message match job msg #: " + jobMsg.getMessageNumber() + " job #: "
							+ prop.getJobNumber() + " " + jobMsg.getMessage());
					prop.addJobMessage(jobMsg);
					iter.remove();
				}
			}
		}

		logger.exiting(sourceClass, "addJobMsgsToJobs");
	}


}
