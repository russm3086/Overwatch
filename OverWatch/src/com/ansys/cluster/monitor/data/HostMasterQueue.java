/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.russ.test.DetailedInfoProp;

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
	private int totalSize = 0;

	public HostMasterQueue(String name) {
		super(name);
		setMembersType(SGE_DataConst.clusterTypeQueue);
	}

	public void addQueue(HostQueue queue) {

		// super.calcQueue(queue);

		logger.finest("Adding " + queue + " to queue " + getName());
		hostQueue.put(queue.getName(), queue);
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

	public void recalc() {

		recalc(hostQueue);

	}

	public void recalc(SortedMap<String, ?> map) {

		for (Entry<String, ?> entry : map.entrySet()) {

			logger.finer("Processing " + entry.getKey());

			AnsQueueAbstract queue = (AnsQueueAbstract) entry.getValue();
			calcQueue(queue);
			addTotalSize(queue.size());
			addAvailableNodes(queue.getAvailableNodes());

			if (queue.getMembersType().equalsIgnoreCase(SGE_DataConst.clusterTypeHost)) {

				HostQueue hq = (HostQueue) queue;
				addDisabledNodes(hq.getDisabledNodes());
			}
		}
	}

	protected void addDisabledNodes(SortedMap<String, Host> map) {
		getDisabledNodes().putAll(map);
	}

	/**
	 * @return the totalSize
	 */
	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize the totalSize to set
	 */
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @param totalSize the totalSize to set
	 */
	public void addTotalSize(int totalSize) {
		setTotalSize(getTotalSize() + totalSize);
	}

	public DetailedInfoProp getDetailedInfoProp() {

		DetailedInfoProp masterDiProp = new DetailedInfoProp();
		masterDiProp.setTitleMetric("Queue Name:");
		masterDiProp.setTitleValue(getName());

		DetailedInfoProp coreDiProp = new DetailedInfoProp();
		coreDiProp.setPanelName(getUnitRes());
		coreDiProp.addMetric("Available: ", getSlotAvailable());
		coreDiProp.addMetric("Unavailable: ", getSlotUnavailable());
		coreDiProp.addMetric("% Available: ", availableSlotsPercent());
		coreDiProp.addMetric("Total: ", getSlotTotal());
		coreDiProp.addMetric("Reserved: ", getSlotRes());
		coreDiProp.addMetric("Used: ", getSlotUsed());
		masterDiProp.addDetailedInfoProp(coreDiProp);

		DetailedInfoProp sessionDiProp = new DetailedInfoProp();
		sessionDiProp.setPanelName(SGE_DataConst.unitResSession);
		sessionDiProp.addMetric("Available: ", getSessionAvailable());
		sessionDiProp.addMetric("Total: ", getSessionTotal());
		sessionDiProp.addMetric("Used: ", getSessionUsed());
		sessionDiProp.addMetric("Unavailable: ", getSessionUnavailable());
		masterDiProp.addDetailedInfoProp(sessionDiProp);

		DetailedInfoProp memoryDiProp = new DetailedInfoProp();
		memoryDiProp.setPanelName("Memory");
		memoryDiProp.addMetric("Available Memory: ", decimalFormatter.format(getFreeMem()));
		memoryDiProp.addMetric("Total Memory: ", decimalFormatter.format(getTotalMem()));
		masterDiProp.addDetailedInfoProp(memoryDiProp);

		DetailedInfoProp nodesDiProp = new DetailedInfoProp();
		nodesDiProp.setPanelName("Node(s");
		nodesDiProp.addMetric("Available Nodes: ", getAvailableNodes());
		nodesDiProp.addMetric("Total Nodes: ", getTotalSize());
		masterDiProp.addDetailedInfoProp(nodesDiProp);

		displayDisabledHosts(masterDiProp);

		return masterDiProp;
	}

	public String getSummary() {
		StringBuilder summary = new StringBuilder();
		summary.append(outputFormatter("Queue Name:", getName()));
		summary.append("\n");

		summary.append(SGE_DataConst.unitResSession);
		summary.append("\n");
		summary.append(outputFormatter("\tTotal:", getSessionTotal()));
		summary.append(outputFormatter("\tUsed:", getSessionUsed()));
		summary.append(outputFormatter("\tAvailable:", getSessionAvailable()));
		summary.append(outputFormatter("\tUnavailable:", getSessionUnavailable()));

		summary.append(getUnitRes());
		summary.append("\n");

		summary.append(outputFormatter("\tTotal:", getSlotTotal()));
		summary.append(outputFormatter("\tReserved:", getSlotRes()));
		summary.append(outputFormatter("\tUsed:", getSlotUsed()));
		summary.append(outputFormatter("\tAvailable:", getSlotAvailable()));
		summary.append(outputFormatter("\tUnavailable:", getSlotUnavailable()));
		summary.append(outputFormatter("\t% Available:", availableSlotsPercent()));

		summary.append(outputFormatter("Memory free:", decimalFormatter.format(getFreeMem())));
		summary.append(outputFormatter("Free Nodes:", getAvailableNodes()));
		summary.append(outputFormatter("Total Nodes:", getTotalSize()));

		return summary.toString();

	}

	protected void printDisabledNodes(DetailedInfoProp masterDiProp) {

		if (getDisabledNodes().size() > 0) {

			DetailedInfoProp disabledNodesDiProp = new DetailedInfoProp();
			StringBuilder sb = new StringBuilder("Disabled Node(s): ");
			sb.append(getDisabledNodes().size());

			disabledNodesDiProp.setPanelName(sb.toString());

			int i = 1;
			for (String hostName : getDisabledNodes().keySet()) {
				String strHost = "Host(" + i + "): ";
				disabledNodesDiProp.addMetric(strHost, hostName);
				i += 1;
			}

			masterDiProp.addDetailedInfoProp(disabledNodesDiProp);
		}
	}


	
}
