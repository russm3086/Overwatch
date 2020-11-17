/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
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
	private SortedMap<String, AnsQueueAbstract> myJobsQueue = new TreeMap<String, AnsQueueAbstract>();
	private LinkedList<Quota> listQuota = null;

	public MyJobsMasterQueue(LinkedList<Quota> quotaList, JobMasterQueue jobMasterQueue, String userName) {
		super("My Jobs");
		
		logger.finest("Reversing quota list order");
		Collections.reverse(quotaList);
		listQuota=quotaList;
		
		
		setClusterType(SGE_DataConst.clusterTypeQueue);
		setMembersType(SGE_DataConst.clusterTypeMix);
		addState(AnsQueueState.Normal);

		setDetailedInfoPanel(DetailedInfoFactory.MyJobsDetailedInfoPanel);

		JobsQueue jobQueue = loadJobQueue(jobMasterQueue, userName);

		addQueue(jobQueue);
		processQueues();

		myJobsQueue.put("Jobs", jobQueue);

	}

	private JobsQueue loadJobQueue(JobMasterQueue jobMasterQueue, String userName) {
		logger.entering(sourceClass, "loadJobQueue");
		JobsQueue myJobQueue = new JobsQueue(SGE_DataConst.myJobs);

		SortedMap<String, JobsQueue> jobsQueuesMap = jobMasterQueue.getJobQueues();
		for (Entry<String, JobsQueue> entry : jobsQueuesMap.entrySet()) {
			logger.finer("Expecting Job queue " + entry.getKey());

			JobsQueue queue = entry.getValue();
			for (Entry<Integer, Job> entryJob : queue.getAllmaps().entrySet()) {

				Job job = entryJob.getValue();

				if (myJobs(job, userName)) {
					
					logger.finest("Found user's job " + job.getJobNumber());
					myJobQueue.addJob(job);
				}
			}
		}

		logger.exiting(sourceClass, "loadJobQueue");

		return myJobQueue;
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
	public SortedMap<String, AnsQueueAbstract> getQueues() {
		return myJobsQueue;
	}

	@Override
	public String getToolTip() {
		// TODO Auto-generated method stub
		Quota quota = getOverallQuota();
		StringBuilder sb = new StringBuilder("my Job(s): ");
		sb.append("\n Running " + myJobsQueue.size() + " job(s)");

		if (quota != null) {
			sb.append("\n Quota " + quota.getUsage() + " of " + quota.getLimit());
		}

		return sb.toString();
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return myJobsQueue.size();
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

		displayDetailJobs(masterDiProp, findActiveJobs(), "Active Jobs");
		// displayActiveJobs(masterDiProp, findActiveJobs());
		displayPendingJobs(masterDiProp, findPendingJobs());
		displayErrorJobs(masterDiProp, findErrorJobs());
		displayIdleJobs(masterDiProp, findIdleJobs());

		displayActiveSessionJobs(masterDiProp, findActiveSessionJobs());
		displayPendingSessionJobs(masterDiProp, findPendingSessionJobs());
		displayIdleSessionJobs(masterDiProp, findIdleSessionJobs());
		displayErrorSessionJobs(masterDiProp, findErrorSessionJobs());

		displayJobsPie(masterDiProp);
		createQuotaBarChartPanel(masterDiProp);
		return masterDiProp;

	}

	private void displayJobsPie(DetailedInfoProp masterDiProp) {
		DetailedInfoProp jobDiProp = new DetailedInfoProp();
		jobDiProp.setPanelName(SGE_DataConst.panelTitleComputeQuotasUsage);
		int total = 0;
		JobsQueue myJobQueue = (JobsQueue) myJobsQueue.get("Jobs");

		HashMap<Integer, Job> jobsMap = new HashMap<Integer, Job>();

		jobsMap.putAll(myJobQueue.getActiveJobs());
		jobsMap.putAll(myJobQueue.getIdleJobs());

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
		masterDiProp.addDetailedInfoProp(jobDiProp);
	}

	protected void createQuotaBarChartPanel(DetailedInfoProp masterDiProp) {

		DetailedInfoProp diProp = new DetailedInfoProp();
		diProp.setPanelName("Quota Summary");

		for (Quota quota : listQuota) {

			String queueName = quota.getQuotaName().substring(0, quota.getQuotaName().indexOf("_"));
			queueName.toLowerCase();
			queueName = queueName.substring(0, 1).toUpperCase() + queueName.substring(1);

			int[] data = { quota.getLimit() - quota.getUsage(), quota.getUsage(), quota.getLimit() };
			diProp.addSeries(queueName, data, quota.getResource(),
					(quota.getLimit() - quota.getUsage()) + " Available");
		}

		diProp.setDataTypeProgressBarChart();
		diProp.setChartDataTitle("");
		masterDiProp.addDetailedInfoProp(diProp);
	}

}