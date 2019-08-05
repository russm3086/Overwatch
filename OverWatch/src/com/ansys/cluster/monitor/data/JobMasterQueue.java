/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;

/**
 * @author rmartine
 *
 */
public class JobMasterQueue extends JobsQueue implements MasterQueue {
	/**
	 * 
	 */
	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);
	private static final long serialVersionUID = -8532349133666707546L;
	private int totalSize = 0;
	private int errorJobsCount = 0;
	private int idleJobsCount = 0;

	private SortedMap<String, JobsQueue> jobQueues = new TreeMap<String, JobsQueue>();

	public JobMasterQueue(String name) {
		super(name);
		setMembersType(SGE_DataConst.clusterTypeQueue);
	}

	public void addQueue(JobsQueue queue) {
		super.calcQueue(queue);
		addErrorJobsCount(queue.getErrorJobs().size());
		jobQueues.put(queue.getQueueName(), queue);
		addIdleJobsCount(queue.getIdleJobs().size());
	}

	public SortedMap<String, JobsQueue> getJobQueues() {
		return jobQueues;

	}

	public void recalc() {
		recalc(jobQueues);
	}

	public void recalc(SortedMap<String, ?> map) {

		for (Entry<String, ?> entry : map.entrySet()) {

			logger.finer("Processing " + entry.getKey());

			AnsQueueAbstract queue = (AnsQueueAbstract) entry.getValue();
			calcQueue(queue);
			addTotalSize(queue.size());
			addAvailableNodes(queue.getAvailableNodes());

		}
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

	public String getSummary() {
		StringBuilder summary = new StringBuilder();
		summary.append("Queue Name: \t");
		summary.append(getName());
		summary.append("\n\n");

		summary.append("Total Jobs:\t");
		summary.append(size());
		summary.append("\n");
		
		summary.append("Total session:\t");
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

		return summary.toString();

	}

	/**
	 * @return the errorJobsCount
	 */
	public int getErrorJobsCount() {
		return errorJobsCount;
	}

	/**
	 * @param errorJobsCount the errorJobsCount to set
	 */
	public void setErrorJobsCount(int errorJobsCount) {
		this.errorJobsCount = errorJobsCount;
	}

	/**
	 * @param errorJobsCount the errorJobsCount to set
	 */
	public void addErrorJobsCount(int errorJobsCount) {
		setErrorJobsCount(getErrorJobsCount() + errorJobsCount);
	}

	/**
	 * @return the idleJobsCount
	 */
	public int getIdleJobsCount() {
		return idleJobsCount;
	}

	/**
	 * @param idleJobsCount the idleJobsCount to set
	 */
	public void setIdleJobsCount(int idleJobsCount) {
		this.idleJobsCount = idleJobsCount;
	}

	/**
	 * @param idleJobsCount the idleJobsCount to set
	 */
	public void addIdleJobsCount(int idleJobsCount) {
		setIdleJobsCount(getIdleJobsCount() + idleJobsCount);
	}

}
