/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.ansys.cluster.monitor.data.state.HostState;
import com.russ.test.DetailedInfoProp;

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

		if (!host.getQueueName().equalsIgnoreCase(SGE_DataConst.noNameHostQueue)) {

			hosts.put(host.getName(), host);

			logger.finer("Adding slots for " + host.getName());
			addResources(host);

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

	public void addResources(Host host) {
		if (host.isVisualNode()) {
			addSessions(host);
		} else {
			addSlots(host);
		}
	}

	public void addSlots(Host host) {

		if (host.isNodeAvailable()) {
			addSlotAvailable(host.getSlotUnused());
			addAvailableNodes();
		}

		logger.fine("Queue: " + getName() + " Node: " + host.getName() + " is avaialble: " + host.isNodeAvailable());

		addSlotTotal(host.getSlotTotal());
		addSlotRes(host.getSlotReserved());
		addSlotUsed(host.getSlotUsed());
		addSlotUnavailable(host.getSlotUnavailable());
	}

	public void addSessions(Host host) {
		if (host.isNodeAvailable()) {
			addAvailableNodes();
			addSessionAvailable(host.getSlotUnused());
		}

		addSessionTotal(host.getSlotTotal());
		addSessionUsed(host.getSlotUsed());
		addSessionUnavailable(host.getSlotUnavailable());
	}

	public void addPendingJobs(Job pendingJob) {
		this.pendingJobs.put(pendingJob.getJobNumber(), pendingJob);
	}


	/**
	 * @param activeJobs the activeJobs to set
	 */
	public void addActiveJobs(Job activeJob) {
		this.activeJobs.put(activeJob.getJobNumber(), activeJob);
	}

	/**
	 * @param disabledNode the disabledNode to set
	 */
	public void setDisabledNodes(SortedMap<String, Host> disabledhosts) {
		this.disabledhosts = disabledhosts;
	}


	public DetailedInfoProp getDetailedInfoProp() {
		DetailedInfoProp mainDiProp = new DetailedInfoProp();
		mainDiProp.setTitleMetric("Queue Name:");
		mainDiProp.setTitleValue(getName());

		DetailedInfoProp resourceDiProp = new DetailedInfoProp();

		if (isVisualNode()) {

			resourceDiProp.setPanelName("Session(s)");
			resourceDiProp.addMetric(summaryOutput(getUnitRes(), "Available: "), getSessionAvailable());
			resourceDiProp.addMetric(summaryOutput(getUnitRes(), "Unavailable: "), getSessionUnavailable());
			resourceDiProp.addMetric(summaryOutput(getUnitRes(), "Total: "), getSessionTotal());

		} else {

			resourceDiProp.setPanelName("Core(s)");
			resourceDiProp.addMetric(summaryOutput(getUnitRes(), "Available: "), getSlotAvailable());
			resourceDiProp.addMetric(summaryOutput(getUnitRes(), "Unavailable: "), getSlotUnavailable());
			resourceDiProp.addMetric(summaryOutput(getUnitRes(), "Total: "), getSlotTotal());
			resourceDiProp.addMetric(summaryOutput(getUnitRes(), "Reserved: "), getSlotRes());
			resourceDiProp.addMetric(summaryOutput(getUnitRes(), "% Available: "), availableSlotsPercent());

		}

		mainDiProp.addDetailedInfoProp(resourceDiProp);

		DetailedInfoProp memDiProp = new DetailedInfoProp();
		memDiProp.setPanelName("Memory");
		memDiProp.addMetric("Available Memory: ", decimalFormatter.format(getFreeMem()));
		memDiProp.addMetric("Total Memory: ", decimalFormatter.format(getTotalMem()));
		mainDiProp.addDetailedInfoProp(memDiProp);

		displayPendingJobs(mainDiProp);
		displayActiveJobs(mainDiProp);
		displayDisabledHosts(mainDiProp);
		
		return mainDiProp;
	}


	public String getStatus() {

		String status = "Free Nodes: " + getAvailableNodes() + "  Free " + getUnitRes() + ": " + getSlotAvailable()
				+ "  Load " + decimalFormatter.format(getNp_Load());
		return status;
	}

	public SortedMap<String, Host> getHosts() {
		return hosts;
	}

	// TODO SHould be part of an interface
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

	public SortedMap<Object, ClusterNodeAbstract> getNodes() {
		// TODO Auto-generated method stub

		SortedMap<Object, ClusterNodeAbstract> map = new TreeMap<Object, ClusterNodeAbstract>();
		map.putAll(hosts);

		return map;
	}

}
