/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.SortedMap;
import java.util.TreeMap;
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

	public void addJob(Job job) {
		logger.entering(sourceClass, "addJob", job);

		if (job.isVisualNode()) {

			if (job.hasState(JobState.RunningState)) {

				addActiveSessionJobs(job.getJobNumber(), job);
			} else if (job.hasState(JobState.Idle)) {

				addIdleSessionJobs(job.getJobNumber(), job);
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

		map.putAll(getActiveJobs());
		map.putAll(getPendingJobs());
		map.putAll(getIdleJobs());
		map.putAll(getErrorJobs());
		map.putAll(getActiveSessionJobs());
		map.putAll(getPendingSessionJobs());
		map.putAll(getErrorSessionJobs());

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
		displayIdleSessionJobs(masterDiProp);
		displayUnavailableVisualHosts(masterDiProp);

		return masterDiProp;
	}

	@Override
	public String getToolTip() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder(" Active Job(s): ");
		sb.append(getActiveJobsSize());
		sb.append(" Active Session(s): ");
		sb.append(getActiveSessionJobsSize());
		if (getPendingJobsSize() > 0) {
			sb.append(" Pending Job(s): ");
			sb.append(getPendingJobsSize());
		}

		if (getPendingSessionJobsSize() > 0) {
			sb.append(" Pending Session(s): ");
			sb.append(getPendingSessionJobsSize());
		}
		return sb.toString();
	}

}
