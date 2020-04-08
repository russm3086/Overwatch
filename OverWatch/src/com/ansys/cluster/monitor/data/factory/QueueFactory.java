/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data.factory;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.data.HostMasterQueue;
import com.ansys.cluster.monitor.data.HostQueue;
import com.ansys.cluster.monitor.data.Job;
import com.ansys.cluster.monitor.data.JobMasterQueue;
import com.ansys.cluster.monitor.data.JobsQueue;
import com.ansys.cluster.monitor.data.NodeProp;
import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;

/**
 * 
 * @author rmartine
 * @since
 */
public class QueueFactory {
	private static final String sourceClass = QueueFactory.class.getName();
	private static final Logger logger = Logger.getLogger(sourceClass);
	public static String userName = new String();

	/**
	 * 
	 */
	private QueueFactory() {
		// TODO Auto-generated constructor stub
	}

	private static void getQueue(ClusterNodeAbstract node) {

		if ((node.getNodeProp().getQueueName() == null) || (node.getNodeProp().getQueueName().equalsIgnoreCase(""))) {

			logger.finest(node + " Queue Name + " + node.getNodeProp().getQueueName());

			if ((node.getNodeProp().getHostQueueName() == null)
					|| (node.getNodeProp().getHostQueueName().equalsIgnoreCase(""))) {

				logger.finest(node + " Host Queue Name + " + node.getNodeProp().getJobQueueName());
				if ((node.getNodeProp().getJobQueueName() == null)
						|| (node.getNodeProp().getJobQueueName().equalsIgnoreCase(""))) {

					logger.finest(node + " Host Queue Name + " + node.getNodeProp().getJobQueueName());
					if (node.getClusterType().equalsIgnoreCase(SGE_DataConst.clusterTypeHost)) {

						node.getNodeProp().setQueueName(SGE_DataConst.noNameHostQueue);

					} else if (node.getClusterType().equalsIgnoreCase(SGE_DataConst.clusterTypeJob)) {

						node.getNodeProp().setQueueName(SGE_DataConst.job_PendingQueue);
					}
				} else {

					node.getNodeProp().setQueueName(node.getNodeProp().getJobQueueName());
				}

			} else {

				node.getNodeProp().setQueueName(node.getNodeProp().getHostQueueName());
			}
		}
		logger.fine(node + ": Setting Queue name to " + node.getNodeProp().getQueueName());
	}

	private static void jobQueue(Job job) {
		logger.entering(sourceClass, "jobQueue", job);
		String queueName = job.getQueueName();
		int index = queueName.indexOf("@");
		if (index > 0) {
			logger.finer("Parsing Job queue " + queueName);
			String newQueueName = queueName.substring(0, index);
			String startHost = queueName.substring(index + 1);

			job.setQueueName(newQueueName);
			job.setStartHost(startHost);
		}

		if (queueName.equalsIgnoreCase(SGE_DataConst.job_PendingQueue)) {

			logger.finer(SGE_DataConst.job_PendingQueue + " looking ");
			NodeProp propList = job.getJB_hard_queue_list();

			if (propList != null) {
				String queue = (String) propList.get("QR_name");
				job.setTargetQueue(queue);
			} else {
				job.setTargetQueue(SGE_DataConst.job_ntq);
			}
		}
		logger.exiting(sourceClass, "jobQueue");
	}

	public static boolean myJobs(ClusterNodeAbstract node) {
		logger.entering(sourceClass, "myJobs", node);
		boolean result = false;
		String jobOwner = node.getNodeProp().getJobOwner();
		if (jobOwner != null && jobOwner.equalsIgnoreCase(userName)) {
			node.getNodeProp().setMyJobTrue();
			result = true;
		}

		logger.exiting(sourceClass, "myJobs", node);
		return result;

	}

	public static JobMasterQueue createJobMasterQueue(HashMap<Integer, Job> map, String regex) {
		JobMasterQueue masterQueue = new JobMasterQueue(SGE_DataConst.mqEntryJobs);

		map.forEach((id, job) -> {

			getQueue(job);
			jobQueue(job);
			detectVisualNode(job, regex);

			String queueName = job.getQueueName();

			if (!queueName.equalsIgnoreCase(SGE_DataConst.noNameHostQueue)) {

				if (masterQueue.containsKey(queueName)) {

					JobsQueue queue = masterQueue.getQueue(queueName);
					queue.addJob(job);
					logger.finer("Added node " + job + " to queue " + queue);

				} else {

					JobsQueue queue = new JobsQueue(job);

					logger.finer("Created queue " + queue + " added node " + job);
					masterQueue.addQueue(queue);
				}
			} else {

				logger.fine(job.getName() + " with queue " + queueName + " was excluded from queue");

			}

		});

		masterQueue.processQueues();
		return masterQueue;
	}

	public static HostMasterQueue createHostMasterQueue(HashMap<String, Host> map, String regex) {
		HostMasterQueue masterQueue = new HostMasterQueue(SGE_DataConst.mqEntryQueues);

		map.forEach((id, node) -> {

			getQueue(node);
			// jobQueue(node);
			detectVisualNode(node, regex);

			String queueName = node.getQueueName();

			if (!queueName.equalsIgnoreCase(SGE_DataConst.noNameHostQueue)) {

				if (masterQueue.containsKey(queueName)) {

					HostQueue queue = masterQueue.getQueue(queueName);
					queue.addHost(node);
					logger.finer("Added node " + node + " to queue " + queue);

				} else {

					HostQueue queue = new HostQueue(node);

					logger.finer("Created queue " + queue + " added node " + node);
					masterQueue.addQueue(queue);
				}
			} else {

				logger.fine(node.getName() + " with queue " + queueName + " was excluded from queue");

			}

		});

		masterQueue.processQueues();
		return masterQueue;
	}

	private static void detectVisualNode(ClusterNodeAbstract node, String regex) {
		if (node.getQueueName().matches(regex)) {
			node.setVisualNode(true);
		}
	}

	public static void addMyJobs(Cluster cluster, String userName) {
		logger.entering(sourceClass, "addMyJobs");
		if (userName != null && userName != "") {

			JobsQueue myJobQueue = new JobsQueue(SGE_DataConst.myJob);
			JobMasterQueue jobMasterQueue = cluster.getJobMasterQueue();

			SortedMap<String, JobsQueue> jobsQueuesMap = jobMasterQueue.getJobQueues();
			for (Entry<String, JobsQueue> entry : jobsQueuesMap.entrySet()) {
				logger.finer("Expecting Job queue " + entry.getKey());

				JobsQueue queue = entry.getValue();
				for (Entry<Integer, Job> entryJob : queue.getAllmaps().entrySet()) {

					Job job = entryJob.getValue();

					if (myJobs(job, userName)) {

						myJobQueue.addJob(job);
					}
				}
			}

			if (myJobQueue.size() > 0) {
				jobMasterQueue.addQueue(myJobQueue);
			}
		}

		logger.entering(userName, "addMyJobs");
	}

	public static boolean myJobs(Job job, String userName) {
		logger.entering(sourceClass, "myJobs", job);
		boolean result = false;
		if (job != null) {
			String jobOwner = job.getJobOwner();
			if (jobOwner != null && jobOwner.equalsIgnoreCase(userName)) {
				result = true;
			}
		}
		logger.exiting(sourceClass, "myJobs", result);
		return result;
	}

}
