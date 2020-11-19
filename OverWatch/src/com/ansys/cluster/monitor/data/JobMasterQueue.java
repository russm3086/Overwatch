/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoProp;
import com.ansys.cluster.monitor.main.SGE_DataConst;

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
	private int errorJobsCount = 0;
	private int idleJobsCount = 0;
	private int activeJobsCount = 0;
	private int pendingJobsCount = 0;
	private int activeSessionJobsCount = 0;
	private int errorSessionJobsCount = 0;
	private int pendingSessionJobsCount = 0;
	private int idleSessionJobsCount = 0;

	private SortedMap<String, JobsQueue> jobQueues = new TreeMap<String, JobsQueue>();

	public JobMasterQueue(String name) {
		super(name);
		logger.fine("Creating " + sourceClass + " is visual node " + isVisualNode());
		setMembersType(SGE_DataConst.clusterTypeQueue);
	}

	public void addQueue(JobsQueue queue) {
		logger.finest("Adding " + queue + " to " + getName());
		jobQueues.put(queue.getQueueName(), queue);
	}

	public void processQueues() {
		logger.finest("");
		for (Entry<String, JobsQueue> entry : jobQueues.entrySet()) {
			processQueue(entry.getValue());
		}
	}

	public void processQueue(JobsQueue queue) {

		if (queue.isVisualNode()) {

			addErrorSessionJobsCount(queue.getErrorSessionJobsSize());
			addActiveSessionJobsCount(queue.getActiveSessionJobsSize());
			addPendingSessionJobsCount(queue.getPendingSessionJobsSize());
			addIdleSessionJobsCount(queue.getIdleSessionJobsSize());
			
		} else {

			addErrorJobsCount(queue.getErrorJobsSize());
			addIdleJobsCount(queue.getIdleJobsSize());
			addActiveJobsCount(queue.getActiveJobsSize());
			addPendingJobsCount(queue.getPendingJobsSize());
		}
	}

	public SortedMap<String, JobsQueue> getJobQueues() {
		return jobQueues;
	}

	/**
	 * @return the totalSize
	 */
	public int getTotalCount() {
		return getQueues().size();
	}

	public int getTotalSessionCount() {
		return (getErrorSessionJobsCount() + getActiveSessionJobsCount() + getPendingSessionJobsCount());
	}

	public int getTotalJobsCount() {
		return (getErrorJobsCount() + getActiveJobsCount() + getPendingJobsCount() + getIdleJobsCount());
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

	public DetailedInfoProp getDetailedInfoProp() {

		DetailedInfoProp masterDiProp = new DetailedInfoProp();
		masterDiProp.setTitleMetric("Queue Name: ");
		masterDiProp.setTitleValue(getName());

		DetailedInfoProp jobSumDiProp = new DetailedInfoProp();
		jobSumDiProp.setPanelName("Job Summary");
		jobSumDiProp.addMetric("Active Jobs: ", getActiveJobsCount());
		jobSumDiProp.addMetric("Pending Jobs: ", getPendingJobsCount());
		jobSumDiProp.addMetric("Error Jobs: ", getErrorJobsCount());
		jobSumDiProp.addMetric("Idle Jobs: ", getIdleJobsCount());
		jobSumDiProp.addMetric("Active Session: ", getActiveSessionJobsCount());
		jobSumDiProp.addMetric("Pending Session: ", getPendingSessionJobsCount());
		jobSumDiProp.addMetric("Error Session: ", getErrorSessionJobsCount());
		masterDiProp.addDetailedInfoProp(jobSumDiProp);

		displayDetailActiveJobs(masterDiProp, findActiveJobs());
		displayPendingJobs(masterDiProp, findPendingJobs());
		displayErrorJobs(masterDiProp, findErrorJobs());
		displayDetailIdleJobs(masterDiProp, findIdleJobs());

		displayActiveSessionJobs(masterDiProp, findActiveSessionJobs());
		displayPendingSessionJobs(masterDiProp, findPendingSessionJobs());
		displayIdleSessionJobs(masterDiProp, findIdleSessionJobs());
		displayErrorSessionJobs(masterDiProp, findErrorSessionJobs());

		return masterDiProp;
	}

	public SortedMap<Integer, Job> findActiveJobs() {
		SortedMap<Integer, Job> map = new TreeMap<Integer, Job>();

		for (Entry<String, JobsQueue> entry : getJobQueues().entrySet()) {
			map.putAll(entry.getValue().getActiveJobs());
		}
		return map;
	}

	public SortedMap<Integer, Job> findPendingJobs() {
		SortedMap<Integer, Job> map = new TreeMap<Integer, Job>();

		for (Entry<String, JobsQueue> entry : getJobQueues().entrySet()) {
			map.putAll(entry.getValue().getPendingJobs());
		}
		return map;
	}

	public SortedMap<Integer, Job> findErrorJobs() {
		SortedMap<Integer, Job> map = new TreeMap<Integer, Job>();

		for (Entry<String, JobsQueue> entry : getJobQueues().entrySet()) {
			map.putAll(entry.getValue().getErrorJobs());
		}
		return map;
	}

	public SortedMap<Integer, Job> findIdleJobs() {
		SortedMap<Integer, Job> map = new TreeMap<Integer, Job>();

		for (Entry<String, JobsQueue> entry : getJobQueues().entrySet()) {
			map.putAll(entry.getValue().getIdleJobs());
		}
		return map;
	}

	public SortedMap<Integer, Job> findActiveSessionJobs() {
		SortedMap<Integer, Job> map = new TreeMap<Integer, Job>();

		for (Entry<String, JobsQueue> entry : getJobQueues().entrySet()) {
			map.putAll(entry.getValue().getActiveSessionJobs());
		}
		return map;
	}

	public SortedMap<Integer, Job> findPendingSessionJobs() {
		SortedMap<Integer, Job> map = new TreeMap<Integer, Job>();

		for (Entry<String, JobsQueue> entry : getJobQueues().entrySet()) {
			map.putAll(entry.getValue().getPendingSessionJobs());
		}
		return map;
	}

	public SortedMap<Integer, Job> findErrorSessionJobs() {
		SortedMap<Integer, Job> map = new TreeMap<Integer, Job>();

		for (Entry<String, JobsQueue> entry : getJobQueues().entrySet()) {
			map.putAll(entry.getValue().getErrorSessionJobs());
		}
		return map;
	}

	public SortedMap<Integer, Job> findIdleSessionJobs() {
		SortedMap<Integer, Job> map = new TreeMap<Integer, Job>();

		for (Entry<String, JobsQueue> entry : getJobQueues().entrySet()) {
			map.putAll(entry.getValue().getIdleSessionJobs());
		}
		return map;
	}

	public int getIdleCores() {
		int core = 0;
		SortedMap<Integer, Job> mapIdleJobs = findIdleJobs();
		SortedMap<Integer, Job> mapIdleSession = findIdleSessionJobs();
		
		mapIdleJobs.putAll(mapIdleSession);
		
		for (Entry<Integer, Job> entry : mapIdleJobs.entrySet()) {
			
			core += entry.getValue().getSlots();
		}
		return core;
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

	/**
	 * @return the activeJobsCount
	 */
	public int getActiveJobsCount() {
		return activeJobsCount;
	}

	/**
	 * @param activeJobsCount the activeJobsCount to set
	 */
	public void setActiveJobsCount(int activeJobsCount) {
		this.activeJobsCount = activeJobsCount;
	}

	/**
	 * @param activeJobsCount the activeJobsCount to set
	 */
	public void addActiveJobsCount(int activeJobsCount) {
		setActiveJobsCount(getActiveJobsCount() + activeJobsCount);
	}

	/**
	 * @return the activeSessionJobsCount
	 */
	public int getActiveSessionJobsCount() {
		return activeSessionJobsCount;
	}

	/**
	 * @param activeSessionJobsCount the activeSessionJobsCount to set
	 */
	public void setActiveSessionJobsCount(int activeSessionJobsCount) {
		this.activeSessionJobsCount = activeSessionJobsCount;
	}

	public void addActiveSessionJobsCount(int activeSessionJobsCount) {
		setActiveSessionJobsCount(getActiveSessionJobsCount() + activeSessionJobsCount);
	}

	/**
	 * @return the errorSessionJobsCount
	 */
	public int getErrorSessionJobsCount() {
		return errorSessionJobsCount;
	}

	/**
	 * @param errorSessionJobsCount the errorSessionJobsCount to set
	 */
	public void setErrorSessionJobsCount(int errorSessionJobsCount) {
		this.errorSessionJobsCount = errorSessionJobsCount;
	}

	public void addErrorSessionJobsCount(int errorSessionJobsCount) {
		setErrorSessionJobsCount(getErrorSessionJobsCount() + errorSessionJobsCount);
	}

	public int getIdleSessionJobsCount() {
		return idleSessionJobsCount;
	}

	/**
	 * @return the pendingJobsCount
	 */
	public int getPendingJobsCount() {
		return pendingJobsCount;
	}

	/**
	 * @param pendingJobsCount the pendingJobsCount to set
	 */
	public void setPendingJobsCount(int pendingJobsCount) {
		this.pendingJobsCount = pendingJobsCount;
	}

	/**
	 * @param pendingJobsCount the pendingJobsCount to set
	 */
	public void addPendingJobsCount(int pendingJobsCount) {
		setPendingJobsCount(getPendingJobsCount() + pendingJobsCount);
	}

	/**
	 * @return the pendingSessionJobsCount
	 */
	public int getPendingSessionJobsCount() {
		return pendingSessionJobsCount;
	}

	/**
	 * @param pendingSessionJobsCount the pendingSessionJobsCount to set
	 */
	public void setIdleSessionJobsCount(int idleSessionJobsCount) {
		this.idleSessionJobsCount = idleSessionJobsCount;
	}

	public void addIdleSessionJobsCount(int idleSessionJobsCount) {
		setIdleSessionJobsCount(getIdleSessionJobsCount() + getIdleSessionJobsCount());

	}
	
	
	/**
	 * @param pendingSessionJobsCount the pendingSessionJobsCount to set
	 */
	public void setPendingSessionJobsCount(int pendingSessionJobsCount) {
		this.pendingSessionJobsCount = pendingSessionJobsCount;
	}

	public void addPendingSessionJobsCount(int pendingSessionJobsCount) {
		setPendingSessionJobsCount(getPendingSessionJobsCount() + getPendingSessionJobsCount());

	}
	
	public String getToolTip() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder(" Active Job(s): ");
		sb.append(getActiveJobsCount());
		sb.append(" Active Session(s): ");
		sb.append(getActiveSessionJobsCount());
		if(getPendingJobsCount()>0) {
			sb.append(" Pending Job(s): ");
			sb.append(getPendingJobsCount());
		}
		
		if(getPendingSessionJobsCount()>0) {
			sb.append(" Pending Session(s): ");
			sb.append(getPendingSessionJobsCount());
		}
		return sb.toString();
	}

}
