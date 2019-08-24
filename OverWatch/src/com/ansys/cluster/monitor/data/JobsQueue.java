/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.state.JobState;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoProp;

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

		for (Entry<Integer, Job> entry : getActiveJobs().entrySet()) {

			if (entry.getValue().hasState(JobState.Idle)) {
				addIdleJobs(entry.getKey(), entry.getValue());
			}
		}
	}

	public void addJob(Job job) {
		logger.entering(sourceClass, "addJob", job);

		if (job.isVisualNode()) {

			if (job.hasState(JobState.RunningState)) {

				addActiveSessionJobs(job.getJobNumber(), job);
			} else if (job.getQueueName().equalsIgnoreCase(SGE_DataConst.job_PendingQueue)) {

				addPendingSessionJobs(job.getJobNumber(), job);
			} else {

				addErrorSessionJobs(job.getJobNumber(), job);
			}

		} else {

			if (job.hasState(JobState.RunningState)) {

				addActiveJobs(job.getJobNumber(), job);
			} else if (job.hasState(JobState.Idle)) {

				addIdleJobs(job.getJobNumber(), job);
			} else if (job.getQueueName().equalsIgnoreCase(SGE_DataConst.job_PendingQueue)) {

				addPendingJobs(job.getJobNumber(), job);
			} else {

				addErrorJobs(job.getJobNumber(), job);
			}
		}

		logger.finest("Adding " + job + " to queue " + getQueueName());
	}

	public String getStatus() {

		String status = "# of active jobs: " + getActiveJobsSize();
		return status;
	}

	public int size() {
		return getAllmaps().size();
	}

	public SortedMap<Object, ClusterNodeAbstract> getNodes() {
		SortedMap<Object, ClusterNodeAbstract> map = new TreeMap<Object, ClusterNodeAbstract>();
		map.putAll(getAllmaps());

		return map;
	}

	public SortedMap<Integer, Job> getAllmaps() {
		SortedMap<Integer, Job> map = new TreeMap<Integer, Job>();
		if (!isVisualNode()) {

			map.putAll(getActiveJobs());
			map.putAll(getPendingJobs());
			map.putAll(getIdleJobs());
			map.putAll(getErrorJobs());
		} else {

			map.putAll(getActiveSessionJobs());
			map.putAll(getPendingSessionJobs());
			map.putAll(getErrorSessionJobs());
		}

		return map;
	}

	public DetailedInfoProp getDetailedInfoProp() {

		DetailedInfoProp masterDiProp = new DetailedInfoProp();
		masterDiProp.setTitleMetric("Queue Name: ");
		masterDiProp.setTitleValue(getName());

		displayActiveJobs(masterDiProp);
		displayPendingJobs(masterDiProp);
		displayErrorJobs(masterDiProp);
		displayIdleJobs(masterDiProp);
		displayUnavailableComputeHosts(masterDiProp);

		displayActiveSessionJobs(masterDiProp);
		displayPendingSessionJobs(masterDiProp);
		displayErrorSessionJobs(masterDiProp);
		displayUnavailableVisualHosts(masterDiProp);

		return masterDiProp;
	}

}
