/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data;

import java.text.DecimalFormat;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.ClusterNodeInterface;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.ansys.cluster.monitor.data.state.ClusterState;

/**
 * 
 * @author rmartine
 * @since
 */
public class Cluster extends ClusterNodeAbstract implements ClusterNodeInterface {
	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);
	private static final long serialVersionUID = -3265482837785245141L;
	private int totalNumberNodes = 0;
	private int totalFreeNodes = 0;
	private int totalSlots = 0;
	private double freeMem = 0;
	private int freeSlots = 0;
	private int totalNumberJobs = 0;
	private int disabledHostCount = 0;
	private DecimalFormat df2 = new DecimalFormat(".##");

	private ConcurrentHashMap<String, AnsQueue> masterQueue = new ConcurrentHashMap<String, AnsQueue>();

	String name;

	public Cluster(String clusterName, AnsQueue hostQueue, AnsQueue jobQueue) {
		// TODO Auto-generated constructor stub
		logger.entering(sourceClass, "Constructor");

		setName(clusterName);

		processQueue(hostQueue);
		processQueue(jobQueue);

		masterQueue.put(hostQueue.getName(), hostQueue);
		masterQueue.put(jobQueue.getName(), jobQueue);
		
		setClusterType(SGE_DataConst.clusterTypeCluster);
		
		addState(ClusterState.Normal);

		logger.exiting(sourceClass, "Constructor");
	}

	private void processQueue(AnsQueue queue) {

		SortedMap<String, ClusterNodeAbstract> queueMap = queue.getNodes();
		for (Entry<String, ClusterNodeAbstract> entryQueue : queueMap.entrySet()) {

			AnsQueue ansQueue = (AnsQueue) entryQueue.getValue();

			switch (ansQueue.getMembersType()) {

			case SGE_DataConst.clusterTypeHost:

				logger.finer("Processing Queue " + ansQueue.getName());
				logger.finer("Total Nodes " + ansQueue.getHosts().size());
				addTotalNodes(ansQueue.size());
				logger.finer("Total Free Nodes " + ansQueue.getAvailableNodes());
				addTotalFreeNodes(ansQueue.getAvailableNodes());
				addFreeMem(ansQueue.getFreeMem());
				addFreeSlots(ansQueue.getSlotAvailable());
				addDisabledHostCount(ansQueue.getDisabledNode().size());

				break;

			case SGE_DataConst.clusterTypeJob:

				addTotalNumberJobs(ansQueue.size());
				addTotalSlots(ansQueue.getSlotTotal());

				break;
			}
		}
	}

	public void processJobQueue(ConcurrentHashMap<String, AnsQueue> hostJobMap) {
		logger.entering(sourceClass, "Constructor", hostJobMap);

		for (Entry<String, AnsQueue> map : hostJobMap.entrySet()) {

			AnsQueue queue = map.getValue();
			addTotalNumberJobs(queue.getJobs().size());
			addTotalSlots(queue.getSlotTotal());

		}
	}

	public void addTotalNodes(int totalNumberNodes) {
		this.totalNumberNodes += totalNumberNodes;
	}

	public int getTotalNodes() {
		return totalNumberNodes;
	}

	/**
	 * @return the totalFreeNodes
	 */
	public int getTotalFreeNodes() {
		return totalFreeNodes;
	}

	/**
	 * @param totalFreeNodes the totalFreeNodes to set
	 */
	public void addTotalFreeNodes(int totalFreeNodes) {
		this.totalFreeNodes += totalFreeNodes;
	}

	/**
	 * @return the freeMem
	 */
	public double getFreeMem() {
		return freeMem;
	}

	/**
	 * @param freeMem the freeMem to set
	 */
	public void addFreeMem(double freeMem) {
		this.freeMem += freeMem;
	}

	/**
	 * @return the freeSlots
	 */
	public int getFreeSlots() {
		return freeSlots;
	}

	/**
	 * @param freeSlots the freeSlots to set
	 */
	public void addFreeSlots(int freeSlots) {
		logger.finest("adding " + freeSlots + " free slots to " + this.freeSlots);
		this.freeSlots += freeSlots;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return the totalSlots
	 */
	public int getTotalSlots() {
		return totalSlots;
	}

	/**
	 * @param totalSlots the totalSlots to set
	 */
	public void addTotalSlots(int totalSlots) {
		this.totalSlots += totalSlots;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.ClusterNode#status()
	 */
	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("Free Nodes: " + getTotalFreeNodes());
		sb.append("\nFree Cores: " + getFreeSlots());
		sb.append("\nFree Memory: " + df2.format(getFreeMem()));
		sb.append("\nTotal Node Count: " + getTotalNodes());
		sb.append("\nTotal Active Job Count: " + getTotalNumberJobs());
		sb.append("\nTotal Active disable node Count: " + getDisabledHostCount());

		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.ClusterNode#clusterType()
	 */
	@Override
	public String getSummary() {
		// TODO Auto-generated method stub

		return getStatus();
	}

	public String toString() {
		return name;
	}

	public ConcurrentHashMap<String, AnsQueue> getMasterQueue() {
		return masterQueue;
	}

	public void setMasterQue(ConcurrentHashMap<String, AnsQueue> masterQueue) {
		this.masterQueue = masterQueue;
	}

	public int getTotalNumberJobs() {
		return totalNumberJobs;
	}

	public void addTotalNumberJobs(int totalNumberJobs) {
		this.totalNumberJobs += totalNumberJobs;
	}

	public void addState(StateAbstract state) {
		addState(state, ClusterState.Normal);
	}

	public String  getQueueName() {
		return "NA";
	}

	/**
	 * @return the disabledHost
	 */
	public int getDisabledHostCount() {
		return disabledHostCount;
	}

	/**
	 * @param disabledHost the disabledHost to set
	 */
	public void addDisabledHostCount(int disabledHostCount) {
		this.disabledHostCount += disabledHostCount;
	}
	
	
}
