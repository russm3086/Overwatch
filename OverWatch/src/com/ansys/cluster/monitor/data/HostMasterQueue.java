/**
 * 
 */
package com.ansys.cluster.monitor.data;

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

	public HostMasterQueue(String name) {
		super(name);
		setMembersType(SGE_DataConst.clusterTypeQueue);
	}

	public void addQueue(HostQueue queue) {

		super.addQueue(queue);

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

}
