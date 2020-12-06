/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.state.AnsQueueState;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoFactory;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoProp;
import com.ansys.cluster.monitor.main.SGE_DataConst;

/**
 * @author rmartine
 *
 */
public class MyJobsMasterQueue extends JobMasterQueue {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9204394829856870587L;
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	// private SortedMap<String, AnsQueueAbstract> myJobsQueue = new TreeMap<String,
	// AnsQueueAbstract>();
	private LinkedList<Quota> listQuota = null;

	public MyJobsMasterQueue(LinkedList<Quota> quotaList, JobMasterQueue jobMasterQueue, String userName) {
		super(SGE_DataConst.clusterMyJobs);

		if (userName != null && userName.trim().length() > 0) {

			setName("My Jobs - " + userName);
		}

		logger.entering(sourceClass, "MyJobsMasterQueue");

		logger.finest("Reversing quota list order");
		Collections.reverse(quotaList);
		listQuota = quotaList;

		setClusterType(SGE_DataConst.clusterTypeQueue);
		setMembersType(SGE_DataConst.clusterTypeMix);
		addState(AnsQueueState.Normal);

		setDetailedInfoPanel(DetailedInfoFactory.MyJobsDetailedInfoPanel);

		loadJobQueue(jobMasterQueue, userName);
		processQueues();

		logger.exiting(sourceClass, "MyJobsMasterQueue");
	}

	private void loadJobQueue(JobMasterQueue jobMasterQueue, String userName) {
		logger.entering(sourceClass, "loadJobQueue");

		SortedMap<String, JobsQueue> jobsQueuesMap = jobMasterQueue.getJobQueues();
		for (Entry<String, JobsQueue> entry : jobsQueuesMap.entrySet()) {
			logger.finer("Expecting Job queue " + entry.getKey());

			JobsQueue queue = entry.getValue();
			for (Entry<Integer, Job> entryJob : queue.getAllmaps().entrySet()) {

				Job job = entryJob.getValue();

				if (myJobs(job, userName)) {

					logger.finest("Found user's job " + job.getJobNumber());

					JobsQueue myQueue = getQueue(job.getQueueName());

					if (myQueue != null) {

						myQueue.addJob(job);
					} else {

						myQueue = new JobsQueue(job);

						logger.finer("Created queue " + myQueue + " added node " + job);
						addQueue(myQueue);
					}

				}
			}
		}

		logger.exiting(sourceClass, "loadJobQueue");

		// return myJobQueue;
	}

	public boolean myJobs(Job job, String userName) {
		logger.entering(sourceClass, "myJobs", job);
		boolean result = false;
		if (job != null) {
			String jobOwner = job.getJobOwner();
			if (jobOwner != null && jobOwner.equalsIgnoreCase(userName)) {
				result = true;
			}
		}
		logger.exiting(sourceClass, "myJobs", result);
		return result;
	}

	private Quota getOverallQuota() {

		for (Quota quota : listQuota) {

			if (quota.getQuotaName().matches("overall.*"))
				return quota;
		}
		return null;
	}

	@Override
	public String getToolTip() {

		String tip = super.getToolTip();
		Quota quota = getOverallQuota();
		StringBuilder sb = new StringBuilder("My Job(s): ");
		sb.append("\n" + tip);

		if (quota != null) {
			sb.append("\n Quota " + quota.getUsage() + " of " + quota.getLimit());
		}

		return sb.toString();
	}

	public DetailedInfoProp getDetailedInfoProp() {

		DetailedInfoProp masterDiProp = new DetailedInfoProp();
		super.getDetailedInfoPropTitle(masterDiProp);
		super.getDetailedInfoPropSummary(masterDiProp);

		displayMyDetailJobs(masterDiProp, findActiveJobs(), "Active Jobs");
		displayMyDetailJobs(masterDiProp, findPendingJobs(), "Pending Jobs");
		displayMyDetailJobs(masterDiProp, findErrorJobs(), "Error Jobs");
		displayMyDetailJobs(masterDiProp, findIdleJobs(), "Idle Jobs");

		displayMyVisualJobs(masterDiProp, findActiveSessionJobs(), "Active Sessions");
		displayMyVisualJobs(masterDiProp, findPendingSessionJobs(), "Pending Sessions");
		displayMyVisualJobs(masterDiProp, findIdleSessionJobs(), "Idle Sessions");
		displayMyVisualJobs(masterDiProp, findErrorSessionJobs(), "Error Sessions");

		displayJobsPie(masterDiProp);
		createQuotaBarChartPanel(masterDiProp);
		return masterDiProp;

	}

	private void displayJobsPie(DetailedInfoProp masterDiProp) {
		DetailedInfoProp jobDiProp = new DetailedInfoProp();
		jobDiProp.setPanelName(SGE_DataConst.panelTitleComputeQuotasUsage);
		int total = 0;

		HashMap<Integer, Job> jobsMap = new HashMap<Integer, Job>();

		jobsMap.putAll(findActiveJobs());
		jobsMap.putAll(findIdleJobs());

		for (Entry<Integer, Job> entry : jobsMap.entrySet()) {

			Job job = (Job) entry.getValue();

			total += job.getSlots();
			StringBuffer sb = new StringBuffer();
			sb.append(entry.getKey());
			sb.append("-");
			sb.append(job.getJobName());
			jobDiProp.addChartData(sb.toString(), job.getSlots());
		}

		jobDiProp.setChartDataTitle("Total: " + total + " " + SGE_DataConst.unitResCore.toLowerCase());
		jobDiProp.setChartDataUnit(SGE_DataConst.unitResCore.toLowerCase());
		jobDiProp.setDataTypePieChart();
		jobDiProp.setSectionColorFalse();
		masterDiProp.addDetailedInfoProp(jobDiProp);
	}

	protected void createQuotaBarChartPanel(DetailedInfoProp masterDiProp) {

		DetailedInfoProp diProp = new DetailedInfoProp();
		diProp.setPanelName("Quota Summary");

		for (Quota quota : listQuota) {

			String queueName = quota.getQuotaName().substring(0, quota.getQuotaName().indexOf("_"));
			queueName.toLowerCase();
			queueName = queueName.substring(0, 1).toUpperCase() + queueName.substring(1);

			StringBuffer sb = new StringBuffer("Name: ");
			sb.append(quota.getQuotaName());
			sb.append(" Limit: ");
			sb.append(quota.getLimit());
			sb.append(" Usage: ");
			sb.append(quota.getUsage());
			sb.append(" Resource: ");
			sb.append(quota.getResource());

			logger.finer(sb.toString());

			int[] data = { quota.getLimit() - quota.getUsage(), quota.getUsage(), quota.getLimit() };

			diProp.addSeries(queueName, data, quota.getResource(), (quota.getUsage()) + " Consumed");
		}

		diProp.setDataTypeProgressBarChart();
		diProp.setChartDataTitle("");
		masterDiProp.addDetailedInfoProp(diProp);
	}

}
