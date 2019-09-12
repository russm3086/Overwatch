/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoProp;

/**
 * @author rmartine
 *
 */
public class HostMasterQueue extends HostQueue implements MasterQueue {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1839423343249204746L;
	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);
	private SortedMap<String, HostQueue> hostQueue = new TreeMap<String, HostQueue>();
	private int availableComputeHostsCount = 0;
	private int unAvailableComputeHostsCount = 0;
	private int availableVisualHostsCount = 0;
	private int unAvailableVisualHostsCount = 0;
	private int fullyUnallocatedComputeHostsCount = 0;
	private int fullyUnallocatedComputeHostsCore = 0;

	public HostMasterQueue(String name) {
		super(name);
		setMembersType(SGE_DataConst.clusterTypeQueue);
	}

	public void addQueue(HostQueue queue) {

		logger.finest("Adding " + queue + " to queue " + getName());
		hostQueue.put(queue.getName(), queue);
	}

	public void processQueues() {
		for (Entry<String, HostQueue> entry : hostQueue.entrySet()) {
			processQueue(entry.getValue());
		}
	}

	public void processQueue(HostQueue queue) {

		if (queue.isVisualNode()) {

			addAvailableVisualHostsCount(queue.getAvailableVisualHostsSize());
			addUnavailableVisualHostsCount(queue.getUnavailableVisualHostsSize());
		} else {

			addAvailableComputeHostsCount(queue.getAvailableComputeHostsSize());
			addUnavailableComputeHostsCount(queue.getUnavailableComputeHostsSize());
			addFullyUnallocatedComputeHostsCount(queue.getFullyUnallocatedComputeHostsSize());
			addFullyUnallocatedComputeHostsCore(queue.getFullyUnallocatedComputeHosts());
		}

		calculateMetrics(queue);

	}

	public SortedMap<String, HostQueue> getHostQueues() {
		return hostQueue;
	}

	@Override
	public int size() {
		return hostQueue.size();
	}

	@Override
	public boolean containsKey(String queueName) {
		return hostQueue.containsKey(queueName);
	}

	public HostQueue getQueue(String queueName) {
		return hostQueue.get(queueName);
	}

	@Override
	public SortedMap<String, AnsQueueAbstract> getQueues() {
		SortedMap<String, AnsQueueAbstract> map = new TreeMap<String, AnsQueueAbstract>();
		map.putAll(hostQueue);

		return map;
	}

	/**
	 * @return the totalSize
	 */
	public int getTotalCount() {
		return getAvailableComputeHostsCount() + getUnavailableComputeHostsCount() + getAvailableVisualHostsCount()
				+ getUnavailableVisualHostsCount();
	}

	/**
	 * @return the availableComputeHostsCount
	 */
	public int getAvailableComputeHostsCount() {
		return availableComputeHostsCount;
	}

	/**
	 * @param availableComputeHostsCount the availableComputeHostsCount to set
	 */
	public void setAvailableComputeHostsCount(int availableComputeHostsCount) {
		this.availableComputeHostsCount = availableComputeHostsCount;
	}

	public void addAvailableComputeHostsCount(int availableComputeHostsCount) {
		setAvailableComputeHostsCount(getAvailableComputeHostsCount() + availableComputeHostsCount);
	}

	/**
	 * @return the unAvailableComputeHostsCount
	 */
	public int getUnavailableComputeHostsCount() {
		return unAvailableComputeHostsCount;
	}

	/**
	 * @param unAvailableComputeHostsCount the unAvailableComputeHostsCount to set
	 */
	public void setUnavailableComputeHostsCount(int unAvailableComputeHostsCount) {
		this.unAvailableComputeHostsCount = unAvailableComputeHostsCount;
	}

	public void addUnavailableComputeHostsCount(int unAvailableComputeHostsCount) {
		setUnavailableComputeHostsCount(getUnavailableComputeHostsCount() + unAvailableComputeHostsCount);
	}

	/**
	 * @return the availableVisualHostsCount
	 */
	public int getAvailableVisualHostsCount() {
		return availableVisualHostsCount;
	}

	/**
	 * @param availableVisualHostsCount the availableVisualHostsCount to set
	 */
	public void setAvailableVisualHostsCount(int availableVisualHostsCount) {
		this.availableVisualHostsCount = availableVisualHostsCount;
	}

	public void addAvailableVisualHostsCount(int availableVisualHostsCount) {
		setAvailableVisualHostsCount(availableVisualHostsCount + availableVisualHostsCount);
	}

	/**
	 * @return the unAvailableVisualHostsCount
	 */
	public int getUnavailableVisualHostsCount() {
		return unAvailableVisualHostsCount;
	}

	/**
	 * @param unAvailableVisualHostsCount the unAvailableVisualHostsCount to set
	 */
	public void setUnavailableVisualHostsCount(int unAvailableVisualHostsCount) {
		this.unAvailableVisualHostsCount = unAvailableVisualHostsCount;
	}

	public void addFullyUnallocatedComputeHostsCount(int count) {
		setFullyUnallocatedComputeHostsCount(getFullyUnallocatedComputeHostsCount() + count);
	}

	/**
	 * @return the fullyUnallocatedComputeHostsCount
	 */
	public int getFullyUnallocatedComputeHostsCount() {
		return fullyUnallocatedComputeHostsCount;
	}

	public void addUnavailableVisualHostsCount(int unAvailableVisualHostsCount) {
		setUnavailableVisualHostsCount(getUnavailableVisualHostsCount() + unAvailableVisualHostsCount);
	}

	/**
	 * @param fullyUnallocatedComputeHostsCount the
	 *                                          fullyUnallocatedComputeHostsCount to
	 *                                          set
	 */
	public void setFullyUnallocatedComputeHostsCount(int fullyUnallocatedComputeHostsCount) {
		this.fullyUnallocatedComputeHostsCount = fullyUnallocatedComputeHostsCount;
	}

	public void addFullyUnallocatedComputeHostsCore(ArrayList<Host> list) {
		for (Host host :list) {
			addFullyUnallocatedComputeHostsCore(host.getM_Core());
		}
	}

	public void addFullyUnallocatedComputeHostsCore(int cores) {
		setFullyUnallocatedComputeHostsCore(getFullyUnallocatedComputeHostsCore() + cores);
	}

	/**
	 * @return the fullyUnallocatedComputeHostsCore
	 */
	public int getFullyUnallocatedComputeHostsCore() {
		return fullyUnallocatedComputeHostsCore;
	}

	/**
	 * @param fullyUnallocatedComputeHostsCore the fullyUnallocatedComputeHostsCore
	 *                                         to set
	 */
	public void setFullyUnallocatedComputeHostsCore(int fullyUnallocatedComputeHostsCore) {
		this.fullyUnallocatedComputeHostsCore = fullyUnallocatedComputeHostsCore;
	}

	public void calculateMetrics(HostQueue queue) {
		for (Entry<String, Host> entry : queue.getAllmaps().entrySet()) {
			entry.getValue().processActiveListJob();
			addResources(entry.getValue());
		}
		queue.processActiveListJob();
	}

	public SortedMap<String, Host> findUnavailableVisualHosts() {
		SortedMap<String, Host> map = new TreeMap<String, Host>();

		for (Entry<String, HostQueue> entry : getHostQueues().entrySet()) {
			map.putAll(entry.getValue().getUnavailableVisualHosts());
		}
		return map;
	}

	public SortedMap<String, Host> findUnavailableComputeHosts() {
		SortedMap<String, Host> map = new TreeMap<String, Host>();

		for (Entry<String, HostQueue> entry : getHostQueues().entrySet()) {
			map.putAll(entry.getValue().getUnavailableComputeHosts());
		}
		return map;
	}

	public SortedMap<String, Host> findAvailableVisualHosts() {
		SortedMap<String, Host> map = new TreeMap<String, Host>();

		for (Entry<String, HostQueue> entry : getHostQueues().entrySet()) {
			map.putAll(entry.getValue().getAvailableVisualHosts());
		}
		return map;
	}

	public SortedMap<String, Host> findAvailableComputeHosts() {
		SortedMap<String, Host> map = new TreeMap<String, Host>();

		for (Entry<String, HostQueue> entry : getHostQueues().entrySet()) {
			map.putAll(entry.getValue().getAvailableComputeHosts());
		}
		return map;
	}

	public ArrayList<Host> findFUN(){
		ArrayList<Host> list = new ArrayList<Host>();

		for (Entry<String, HostQueue> entry : getHostQueues().entrySet()) {
			list.addAll(entry.getValue().getFullyUnallocatedComputeHosts());
		}
		
		return list;
	}
	
	
	
	public DetailedInfoProp getDetailedInfoProp() {

		DetailedInfoProp masterDiProp = new DetailedInfoProp();
		masterDiProp.setTitleMetric("Queue Name: ");
		masterDiProp.setTitleValue(getName());

		DetailedInfoProp coreDiProp = new DetailedInfoProp();
		coreDiProp.setPanelName(getUnitRes());
		coreDiProp.addMetric("Available: ", getCoreAvailable());
		coreDiProp.addMetric("Unavailable: ", getCoreUnavailable());
		coreDiProp.addMetric("Total: ", getCoreTotal());
		coreDiProp.addMetric("Reserved: ", getCoreReserved());
		coreDiProp.addMetric("Used: ", getCoreUsed());
		masterDiProp.addDetailedInfoProp(coreDiProp);

		DetailedInfoProp sessionDiProp = new DetailedInfoProp();
		sessionDiProp.setPanelName(SGE_DataConst.unitResSession);
		sessionDiProp.addMetric("Available: ", getSessionAvailable());
		sessionDiProp.addMetric("Unavailable: ", getSessionUnavailable());
		sessionDiProp.addMetric("Total: ", getSessionTotal());
		sessionDiProp.addMetric("Used: ", getSessionUsed());
		masterDiProp.addDetailedInfoProp(sessionDiProp);

		DetailedInfoProp memoryDiProp = new DetailedInfoProp();
		memoryDiProp.setPanelName("Memory");
		memoryDiProp.addMetric("Available Memory: ", decimalFormatter.format(getAvailableMem()));
		memoryDiProp.addMetric("Total Memory: ", decimalFormatter.format(getTotalMem()));
		masterDiProp.addDetailedInfoProp(memoryDiProp);

		DetailedInfoProp nodesDiProp = new DetailedInfoProp();
		nodesDiProp.setPanelName("Node(s)");
		nodesDiProp.addMetric("Available Compute Nodes: ", getAvailableComputeHostsCount());
		nodesDiProp.addMetric("Unavailable Compute Nodes: ", getUnavailableComputeHostsCount());
		nodesDiProp.addMetric("Available Visual Nodes: ", getAvailableVisualHostsCount());
		nodesDiProp.addMetric("Unavailable Visual Nodes: ", getUnavailableVisualHostsCount());
		nodesDiProp.addMetric("Total Nodes: ", getTotalCount());
		masterDiProp.addDetailedInfoProp(nodesDiProp);
		
		DetailedInfoProp funDiProp = new DetailedInfoProp();
		funDiProp.setPanelName("FUN (Fully Unallocated Nodes)");
		funDiProp.addMetric("F.U.N.: ", getUnavailableVisualHostsCount());
		funDiProp.addMetric("F.U.N. Cores: ", getFullyUnallocatedComputeHostsCore());
		masterDiProp.addDetailedInfoProp(funDiProp);

		displayFullyUnallocatedNodes(masterDiProp, findFUN());
		displayUnavailableVisualHosts(masterDiProp, findUnavailableVisualHosts());
		displayUnavailableComputeHosts(masterDiProp, findUnavailableComputeHosts());

		return masterDiProp;
	}

}
