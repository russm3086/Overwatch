/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data.factory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import org.jdom2.JDOMException;
import org.json.JSONException;

import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.data.Job;
import com.ansys.cluster.monitor.data.NodeProp;
import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.gui.Console;
import com.ansys.cluster.monitor.data.AnsQueue;
import com.ansys.cluster.monitor.net.DataCollector;
import com.ansys.cluster.monitor.net.Payload;
import com.ansys.cluster.monitor.net.SGE_ConnectConst;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * 
 * @author rmartine
 * @since
 */
public class ClusterFactory {
	private static final String sourceClass = QueueFactory.class.getName();
	private static final Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */
	private ClusterFactory() {
		// TODO Auto-generated constructor stub

	}

	public static Cluster createCluster(DataCollector dc, String clusterName, int index, SGE_MonitorProp mainProps)
			throws ClassNotFoundException, IOException, JSONException, URISyntaxException, JDOMException,
			InterruptedException, TransformerException {

		// TODO props setting of serialized object
		Cluster cluster = null;
		if (mainProps.getClusterConnectionRequestContentType().equalsIgnoreCase(SGE_ConnectConst.clusterType)) {

			Payload payload = dc.getCluster(index);
			cluster = payload.getClusterObject();
		} else {

			cluster = createStringCluster(dc, clusterName, index, mainProps);
		}
		return cluster;
	}

	public static Cluster createStringCluster(DataCollector dc, String clusterName, int index,
			SGE_MonitorProp mainProps) throws JSONException, IOException, URISyntaxException, JDOMException,
			InterruptedException, TransformerException, ClassNotFoundException {
		logger.entering(sourceClass, "createCluster");
		logger.info("Getting host data");
		Console.setStatusLabel("Getting host data");
		Payload payLoadHost = dc.getHostsData(index);

		logger.info("Getting job data");
		Console.setStatusLabel("Getting job data");
		Payload payLoadJob = dc.getJobsData(index);

		logger.info("Getting detailed job data");
		Console.setStatusLabel("Getting detailed job data");
		Payload payLoadoDetailedJob = dc.getDetailedJobsData(index);

		logger.info("Creating host objects");
		HashMap<String, Host> hostMap = HostFactory.createHostMap(payLoadHost, mainProps);

		logger.info("Creating job objects");
		Console.setStatusLabel("Creating job objects");
		HashMap<Integer, Job> DetailedJobsmap = JobFactory.createJobsMap(payLoadJob, payLoadoDetailedJob, mainProps);

		//logger.info("Setting user for MyJobs queue (" + System.getProperty("user.name") + ")");
		//QueueFactory.userName = System.getProperty("user.name");

		logger.info("Job and Host cross reference");
		Console.setStatusLabel("Job and Host cross referencing");
		JobHostCrossReference(DetailedJobsmap, hostMap);

		logger.info("Creating Job Queues");
		Console.setStatusLabel("Creating Job Queues");
		AnsQueue jobsQueue = QueueFactory.createMasterQueue(SGE_DataConst.mqEntryJobs, SGE_DataConst.clusterTypeQueue,
				DetailedJobsmap);

		logger.info("Creating Host Queues");
		Console.setStatusLabel("Creating Host Queues");
		AnsQueue hostsQueue = QueueFactory.createMasterQueue(SGE_DataConst.mqEntryQueues,
				SGE_DataConst.clusterTypeQueue, hostMap);

		logger.info("Assigning jobs to target queues");
		JobQueueTargetQueue(jobsQueue, hostsQueue);

		logger.info("Creating Cluster object");
		Console.setStatusLabel("Creating Cluster object " + clusterName);
		Cluster cluster = new Cluster(clusterName, hostsQueue, jobsQueue);

		logger.exiting(sourceClass, "createCluster", cluster);
		return cluster;

	}

	public static void JobQueueTargetQueue(AnsQueue jobsQueue, AnsQueue hostsQueue) {
		logger.entering(sourceClass, "JobQueueTargetQueue");
		AnsQueue jobPendingQueue = jobsQueue.get(SGE_DataConst.job_PendingQueue);

		if (jobPendingQueue != null) {
			for (Entry<String, ClusterNodeAbstract> entry : jobPendingQueue.getNodes().entrySet()) {

				Job job = (Job) entry.getValue();
				String targetQueue = job.getTargetQueue();
				AnsQueue hostTargetQueue = hostsQueue.get(targetQueue);

				if (hostTargetQueue != null) {

					logger.finer("Pending Job: " + job + " adding to queue: " + hostTargetQueue);
					hostTargetQueue.addPendingJobs(job);
				} else {

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

				String hostName = (String) nodeProp.get("GRU_host");
				Host host = hostMap.get(hostName);

				if (host != null) {
					logger.finer("Added job id: " + jobId + " to " + hostName);
					host.addJob(job);
					job.addHost(host);

				}
			}
		}

	}

}
