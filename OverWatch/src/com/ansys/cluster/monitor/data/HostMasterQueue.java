/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;

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

	protected String printDisabledNodes() {

		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("Disabled Node(s):\t" + getDisabledNodes().size());

		if (getDisabledNodes().size() > 0) {

			sb.append("\n");
			for (String hostName : getDisabledNodes().keySet()) {

				sb.append("\t");
				sb.append(hostName);
			}
		}
		return sb.toString();
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

	public String getSummary() {
		StringBuilder summary = new StringBuilder();
		summary.append("Queue Name: \t");
		summary.append(getName());
		summary.append("\n\n");

		summary.append(SGE_DataConst.unitResSession);
		summary.append("\n");
		summary.append("\tTotal: \t");
		summary.append(getSessionTotal());
		summary.append("\n");

		summary.append("\tUsed: \t");
		summary.append(getSessionUsed());
		summary.append("\n");

		summary.append("\tAvailable:\t");
		summary.append(getSessionAvailable());
		summary.append("\n");

		summary.append("\tUnavailable:\t");
		summary.append(getSessionUnavailable());
		summary.append("\n\n");

		summary.append(getUnitRes());
		summary.append("\n");

		summary.append("\tTotal: \t");
		summary.append(getSlotTotal());
		summary.append("\n");

		summary.append("\tReserved: \t");
		summary.append(getSlotRes());
		summary.append("\n");

		summary.append("\tUsed: \t");
		summary.append(getSlotUsed());
		summary.append("\n");
			
		summary.append("\tAvailable:\t");
		summary.append(getSlotAvailable());
		summary.append("\n");
		
		summary.append("\tUnavailable:\t");
		summary.append(getSlotUnavailable());
		summary.append("\n");
		
		summary.append("\t% Available: \t");
		summary.append(availableSlotsPercent());
		summary.append("\n");
		
		summary.append("\n Memory free: \t\t"  );
		summary.append(decimalFormatter.format(getFreeMem()));
		
		summary.append("\n Free Nodes: \t\t");
		summary.append(getAvailableNodes());
		
		summary.append("\n Total Nodes:\t\t");
		summary.append(getTotalSize());
		
		summary.append("\n");
		summary.append(printDisabledNodes());

		return summary.toString();

	}

}
