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

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.ansys.cluster.monitor.data.state.ClusterState;
import com.russ.test.DetailedInfoProp;

/**
 * 
 * @author rmartine
 * @since
 */
public class Cluster extends AnsQueueAbstract {
	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);
	private static final long serialVersionUID = -3265482837785245141L;

	private int totalComputeNodes = 0;
	private int availableComputeNodes = 0;
	
	private int totalVisualNodes = 0;
	private int availalableVisualNodes = 0;

	private int totalCores = 0;
	private int availableCores = 0;

	private double totalMem = 0;
	private double availableMem = 0;

	private int totalSession = 0;
	private int availableSessions = 0;

	private int totalNumberJobs = 0;
	private SortedMap<String, MasterQueue> masterQueue = new TreeMap<String, MasterQueue>(Collections.reverseOrder());

	String name;

	// TODO Gather all Slot information Total, Used, Used Reserved, Used Exclusive
	// TODO Gather all node information # Running, # Available, # out of service
	// TODO Gather all Job information # Running, # Pending, # Pending Error
	// TODO Gather all Job information Duration, # Pending, # Pending Error

	public Cluster(String clusterName, HostMasterQueue hostMasterQueue, JobMasterQueue jobMasterQueue) {
		super();
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

			if (hostQueue.isVisualNode()) {
				addTotalVisualNodes(hostQueue.size());
				addTotalFreeVisualNodes(hostQueue.getAvailableNodes());
				addTotalSession(hostQueue.getSessionTotal());
				addAvailableSessions(hostQueue.getSessionAvailable());

			} else {
				addAvailableComputeNodes(hostQueue.getAvailableNodes());
				addTotalComputeNodes(hostQueue.size());
				addAvailableMem(hostQueue.getFreeMem());

				addAvailableCores(hostQueue.getSlotAvailable());
				addTotalCores(hostQueue.getSlotTotal());

				addTotalMem(hostQueue.getTotalMem());

			}

			addDisabledHosts(hostQueue.getDisabledNodes());

		}
	}

	private void processJobQueue(JobMasterQueue jobMasterQueue) {

		SortedMap<String, JobsQueue> jobMap = jobMasterQueue.getJobQueues();
		for (Entry<String, JobsQueue> entryQueue : jobMap.entrySet()) {

			JobsQueue jobQueue = entryQueue.getValue();

			jobQueue.checkForIdle();

			logger.finer("Processing Queue " + jobQueue.getName());
			logger.finer("Total Nodes " + jobQueue.size());
			addTotalNumberJobs(jobQueue.size());

			if (jobMasterQueue.isVisualNode()) {

			} else {

			}

			if (jobQueue.getIdleJobs().size() > 0)
				addIdleJobs(jobQueue.getIdleJobs());
		}
	}

	/**
	 * @return the freeMem
	 */
	public double getAvailableMem() {
		return availableMem;
	}

	/**
	 * @param freeMem the freeMem to set
	 */
	public void addAvailableMem(double availableMem) {
		setAvailableMem(getAvailableMem() + availableMem);
	}

	public void setAvailableMem(double availableMem) {
		this.availableMem = availableMem;
	}

	public String getName() {
		return name;
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
		sb.append("Compute Nodes\n");
		sb.append(outputFormatter("Total Compute Node:", getTotalComputeNodes()));
		sb.append(outputFormatter("Available Compute Node:", getAvailableComputeNodes()));

		sb.append("\n");
		sb.append(outputFormatter("Total Cores:", getTotalCores()));
		sb.append(outputFormatter("Avaiable Cores:", getAvailableCores()));
		sb.append(outputFormatter("Total Memory:", decimalFormatter.format(getTotalMem())));
		sb.append(outputFormatter("Free Memory:", decimalFormatter.format(getAvailableMem())));

		sb.append("\n\nVisual Nodes\n");
		sb.append(outputFormatter("Total Visual Node:", getTotalVisualNodes()));
		sb.append(outputFormatter("Available Visual Node(s):", getAvailalableVisualNodes()));
		sb.append(outputFormatter("Total Session:", getTotalSession()));
		sb.append(outputFormatter("Available Session:", getAvailableSessions()));

		sb.append("\n\nJobs:\n");
		sb.append(outputFormatter("Total Active Job Count:", getTotalNumberJobs()));
		sb.append(outputFormatter("Jobs Pending:", getPendingJobs()));
		sb.append(outputFormatter("Total Idle Jobs:", totalIdleJobs()));

		sb.append("\n");

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

	public void addState(StateAbstract state) {
		addState(state, ClusterState.Normal);
	}

	public String getQueueName() {
		return "NA";
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

	public SortedMap<String, MasterQueue> getMasterQueue() {
		return masterQueue;
	}

	/**
	 * @return the totalNumberComputeNodes
	 */
	public int getTotalComputeNodes() {
		return totalComputeNodes;
	}

	/**
	 * @param totalNumberComputeNodes the totalNumberComputeNodes to set
	 */
	public void setTotalComputeNodes(int totalComputeNodes) {
		this.totalComputeNodes = totalComputeNodes;
	}

	public void addTotalComputeNodes(int totalComputeNodes) {
		setTotalComputeNodes(getTotalComputeNodes() + totalComputeNodes);
	}

	/**
	 * @return the totalFreeComputeNodes
	 */
	public int getAvailableComputeNodes() {
		return availableComputeNodes;
	}

	/**
	 * @param totalFreeComputeNodes the totalFreeComputeNodes to set
	 */
	public void setAvailableComputeNodes(int availableComputeNodes) {
		this.availableComputeNodes = availableComputeNodes;
	}

	public void addAvailableComputeNodes(int availableComputeNodes) {
		setAvailableComputeNodes(getAvailableComputeNodes() + availableComputeNodes);
	}


	/**
	 * @param disabledhosts the disabledhosts to set
	 */
	public void setDisabledHosts(SortedMap<String, Host> disabledhosts) {
		this.disabledhosts = disabledhosts;
	}

	public void addDisabledHosts(SortedMap<String, Host> disabledhosts) {
		this.disabledhosts.putAll(disabledhosts);
	}

	/**
	 * @return the totalNumberVisualNodes
	 */
	public int getTotalVisualNodes() {
		return totalVisualNodes;
	}

	/**
	 * @param totalNumberVisualNodes the totalNumberVisualNodes to set
	 */
	public void setTotalVisualNodes(int totalVisualNodes) {
		this.totalVisualNodes = totalVisualNodes;
	}

	public void addTotalVisualNodes(int totalVisualNodes) {
		setTotalVisualNodes(getTotalVisualNodes() + totalVisualNodes);
	}

	/**
	 * @return the totalFreeVisualNodes
	 */
	public int getAvailalableVisualNodes() {
		return availalableVisualNodes;
	}

	/**
	 * @param totalFreeVisualNodes the totalFreeVisualNodes to set
	 */
	public void setAvailalableVisualNodes(int availalableVisualNodes) {
		this.availalableVisualNodes = availalableVisualNodes;
	}

	public void addTotalFreeVisualNodes(int availalableVisualNodes) {
		setAvailalableVisualNodes(getAvailalableVisualNodes() + availalableVisualNodes);
	}

	/**
	 * @return the totalCores
	 */
	public int getTotalCores() {
		return totalCores;
	}

	/**
	 * @param totalCores the totalCores to set
	 */
	public void setTotalCores(int totalCores) {
		this.totalCores = totalCores;
	}

	public void addTotalCores(int totalCores) {
		setTotalCores(getTotalCores() + totalCores);
	}

	/**
	 * @return the availableCores
	 */
	public int getAvailableCores() {
		return availableCores;
	}

	/**
	 * @param availableCores the availableCores to set
	 */
	public void setAvailableCores(int availableCores) {
		this.availableCores = availableCores;
	}

	public void addAvailableCores(int availableCores) {
		setAvailableCores(getAvailableCores() + availableCores);
	}

	/**
	 * @return the totalSession
	 */
	public int getTotalSession() {
		return totalSession;
	}

	/**
	 * @param totalSession the totalSession to set
	 */
	public void setTotalSession(int totalSession) {
		this.totalSession = totalSession;
	}

	public void addTotalSession(int totalSession) {
		setTotalSession(getTotalSession() + totalSession);
	}

	/**
	 * @return the freeSession
	 */
	public int getAvailableSessions() {
		return availableSessions;
	}

	/**
	 * @param freeSession the freeSession to set
	 */
	public void setAvailableSessions(int availableSessions) {
		this.availableSessions = availableSessions;
	}

	public void addAvailableSessions(int availableSessions) {
		setAvailableSessions(getAvailableSessions() + availableSessions);
	}

	/**
	 * @param totalNumberJobs the totalNumberJobs to set
	 */
	public void setTotalNumberJobs(int totalNumberJobs) {
		this.totalNumberJobs = totalNumberJobs;
	}

	public int getTotalNumberJobs() {
		return totalNumberJobs;
	}

	public void addTotalNumberJobs(int totalNumberJobs) {
		this.totalNumberJobs += totalNumberJobs;
	}

	/**
	 * @return the totalMem
	 */
	public double getTotalMem() {
		return totalMem;
	}

	/**
	 * @param totalMem the totalMem to set
	 */
	public void setTotalMem(double totalMem) {
		this.totalMem = totalMem;
	}

	public void addTotalMem(double totalMem) {
		setTotalMem(getTotalMem() + totalMem);
	}

	/**
	 * @return the idleJobs
	 */
	public SortedMap<Integer, Job> getIdleJobs() {
		return idleJobs;
	}

	/**
	 * @param idleJobs the idleJobs to set
	 */
	public void setIdleJobs(SortedMap<Integer, Job> idleJobs) {
		this.idleJobs = idleJobs;
	}

	public void addIdleJobs(SortedMap<Integer, Job> idleJobs) {
		this.idleJobs.putAll(idleJobs);
	}

	public int totalIdleJobs() {
		return idleJobs.size();
	}

	public int getPendingJobsSize() {
		JobMasterQueue jmq = getJobMasterQueue();
		JobsQueue jq = jmq.getQueue(SGE_DataConst.job_PendingQueue);
		return jq.size();
	}

	@Override
	public DetailedInfoProp getDetailedInfoProp() {

		DetailedInfoProp masterDiProp = new DetailedInfoProp();
		masterDiProp.setTitleMetric("Cluster Name: ");
		masterDiProp.setTitleValue(getName());

		DetailedInfoProp coreDiProp = new DetailedInfoProp();
		coreDiProp.setPanelName(getUnitRes());
		coreDiProp.addMetric("Available: ", getAvailableCores());
		coreDiProp.addMetric("Unavailable: ", getSlotUnavailable());
		coreDiProp.addMetric("% Available: ", "");
		coreDiProp.addMetric("Total: ", getTotalCores());
		coreDiProp.addMetric("Reserved: ", "");
		coreDiProp.addMetric("Used: ", "");
		masterDiProp.addDetailedInfoProp(coreDiProp);

		DetailedInfoProp sessionDiProp = new DetailedInfoProp();
		sessionDiProp.setPanelName(SGE_DataConst.unitResSession);
		sessionDiProp.addMetric("Available: ", getAvailableSessions());
		sessionDiProp.addMetric("Total: ", getTotalSession());
		sessionDiProp.addMetric("Used: ", "");
		sessionDiProp.addMetric("Unavailable: ", "");
		masterDiProp.addDetailedInfoProp(sessionDiProp);

		DetailedInfoProp memoryDiProp = new DetailedInfoProp();
		memoryDiProp.setPanelName("Memory");
		memoryDiProp.addMetric("Available Memory: ", decimalFormatter.format(getAvailableMem()));
		memoryDiProp.addMetric("Total Memory: ", decimalFormatter.format(getTotalMem()));
		masterDiProp.addDetailedInfoProp(memoryDiProp);

		DetailedInfoProp nodesDiProp = new DetailedInfoProp();
		nodesDiProp.setPanelName("Node(s");
		nodesDiProp.addMetric("Available Nodes: ", getAvailableComputeNodes());
		nodesDiProp.addMetric("Total Nodes: ", getTotalComputeNodes());
		masterDiProp.addDetailedInfoProp(nodesDiProp);
		
		DetailedInfoProp visualDiProp = new DetailedInfoProp();
		visualDiProp.setPanelName("Visual Node");
		visualDiProp.addMetric("Total Session: ", getTotalSession());
		visualDiProp.addMetric("Available Session: ", getAvailableSessions());
		visualDiProp.addMetric("Total Visual Node: ", getTotalVisualNodes());
		visualDiProp.addMetric("Available Visual Node(s): ", getAvailalableVisualNodes());
		masterDiProp.addDetailedInfoProp(visualDiProp);
		
		DetailedInfoProp jobDiProp = new DetailedInfoProp();
		jobDiProp.setPanelName("Jobs:");
		jobDiProp.addMetric("Total Active Job Count:", getTotalNumberJobs());
		jobDiProp.addMetric("Jobs Pending:", getPendingJobsSize());
		jobDiProp.addMetric("Total Idle Jobs:", totalIdleJobs());
		masterDiProp.addDetailedInfoProp(jobDiProp);

		displayDisabledHosts(masterDiProp);

		return masterDiProp;
	}

	
}
