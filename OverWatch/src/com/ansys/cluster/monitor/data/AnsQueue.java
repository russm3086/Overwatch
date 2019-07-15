/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.swing.JTable;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueInterface;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.ansys.cluster.monitor.data.state.AnsQueueState;
import com.ansys.cluster.monitor.data.state.HostState;

/**
 * 
 * @author rmartine
 * @since
 */
public class AnsQueue extends ClusterNodeAbstract implements AnsQueueInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = -364220974815470506L;

	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);
	private double total_np_load = 0;
	private double np_load = 0;
	private int nodesAvailable = 0;
	private double freeMem = 0;
	private double totalMem = 0;
	private String membersType;
	private JTable table;
	private SortedMap<Integer, Job> pendingJobs = new TreeMap<Integer, Job>();
	private SortedMap<Integer, Job> activeJobs = new TreeMap<Integer, Job>();
	private SortedMap<String, ClusterNodeAbstract> nodes = new TreeMap<String, ClusterNodeAbstract>();
	private SortedMap<String, Host> disabledNodes = new TreeMap<String, Host>();

	public AnsQueue(ClusterNodeAbstract node) {
		// TODO Auto-generated constructor stub
		addNode(node);
		setup();
	}

	public AnsQueue(ClusterNodeAbstract node, String name) {
		// TODO Auto-generated constructor stub
		this(node);
		setName(name);
	}

	public AnsQueue(String name) {
		// TODO Auto-generated constructor stub
		setName(name);
		setup();
	}

	public void addAvailableNodes() {
		nodesAvailable += 1;
	}

	public void addFreeMem(double freeMem) {
		setFreeMem(getFreeMem() + freeMem);
	}

	private void addHost(ClusterNodeAbstract node) {
		logger.entering(sourceClass, "addHost", node);

		Host host = (Host) node;

		logger.finest("Adding " + host + " to queue " + getQueueName());

		nodes.put(host.getName(), host);

		if (!node.getNodeProp().getQueueName().equalsIgnoreCase(SGE_DataConst.noNameHostQueue)) {

			logger.finer("Adding slots for " + host.getName());
			addSlots(host);

			logger.finer("Processng load for " + host.getName());
			addNP_Load(host);

			logger.finer("Processng free memory for " + host.getName());
			addMemory(host);

			logger.finer("Processing for disabled host: " + host.getName());
			addDisabledHost(host);

			logger.finer("Processing for job list on host: " + host.getName());
			addActiveJobs(host.getListJob());
		}

		logger.exiting(sourceClass, "addHost");
	}

	private void addActiveJobs(ArrayList<Job> listJobs) {
		logger.entering(sourceClass, "addActiveJobs", listJobs);
		for (Job job : listJobs) {
			addActiveJobs(job);
		}
		logger.exiting(sourceClass, "addActiveJobs", listJobs);
	}

	private void addDisabledHost(Host host) {
		logger.entering(sourceClass, "addDisabledHost", host);
		StateAbstract nodeState = host.getState();

		if (nodeState.between(HostState.Unknown, HostState.Error)) {
			disabledNodes.put(host.getName(), host);
		}
		logger.exiting(sourceClass, "addDisabledHost");
	}

	private void addJob(ClusterNodeAbstract node) {
		logger.entering(sourceClass, "addJob", node);

		Job job = (Job) node;

		nodes.put(String.valueOf(job.getJobNumber()), job);

		addSlotTotal(job.getNodeProp().getSlots());

		logger.finest("Adding " + job + " to queue " + getQueueName());
	}

	public void addMemory(ClusterNodeAbstract node) {
		logger.entering(sourceClass, "addMemory", node);
		if (node.isNodeAvailable()) {
			addFreeMem(node.getNodeProp().getMemFreeNum());
		}
		addTotalMem(node.getNodeProp().getMemTotalNum());

		logger.exiting(sourceClass, "addMemory");
	}

	public void addNode(ClusterNodeAbstract node) {
		logger.entering(sourceClass, "addNode", node);

		if (getName() == null) {
			setName(node.getNodeProp().getQueueName());
		}

		switch (node.getClusterType()) {

		case SGE_DataConst.clusterTypeJob:
			setMembersType(SGE_DataConst.clusterTypeJob);
			addJob(node);
			break;

		case SGE_DataConst.clusterTypeHost:
			setMembersType(SGE_DataConst.clusterTypeHost);
			addHost((Host) node);
			break;

		case SGE_DataConst.clusterTypeQueue:
			setMembersType(SGE_DataConst.clusterTypeQueue);
			addQueue((AnsQueue) node);
			break;

		}

		logger.exiting(sourceClass, "addNode");
	}

	public void addNp_load(double np_load) {

		setNp_Load(getNp_Load() + np_load);
		this.total_np_load += np_load;
	}

	public void addNP_Load(ClusterNodeAbstract node) {

		addNp_load(node.getNodeProp().getNp_load_avg());
		calc_NP_Load();
	}

	private void addQueue(AnsQueue queue) {

		switch (queue.getMembersType()) {
		case SGE_DataConst.clusterTypeJob:
			addSlotTotal(queue.getSlotTotal());
			break;

		case SGE_DataConst.clusterTypeHost:
			addSlotTotal(queue.getSlotTotal());
			addSlotRes(queue.getSlotRes());
			addSlotUsed(queue.getSlotUsed());
			addNp_load(queue.getNp_Load());
			addFreeMem(queue.getFreeMem());

			break;
		}

		logger.finest("Adding " + queue + " to queue " + this);
		nodes.put(queue.getName(), queue);
	}

	/**
	 * @param freeSlots the freeSlots to set
	 */
	public void addSlotAvailable(int slotAvailable) {
		this.slotAvailable += slotAvailable;
	}

	/**
	 * @param slotRes the slotRes to set
	 */
	public void addSlotRes(int slotRes) {
		setSlotRes(getSlotRes() + slotRes);
	}

	public void addSlots(ClusterNodeAbstract node) {

		if (node.isNodeAvailable()) {
			addSlotAvailable(node.getNodeProp().getSlotTotal());
			addAvailableNodes();
		}

		logger.fine("Queue: " + getName() + " Node: " + node.getName() + " is avaialble: " + node.isNodeAvailable());

		addSlotTotal(node.getNodeProp().getSlotTotal());
		addSlotRes(node.getNodeProp().getSlotReserved());
		addSlotUsed(node.getNodeProp().getSlotUsed());
	}

	/**
	 * @param slotTotal the slotTotal to set
	 */
	public void addSlotTotal(int slotTotal) {
		setSlotTotal(getSlotTotal() + slotTotal);
	}

	/**
	 * @param slotUsed the slotUsed to set
	 */
	public void addSlotUsed(int slotUsed) {
		setSlotUsed(getSlotUsed() + slotUsed);
	}

	@Override
	public void addState(StateAbstract state) {
		addState(state, AnsQueueState.Normal);
	}

	public void addTotalMem(double totalMem) {
		setTotalMem(getTotalMem() + totalMem);
	}

	public void calc_NP_Load() {

		this.np_load = total_np_load / nodes.size();
	}

	public boolean containsKey(String queueName) {
		// TODO Auto-generated method stub
		return nodes.containsKey(queueName);
	}

	public AnsQueue get(String queueName) {
		// TODO Auto-generated method stub
		return (AnsQueue) nodes.get(queueName);
	}

	public int getAvailableNodes() {
		return nodesAvailable;
	}

	public double getFreeMem() {
		return freeMem;
	}

	public SortedMap<String, ClusterNodeAbstract> getHosts() {
		// TODO Auto-generated method stub
		return getNodes(SGE_DataConst.clusterTypeHost);
	}

	public SortedMap<String, ClusterNodeAbstract> getJobs() {
		// TODO Auto-generated method stub
		return getNodes(SGE_DataConst.clusterTypeJob);
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	public String getMembersType() {
		return membersType;
	}

	@Override
	public NodeProp getNodeProp() {
		// TODO Auto-generated method stub
		return null;
	}

	public SortedMap<String, ClusterNodeAbstract> getNodes() {
		// TODO Auto-generated method stub
		return nodes;
	}

	public SortedMap<String, ClusterNodeAbstract> getNodes(String ClusterType) {
		// TODO Auto-generated method stub

		SortedMap<String, ClusterNodeAbstract> objects = new TreeMap<String, ClusterNodeAbstract>();

		for (Entry<String, ClusterNodeAbstract> node : nodes.entrySet()) {

			if (node.getValue().getClusterType().equalsIgnoreCase(ClusterType)) {
				objects.put(node.getKey(), node.getValue());
			}
		}

		return objects;
	}

	/**
	 * @return the nodesAvailable
	 */
	public int getNodesAvailable() {
		return nodesAvailable;
	}

	/**
	 * @return the np_load
	 */
	public double getNp_Load() {
		return np_load;
	}

	@Override
	public String getStatus() {
		// TODO Add null check to handle HOST and JOB queue
		String status = "";
		if (getMembersType() == null)
			return status;
		if (getMembersType().equalsIgnoreCase(SGE_DataConst.clusterTypeHost)) {
			status = "Free Nodes: " + getAvailableNodes() + "  Free Slots: " + getSlotAvailable() + "  Load "
					+ decimalFormatter.format(getNp_Load());
		} else {

			status = "# of jobs: " + nodes.size() + "  Used Slots: " + getSlotUsed();
		}

		return status;
	}

	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		StringBuilder summary = new StringBuilder();
		summary.append("Queue Name: \t" + getName() + "\n");

		if (getMembersType().equalsIgnoreCase(SGE_DataConst.clusterTypeHost)) {
			summary.append("\n Cores Total: \t" + getSlotTotal());
			summary.append("\n Cores Reserved: \t" + getSlotRes());
			summary.append("\n Cores Used: \t" + getSlotUsed());
			summary.append("\n Cores Available:\t" + getSlotAvailable());
			summary.append("\n Cores % Available: \t" + availableSlotsPercent());
			summary.append("\n Memory free: \t " + decimalFormatter.format(getFreeMem()));
			summary.append("\n Free Nodes: \t" + getAvailableNodes());
			summary.append("\n Total Nodes: \t" + size() + "\n");

			if (pendingJobs.size() > 0) {
				summary.append("\n Pending Job(s):\n" + displayPendingJobs());
			}

			if (activeJobs.size() > 0) {
				summary.append("\n Active Job(s):\n" + displayActiveJobs());
			}

			if (disabledNodes.size() > 0) {
				summary.append("\n Disabled Nodes: \n" + displayDisabledHosts());
			}
		} else if (getMembersType().equalsIgnoreCase(SGE_DataConst.clusterTypeJob)) {

			summary.append("\n# of Jobs: \t" + nodes.size());
		} else if (getMembersType().equalsIgnoreCase(SGE_DataConst.clusterTypeQueue)) {

			for (Entry<String, ClusterNodeAbstract> entry : nodes.entrySet()) {

				AnsQueue queue = (AnsQueue) entry.getValue();

				summary.append("\n" + entry.getKey());
				summary.append("\n\t" + entry.getValue().availableSlotsPercent() + " Cores available");

				if (queue.getDisabledNode().size() > 0) {
					summary.append("\n\t Disabled Nodes: " + queue.getDisabledNode().size());
				}
			}
		}

		return summary.toString();
	}

	/**
	 * @return the total_np_load
	 */
	public double getTotal_np_load() {
		return total_np_load;
	}

	public double getTotalMem() {
		return totalMem;
	}

	public void put(String queueName, AnsQueue queue) {
		// TODO Auto-generated method stub
		nodes.put(queueName, queue);
	}

	/**
	 * @param freeMem the freeMem to set
	 */
	public void setFreeMem(double freeMem) {
		this.freeMem = freeMem;
	}

	/**
	 * @param hosts the hosts to set
	 */
	public void setHosts(SortedMap<String, ClusterNodeAbstract> nodes) {
		this.nodes = nodes;
	}

	public void setMembersType(String membersType) {
		if (this.membersType == null)
			this.membersType = membersType;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(SortedMap<String, ClusterNodeAbstract> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @param nodesAvailable the nodesAvailable to set
	 */
	public void setNodesAvailable(int nodesAvailable) {
		this.nodesAvailable = nodesAvailable;
	}

	/**
	 * @param np_load the np_load to set
	 */
	public void setNp_Load(double np_load) {
		this.np_load = np_load;
	}

	/**
	 * @param total_np_load the total_np_load to set
	 */
	public void setTotal_np_load(double total_np_load) {
		this.total_np_load = total_np_load;
	}

	/**
	 * @param totalMem the totalMem to set
	 */
	public void setTotalMem(double totalMem) {
		this.totalMem = totalMem;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	private void setup() {
		setClusterType(SGE_DataConst.clusterTypeQueue);
		addState(AnsQueueState.Normal);
	}

	public int size() {
		return nodes.size();
	}

	public String getQueueName() {
		return getName();
	}

	public String displayDisabledHosts() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Host> entry : disabledNodes.entrySet()) {
			StringBuilder summary = new StringBuilder();
			Host host = entry.getValue();
			summary.append("  Host: " + host.getName());
			summary.append("\tState: " + host.getStateNames());
			summary.append("\n");
			sb.append(summary);
		}
		return sb.toString();
	}

	public String displayPendingJobs() {
		return displayJobs(getPendingJobs());
	}

	public String displayActiveJobs() {
		return displayJobs(getActiveJobs());
	}

	public String displayJobs(SortedMap<Integer, Job> jobs) {
		StringBuilder sb = new StringBuilder();
		for (Entry<Integer, Job> entry : jobs.entrySet()) {
			StringBuilder summary = new StringBuilder();
			Job job = entry.getValue();
			NodeProp prop = job.getNodeProp();
			summary.append("  Job Name: " + prop.getJobName());
			summary.append("\tOwner: " + prop.getJobOwner());
			summary.append("\tJob #: " + job.getJobNumber());
			summary.append("\tCores: " + prop.getSlots());
			summary.append("\n");
			sb.append(summary);
		}
		return sb.toString();
	}

	public SortedMap<Integer, Job> getPendingJobs() {
		return pendingJobs;
	}

	public void addPendingJobs(Job pendingJob) {
		this.pendingJobs.put(pendingJob.getJobNumber(), pendingJob);
	}

	/**
	 * @return the activeJobs
	 */
	public SortedMap<Integer, Job> getActiveJobs() {
		return activeJobs;
	}

	/**
	 * @param activeJobs the activeJobs to set
	 */
	public void addActiveJobs(Job activeJob) {
		this.activeJobs.put(activeJob.getJobNumber(), activeJob);
	}

	/**
	 * @return the disabledNode
	 */
	public SortedMap<String, Host> getDisabledNode() {
		return disabledNodes;
	}

	/**
	 * @param disabledNode the disabledNode to set
	 */
	public void setDisabledNodes(SortedMap<String, Host> disabledNodes) {
		this.disabledNodes = disabledNodes;
	}

	public String toString() {
		return getName();
	}

}
