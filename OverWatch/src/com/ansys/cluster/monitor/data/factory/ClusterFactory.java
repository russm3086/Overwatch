/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data.factory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import org.jdom2.JDOMException;
import org.json.JSONException;

import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.data.HostMasterQueue;
import com.ansys.cluster.monitor.data.HostQueue;
import com.ansys.cluster.monitor.data.Job;
import com.ansys.cluster.monitor.data.JobMasterQueue;
import com.ansys.cluster.monitor.data.JobsQueue;
import com.ansys.cluster.monitor.data.MyJobsMasterQueue;
import com.ansys.cluster.monitor.data.NodeProp;
import com.ansys.cluster.monitor.data.Quota;
import com.ansys.cluster.monitor.gui.Console;
import com.ansys.cluster.monitor.main.SGE_DataConst;
import com.ansys.cluster.monitor.net.ConcurrentDataCollector;
import com.ansys.cluster.monitor.net.Connector;
import com.ansys.cluster.monitor.net.DataCollector;
import com.ansys.cluster.monitor.net.Payload;
import com.ansys.cluster.monitor.net.DataCollectorWorker.SrcType;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * 
 * @author rmartine
 * @since
 */
public class ClusterFactory {
	private static final String sourceClass = QueueFactory.class.getName();
	private static final Logger logger = Logger.getLogger(sourceClass);
	private static boolean consoleMode = false;

	/**
	 * 
	 */
	private ClusterFactory() {
		// TODO Auto-generated constructor stub

	}

	public static void setStatusLabel(String msg) {
		if (consoleMode == true)
			Console.setStatusLabel(msg);
	}

	public static Cluster createCluster(SGE_MonitorProp mainProps, boolean consoleMode, String userName)
			throws JSONException, IOException, URISyntaxException, JDOMException, InterruptedException,
			TransformerException, ClassNotFoundException {
		ClusterFactory.consoleMode = consoleMode;
		Connector conn = new Connector(mainProps);
		DataCollector dc = new DataCollector(mainProps, conn);
		int index = mainProps.getClusterIndex();
		String clusterName = mainProps.getClusterName(mainProps.getClusterIndex());

		logger.entering(sourceClass, "createCluster");
		logger.info("Getting environment data");

		ZoneId zoneId = mainProps.getClusterZoneId(index);
		logger.info("Setting TimeZone to " + zoneId);

		mainProps.setClusterZoneId(zoneId);

		if (mainProps.getUsernameOverride() != null && (mainProps.getUsernameOverride().trim().length() != 0))
			userName = mainProps.getUsernameOverride();

		logger.info("Current user: " + userName);

		ConcurrentDataCollector cdc = new ConcurrentDataCollector(dc, index, 4, 10, TimeUnit.SECONDS);
		HashMap<SrcType, Payload> resultMap = cdc.collect();

		logger.info("Creating quota objects");
		setStatusLabel("Creating quota objects");
		LinkedList<Quota> quotaList = QuotaFactory.createQuotaList(resultMap.get(SrcType.QUOTA_DATA), mainProps,
				userName);

		logger.info("Creating job objects");
		setStatusLabel("Creating job objects");
		HashMap<Integer, Job> DetailedJobsmap = JobFactory.createJobsMap(resultMap.get(SrcType.JOB_DATA),
				resultMap.get(SrcType.DETAILED_JOB_DATA), mainProps);

		logger.info("Creating host objects");
		setStatusLabel("Creating host objects");
		HashMap<String, Host> hostMap = HostFactory.createHostMap(resultMap.get(SrcType.HOST_DATA), mainProps);

		logger.info("Job and Host cross reference");
		setStatusLabel("Job and Host cross referencing");

		/**
		 * Matches jobs with host. Detect for idle jobs
		 */
		JobHostCrossReference(DetailedJobsmap, hostMap);

		logger.info("Creating Job Queues");
		setStatusLabel("Creating Job Queues");
		JobMasterQueue jobMasterQueue = QueueFactory.createJobMasterQueue(DetailedJobsmap,
				mainProps.getClusterQueueVisualRegex());

		logger.info("Creating Host Queues");
		setStatusLabel("Creating Host Queues");
		HostMasterQueue hostMasterQueue = QueueFactory.createHostMasterQueue(hostMap,
				mainProps.getClusterQueueVisualRegex());

		logger.info("Creating MyJob Queues");
		setStatusLabel("Creating MyJob Queues");
		MyJobsMasterQueue myJobsMasterQueue = new MyJobsMasterQueue(quotaList, jobMasterQueue, userName);

		logger.info("Assigning jobs to target queues");
		JobQueueTargetQueue(jobMasterQueue, hostMasterQueue);

		logger.info("Creating Cluster object");
		setStatusLabel("Creating Cluster object " + clusterName);

		Cluster cluster = new Cluster(clusterName, hostMasterQueue, jobMasterQueue, myJobsMasterQueue, zoneId,
				mainProps.getMedianTimeThrowOut());

		logger.exiting(sourceClass, "createCluster", cluster);
		return cluster;

	}

	public static void JobQueueTargetQueue(JobMasterQueue jobMasterQueue, HostMasterQueue hostMasterQueue) {
		logger.entering(sourceClass, "JobQueueTargetQueue");
		JobsQueue jobPendingQueue = jobMasterQueue.getQueue(SGE_DataConst.job_PendingQueue);

		if (jobPendingQueue != null) {
			for (Entry<Integer, Job> entry : jobPendingQueue.getPendingJobs().entrySet()) {

				Job job = entry.getValue();
				String targetQueue = job.getTargetQueue();

				if (targetQueue.indexOf("@") > 0) {

					int index = targetQueue.indexOf("@");
					logger.finer("Parsing Job queue " + targetQueue);
					targetQueue = targetQueue.substring(0, index);
				}

				HostQueue hostTargetQueue = hostMasterQueue.getQueue(targetQueue);

				if (hostTargetQueue != null) {

					logger.finer("Pending Job: " + job + " adding to queue: " + hostTargetQueue);
					hostTargetQueue.addPendingJobs(job.getJobNumber(), job);
				} else {

					if (!targetQueue.equalsIgnoreCase(SGE_DataConst.job_ntq))
						logger.severe("Target Queue " + targetQueue + " for pending job " + job.getJobNumber()
								+ " does not exist");

				}

			}
		} else {

			logger.info("0 Pending Jobs");
		}

		logger.exiting(sourceClass, "JobQueueTargetQueue");
	}

	public static void JobHostCrossReference(HashMap<Integer, Job> DetailedJobsmap, HashMap<String, Host> hostMap) {

		for (Integer jobId : DetailedJobsmap.keySet()) {

			Job job = DetailedJobsmap.get(jobId);
			ArrayList<NodeProp> list = job.getResourceList();
			for (NodeProp nodeProp : list) {

				String hostName = new String();
				if (nodeProp.get("GRU_host") != null) {

					hostName = (String) nodeProp.get("GRU_host");
				} else if (nodeProp.get("JG_qhostname") != null) {

					hostName = (String) nodeProp.get("JG_qhostname");
				}

				Host host = hostMap.get(hostName);

				if (host != null) {
					logger.finer("Added job id: " + jobId + " to " + hostName);
					job.addHost(host);
					host.addJob(job);

				}
			}
		}

	}

}
