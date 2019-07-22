/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.SortedMap;
import java.util.TreeMap;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;


/**
 * @author rmartine
 *
 */
public class JobMasterQueue extends JobsQueue implements MasterQueue {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8532349133666707546L;

	private SortedMap<String, JobsQueue> jobQueues = new TreeMap<String, JobsQueue>();

	public JobMasterQueue(String name) {
		super(name);
		setMembersType(SGE_DataConst.clusterTypeQueue);
	}

	public void addQueue(JobsQueue queue) {

		super.addQueue(queue);
		jobQueues.put(queue.getQueueName(), queue);
	}

	public SortedMap<String, JobsQueue> getJobQueues() {
		return jobQueues;
		
	}
	
	public SortedMap<String, AnsQueueAbstract> getQueues() {
		
		SortedMap<String, AnsQueueAbstract> map = new TreeMap<String, AnsQueueAbstract>();
		map.putAll(jobQueues);
		
		return map;
	}
	
	@Override
	public int size() {
		return jobQueues.size();
	}

	@Override
	public boolean containsKey(String queueName) {
		return jobQueues.containsKey(queueName);
	}

	public JobsQueue getQueue(String queueName) {
		return jobQueues.get(queueName);
	}
}
