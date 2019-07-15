/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data.factory;

import java.util.HashMap;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.AnsQueue;
import com.ansys.cluster.monitor.data.NodeProp;
import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;

/**
 * 
 * @author rmartine
 * @since
 */
public class QueueFactory2 {
	private static final String sourceClass = QueueFactory2.class.getName();
	private static final Logger logger = Logger.getLogger(sourceClass);
	public static String userName = new String();

	/**
	 * 
	 */
	private QueueFactory2() {
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
		logger.finest(node + ": Setting Queue name to " + node.getNodeProp().getQueueName());
	}

	private static void jobQueue(ClusterNodeAbstract node) {
		logger.entering(sourceClass, "jobQueue", node);
		String queueName = node.getNodeProp().getQueueName();
		int index = queueName.indexOf("@");
		if (index > 0) {
			logger.finer("Parsing Job queue " + queueName);
			String newQueueName = queueName.substring(0, index);
			String startHost = queueName.substring(index + 1);

			node.getNodeProp().setQueueName(newQueueName);
			node.getNodeProp().setStartHost(startHost);
		}

		if (queueName.equalsIgnoreCase(SGE_DataConst.job_PendingQueue)) {

			logger.finer(SGE_DataConst.job_PendingQueue + " looking ");
			NodeProp propList = (NodeProp) node.getNodeProp().get("JB_hard_queue_list");

			if (propList != null) {
				String queue = (String) propList.get("QR_name");
				node.getNodeProp().setTargetQueue(queue);
			} else {
				node.getNodeProp().setTargetQueue("NA");
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

	public static AnsQueue createMasterQueue(String queueName, String membersType, HashMap<?, ?> map) {

		AnsQueue masterQueue = new AnsQueue(queueName);
		masterQueue.setMembersType(membersType);

		map.forEach((id, node) -> {

			createNodeQueue((ClusterNodeAbstract) node, masterQueue);

		});

		return masterQueue;
	}

	public static AnsQueue createNodeQueue(ClusterNodeAbstract node, AnsQueue masterQueue) {
		logger.entering(sourceClass, "createQueue", node);
		getQueue(node);
		jobQueue(node);

		String queueName = node.getNodeProp().getQueueName();

		if (!queueName.equalsIgnoreCase(SGE_DataConst.noNameHostQueue)) {
			if (myJobs(node)) {
				queueName = SGE_DataConst.myJob;
			}

			if (masterQueue.containsKey(queueName)) {

				AnsQueue queue = masterQueue.get(queueName);
				queue.addNode(node);
				logger.finer("Added node " + node + " to queue " + queue);

			} else {

				AnsQueue queue;

				if (node.getNodeProp().isMyJob()) {
					queue = new AnsQueue(node, SGE_DataConst.myJob);
				} else {
					queue = new AnsQueue(node);
				}

				logger.finer("Created queue " + queue + " added node " + node);
				masterQueue.addNode(queue);
			}
		} else {

			logger.fine(node.getName() + " with queue " + queueName + " was excluded from queue");

		}
		logger.exiting(sourceClass, "createQueue", masterQueue);
		return masterQueue;

	}

}
