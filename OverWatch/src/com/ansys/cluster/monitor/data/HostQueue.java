/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.ansys.cluster.monitor.data.state.HostState;
import com.ansys.cluster.monitor.data.state.JobState;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoProp;

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

			if (host.isVisualNode()) {

				if (!addUnavailbleHost(host, getUnavailableVisualHosts())) {

					addAvailableVisualHosts(host.getName(), host);
				}
			} else {

				if (!addUnavailbleHost(host, getUnavailableComputeHosts())) {

					addAvailableComputeHosts(host.getName(), host);

					if (host.getFUN_Cores() > 0) {
						addFullyUnallocatedComputeHosts(host);
						addCoreFUN(host.getFUN_Cores());
					}
				}
			}

			logger.finer("Adding slots for " + host.getName());
			addResources(host);

			logger.finer("Processng load for " + host.getName());
			addNP_Load(host);

			logger.finer("Processng free memory for " + host.getName());
			addMemory(host);

			logger.finer("Processing for job list on host: " + host.getName());
			addActiveJobs(host.getListActiveJob());
		}

		logger.exiting(sourceClass, "addHost");
	}

	private void addActiveJobs(ArrayList<Job> listJobs) {
		logger.entering(sourceClass, "addActiveJobs", listJobs);
		for (Job job : listJobs) {
			addActiveJobs(job.getJobNumber(), job);
		}
		logger.exiting(sourceClass, "addActiveJobs", listJobs);
	}

	private boolean addUnavailbleHost(Host host, SortedMap<String, Host> map) {
		logger.entering(sourceClass, "addDisabledHost", host);
		boolean result = false;
		StateAbstract nodeState = host.getState();

		if ((nodeState.between(HostState.Unknown, HostState.Error))
				|| (nodeState.between(HostState.Suspended, HostState.DisabledManually))) {
			map.put(host.getName(), host);
			result = true;
		}
		logger.exiting(sourceClass, "addDisabledHost");
		return result;
	}

	public void addNP_Load(Host host) {
		int totalNum = getAvailableComputeHostsSize() + getAvailableVisualHostsSize();
		double avg = ((getNp_Load() * (totalNum)) + host.getNp_load_avg()) / (totalNum + 1);

		setNp_Load(avg);
	}

	public void addMemory(Host host) {
		logger.entering(sourceClass, "addMemory", host);
		if (host.isNodeAvailable()) {
			addAvailableMem(host.getMemFreeNum());
		}
		addTotalMem(host.getNodeProp().getMemTotalNum());

		logger.exiting(sourceClass, "addMemory");
	}

	public void addResources(Host host) {
		if (host.isVisualNode()) {
			addSessions(host);
		} else {
			addCores(host);
		}

		addMemory(host);
	}

	public void processActiveListJob() {
		for (Iterator<Map.Entry<Integer, Job>> it = getActiveJobs().entrySet().iterator(); it.hasNext();) {

			Map.Entry<Integer, Job> entry = it.next();
			if (entry.getValue().getState().equals(JobState.Idle)) {
				addIdleJobs(entry.getKey(), entry.getValue());
				it.remove();
			}
		}
	}

	public void addCores(Host host) {

		if (host.isNodeAvailable()) {
			addCoreAvailable(host.getSlotUnused());
		}

		logger.fine("Queue: " + getName() + " Node: " + host.getName() + " is avaialble: " + host.isNodeAvailable());

		addCoreTotal(host.getSlotTotal());
		addCoreReserved(host.getSlotReserved());
		addCoreUsed(host.getSlotUsed());
		addCoreUnavailable(host.getSlotUnavailable());
	}

	public void addSessions(Host host) {
		if (host.isNodeAvailable()) {
			addSessionAvailable(host.getSlotUnused());
		}

		addSessionTotal(host.getSlotTotal());
		addSessionUsed(host.getSlotUsed());
		addSessionUnavailable(host.getSlotUnavailable());
	}

	public DetailedInfoProp getDetailedInfoProp() {
		DetailedInfoProp mainDiProp = new DetailedInfoProp();
		mainDiProp.setTitleMetric("Queue Name: ");
		mainDiProp.setTitleValue(getName());

		DetailedInfoProp resourceDiProp = new DetailedInfoProp();

		resourceDiProp.setPanelName(getUnitRes());
		if (isVisualNode()) {

			resourceDiProp.addMetric("Available: ", getSessionAvailable());
			resourceDiProp.addMetric("Unavailable: ", getSessionUnavailable());
			resourceDiProp.addMetric("Total: ", getSessionTotal());
		} else {

			resourceDiProp.addMetric("Available: ", getCoreAvailable());
			resourceDiProp.addMetric("Unavailable: ", getCoreUnavailable());
			resourceDiProp.addMetric("Total: ", getCoreTotal());
			resourceDiProp.addMetric("Reserved: ", getCoreReserved());
			resourceDiProp.addMetric("F.U.N. Core(s):", getCoreFUN());
			resourceDiProp.addMetric("Core(s) / Node: ", getCoreTotal()/size());
		}

		resourceDiProp.addMetric("Load: ", numberFormmatter.format(getNp_Load()));
		mainDiProp.addDetailedInfoProp(resourceDiProp);

		DetailedInfoProp memDiProp = new DetailedInfoProp();
		memDiProp.setPanelName("Memory");
		memDiProp.addMetric("Available Memory: ", numberFormmatter.format(getAvailableMem()));
		memDiProp.addMetric("Total Memory: ", numberFormmatter.format(getTotalMem()));
		mainDiProp.addDetailedInfoProp(memDiProp);

		DetailedInfoProp nodeDiProp = new DetailedInfoProp();

		int unavailbleNode = 0;
		int available = 0;

		if (isVisualNode()) {

			unavailbleNode = getUnavailableVisualHostsSize();
			available = getAvailableVisualHostsSize();
		} else {
			unavailbleNode = getUnavailableComputeHostsSize();
			available = getAvailableComputeHostsSize();
		}
		nodeDiProp.setPanelName("Nodes");
		nodeDiProp.addMetric("Total Host: ", unavailbleNode + available);
		nodeDiProp.addMetric("Total Unavailable Host: ", unavailbleNode);
		nodeDiProp.addMetric("Total Available Host: ", available);
		//mainDiProp.addDetailedInfoProp(nodeDiProp);

		displayPendingJobs(mainDiProp);
		displayActiveJobs(mainDiProp);
		displayIdleJobs(mainDiProp);
		displayUnavailableVisualHosts(mainDiProp);
		displayUnavailableComputeHosts(mainDiProp);
		displayFullyUnallocatedNodes(mainDiProp);

		return mainDiProp;
	}

	public SortedMap<String, Host> getHosts() {
		return getAllmaps();
	}

	// TODO SHould be part of an interface
	@Override
	public int size() {
		return getAllmaps().size();
	}

	@Override
	public boolean containsKey(String key) {
		// TODO Auto-generated method stub
		return getAllmaps().containsKey(key);
	}

	public Host get(String host) {
		// TODO Auto-generated method stub
		return getAllmaps().get(host);
	}

	public SortedMap<Object, ClusterNodeAbstract> getNodes() {
		// TODO Auto-generated method stub

		SortedMap<Object, ClusterNodeAbstract> map = new TreeMap<Object, ClusterNodeAbstract>();
		map.putAll(getAllmaps());

		return map;
	}

	public SortedMap<String, Host> getAllmaps() {
		SortedMap<String, Host> map = new TreeMap<String, Host>();
		if (isVisualNode()) {

			map.putAll(getAvailableVisualHosts());
			map.putAll(getUnavailableVisualHosts());
		} else {

			map.putAll(getAvailableComputeHosts());
			map.putAll(getUnavailableComputeHosts());
		}

		return map;
	}

	@Override
	public String getToolTip() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();

		if (isVisualNode()) {

			sb.append(" Available Session(s): ");
			sb.append(getSessionAvailable());
			sb.append(" Available Nodes: ");
			sb.append(getUnavailableVisualHostsSize());
		} else {

			sb.append(" Available Core(s): ");
			sb.append(getCoreAvailable());
			sb.append(" Available Nodes: ");
			sb.append(getAvailableComputeHostsSize());
		}

		sb.append(" Available Memory: ");
		sb.append(numberFormmatter.format(getAvailableMem()));
		return sb.toString();
	}

}
