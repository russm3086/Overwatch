/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.ansys.cluster.monitor.data.state.HostState;

/**
 * @author rmartine
 *
 */
public class HostQueue extends AnsQueueAbstract {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8728921820872189083L;
	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);
	private SortedMap<String, Host> hosts = new TreeMap<String, Host>();
	private SortedMap<String, Host> disabledhosts = new TreeMap<String, Host>();
	private SortedMap<Integer, Job> activeJobs = new TreeMap<Integer, Job>();
	private SortedMap<Integer, Job> pendingJobs = new TreeMap<Integer, Job>();

	public HostQueue(String name) {
		super(name, SGE_DataConst.clusterTypeHost);
		// TODO Auto-generated constructor stub
	}

	public HostQueue(Host host) {
		super(host);
		addHost(host);
		// TODO Auto-generated constructor stub
	}

	public void addHost(Host host) {
		logger.entering(sourceClass, "addHost", host);

		logger.finest("Adding " + host + " to queue " + getQueueName());

		hosts.put(host.getName(), host);

		if (!host.getQueueName().equalsIgnoreCase(SGE_DataConst.noNameHostQueue)) {

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

		if ((nodeState.between(HostState.Unknown, HostState.Error))
				|| (nodeState.between(HostState.Suspended, HostState.DisabledManually))) {
			disabledhosts.put(host.getName(), host);
		}
		logger.exiting(sourceClass, "addDisabledHost");
	}

	public void addNP_Load(Host host) {
		addNp_load(host.getNp_load_avg());
		calc_NP_Load();
	}

	public void addMemory(Host host) {
		logger.entering(sourceClass, "addMemory", host);
		if (host.isNodeAvailable()) {
			addFreeMem(host.getMemFreeNum());
		}
		addTotalMem(host.getNodeProp().getMemTotalNum());

		logger.exiting(sourceClass, "addMemory");
	}

	public void calc_NP_Load() {

		np_load = total_np_load / hosts.size();
	}

	public void addSlots(Host host) {

		if (host.isNodeAvailable()) {
			addSlotAvailable(host.getSlotTotal());
			addAvailableNodes();
		}

		logger.fine("Queue: " + getName() + " Node: " + host.getName() + " is avaialble: " + host.isNodeAvailable());

		addSlotTotal(host.getSlotTotal());
		addSlotRes(host.getSlotReserved());
		addSlotUsed(host.getSlotUsed());
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
		return disabledhosts;
	}

	/**
	 * @param disabledNode the disabledNode to set
	 */
	public void setDisabledNodes(SortedMap<String, Host> disabledhosts) {
		this.disabledhosts = disabledhosts;
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

	public String displayDisabledHosts() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Host> entry : disabledhosts.entrySet()) {
			StringBuilder summary = new StringBuilder();
			Host host = entry.getValue();
			summary.append("  Host: " + host.getName());
			summary.append("\tState: " + host.getStateNames());
			summary.append("\n");
			sb.append(summary);
		}
		return sb.toString();
	}

	public String getSummary() {
		StringBuilder summary = new StringBuilder();
		summary.append("Queue Name: \t" + getName() + "\n");

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

		if (disabledhosts.size() > 0) {
			summary.append("\n Disabled Nodes: \n" + displayDisabledHosts());
		}

		return summary.toString();

	}

	public String getStatus() {

		String status = "Free Nodes: " + getAvailableNodes() + "  Free Slots: " + getSlotAvailable() + "  Load "
				+ decimalFormatter.format(getNp_Load());
		return status;
	}

	public SortedMap<String, Host> getHosts() {
		return hosts;
	}

	@Override
	public int size() {
		return hosts.size();
	}

	@Override
	public boolean containsKey(String queueName) {
		// TODO Auto-generated method stub
		return hosts.containsKey(queueName);
	}

	public Host get(String host) {
		// TODO Auto-generated method stub
		return hosts.get(host);
	}
	
	public SortedMap<String, ClusterNodeAbstract> getNodes() {
		SortedMap<String, ClusterNodeAbstract> map = new TreeMap<String, ClusterNodeAbstract>();
		map.putAll(hosts);
		return map;
	}

}
