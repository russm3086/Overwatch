/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.state.JobState;
import com.russ.test.DetailedInfoProp;

/**
 * @author rmartine
 *
 */
public class JobsQueue extends AnsQueueAbstract {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6349369496228918137L;
	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);

	public JobsQueue(String name) {
		super(name, SGE_DataConst.clusterTypeJob);
		// TODO Auto-generated constructor stub
	}

	public JobsQueue(Job job) {
		super(job);
		addJob(job);
	}

	public void checkForIdle() {

		for (Entry<Integer, Job> entry : jobs.entrySet()) {

			if (entry.getValue().hasState(JobState.Idle)) {
				addIdleJobs(entry.getKey(), entry.getValue());
			}
		}
	}

	public void addJob(Job job) {
		logger.entering(sourceClass, "addJob", job);

		jobs.put(job.getJobNumber(), job);

		if (job.hasState(JobState.Error)) {
			errorJobs.put(job.getJobNumber(), job);

			if (job.isVisualNode()) {
				addSessionUnavailable();
				addSessionTotal();
			}

		} else {

			if (job.isVisualNode()) {

				addSessionTotal();
				addSessionAvailable();

			} else {
				addSlotTotal(job.getSlots());
				if (job.hasState(JobState.Idle)) {
					addIdleJobs(job.getJobNumber(), job);
				}
			}
		}
		logger.finest("Adding " + job + " to queue " + getQueueName());
	}

	/**
	 * @param jobs the jobs to set
	 */
	public void setJobs(SortedMap<Integer, Job> jobs) {
		this.jobs = jobs;
	}

	public String getSummary() {
		StringBuilder summary = new StringBuilder();
		summary.append("Queue Name: \t" + getName() + "\n");
		summary.append("\n# of Jobs: \t" + jobs.size());

		return summary.toString();
	}

	public String getStatus() {

		String status = "# of jobs: " + jobs.size() + "  Used " + getUnitRes() + ": " + getSlotUsed();
		return status;
	}

	/**
	 * @param errorJobs the errorJobs to set
	 */
	public void setErrorJobs(SortedMap<Integer, Job> errorJobs) {
		this.errorJobs = errorJobs;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return jobs.size();
	}

	@Override
	public boolean containsKey(String queueName) {
		// TODO Auto-generated method stub
		return jobs.containsKey(queueName);
	}

	public SortedMap<Object, ClusterNodeAbstract> getNodes() {
		// TODO Auto-generated method stub

		SortedMap<Object, ClusterNodeAbstract> map = new TreeMap<Object, ClusterNodeAbstract>();
		map.putAll(jobs);

		return map;
	}

	public DetailedInfoProp getDetailedInfoProp() {

		DetailedInfoProp masterDiProp = new DetailedInfoProp();
		masterDiProp.setTitleMetric("Queue Name: ");
		masterDiProp.setTitleValue(getName());

		DetailedInfoProp jobSumDiProp = new DetailedInfoProp();
		jobSumDiProp.setPanelName("Job Summary");
		jobSumDiProp.addMetric("Jobs count: ", jobs.size());
		jobSumDiProp.addMetric("Active Jobs count: ", activeJobs.size());
		jobSumDiProp.addMetric("Pending Jobs count: ", pendingJobs.size());
		jobSumDiProp.addMetric("Error Jobs count: ", errorJobs.size());
		jobSumDiProp.addMetric("Idle Jobs count: ", idleJobs.size());
		masterDiProp.addDetailedInfoProp(jobSumDiProp);

		displayJobs(masterDiProp);
		displayActiveJobs(masterDiProp);
		displayPendingJobs(masterDiProp);
		displayErrorJobs(masterDiProp);
		displayIdleJobs(masterDiProp);
		
		return masterDiProp;
	}

	/**
	 * @param idleJobs the idleJobs to set
	 */
	public void setIdleJobs(SortedMap<Integer, Job> idleJobs) {
		this.idleJobs = idleJobs;
	}

	public void addIdleJobs(Integer jobId, Job job) {
		getIdleJobs().put(jobId, job);
	}

}
