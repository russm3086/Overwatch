/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Map.Entry;
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
	private SortedMap<String, MasterQueue> masterQueue = new TreeMap<String, MasterQueue>(Collections.reverseOrder());

	String name;

	public Cluster(String clusterName, HostMasterQueue hostMasterQueue, JobMasterQueue jobMasterQueue) {
		// TODO Auto-generated constructor stub
		logger.entering(sourceClass, "Constructor");

		setName(clusterName);

		processHostQueue(hostMasterQueue);
		processJobQueue(jobMasterQueue);

		setJobMasterQueue(jobMasterQueue);
		setHostMasterQueue(hostMasterQueue);

		setClusterType(SGE_DataConst.clusterTypeCluster);

		addState(ClusterState.Normal);

		logger.exiting(sourceClass, "Constructor");
	}

	private void processHostQueue(HostMasterQueue hostMasterQueue) {

		SortedMap<String, HostQueue> hostMap = hostMasterQueue.getHostQueues();
		for (Entry<String, HostQueue> entryQueue : hostMap.entrySet()) {

			HostQueue hostQueue = entryQueue.getValue();

			logger.finer("Processing Queue " + hostQueue.getName());
			logger.finer("Total Nodes " + hostQueue.size());
			addTotalNodes(hostQueue.size());
			logger.finer("Total Free Nodes " + hostQueue.getAvailableNodes());
			addTotalFreeNodes(hostQueue.getAvailableNodes());
			addFreeMem(hostQueue.getFreeMem());
			addFreeSlots(hostQueue.getSlotAvailable());
			addDisabledHostCount(hostQueue.getDisabledNode().size());
		}
	}

	private void processJobQueue(JobMasterQueue jobMasterQueue) {

		SortedMap<String, JobsQueue> jobMap = jobMasterQueue.getJobQueues();
		for (Entry<String, JobsQueue> entryQueue : jobMap.entrySet()) {

			JobsQueue jobQueue = entryQueue.getValue();

			logger.finer("Processing Queue " + jobQueue.getName());
			logger.finer("Total Nodes " + jobQueue.size());
			addTotalNumberJobs(jobQueue.size());
			addTotalSlots(jobQueue.getSlotTotal());

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
		sb.append("\nFree Memory: " + decimalFormatter.format(getFreeMem()));
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

	public int getTotalNumberJobs() {
		return totalNumberJobs;
	}

	public void addTotalNumberJobs(int totalNumberJobs) {
		this.totalNumberJobs += totalNumberJobs;
	}

	public void addState(StateAbstract state) {
		addState(state, ClusterState.Normal);
	}

	public String getQueueName() {
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

	/**
	 * @return the hostMasterQueue
	 */
	public HostMasterQueue getHostMasterQueue() {
		return (HostMasterQueue) masterQueue.get(SGE_DataConst.mqEntryQueues);
	}

	/**
	 * @param hostMasterQueue the hostMasterQueue to set
	 */
	public void setHostMasterQueue(HostMasterQueue hostMasterQueue) {
		masterQueue.put(SGE_DataConst.mqEntryQueues, hostMasterQueue);
	}

	/**
	 * @return the jobMasterQueue
	 */
	public JobMasterQueue getJobMasterQueue() {
		return (JobMasterQueue) masterQueue.get(SGE_DataConst.mqEntryJobs);
	}

	/**
	 * @param jobMasterQueue the jobMasterQueue to set
	 */
	public void setJobMasterQueue(JobMasterQueue jobMasterQueue) {
		masterQueue.put(SGE_DataConst.mqEntryJobs, jobMasterQueue);
	}

	public SortedMap<String, MasterQueue> getMasterQueue(){
		return masterQueue;
	}
	
}
