/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.state.AnsQueueState;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoFactory;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoProp;

/**
 * @author rmartine
 *
 */
public class MyJobs extends JobMasterQueue {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9204394829856870587L;
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	private SortedMap<String, AnsQueueAbstract> myJobsQueue = new TreeMap<String, AnsQueueAbstract>();
	private List<Quota> listQuota = null;

	public MyJobs(Cluster cluster, String userName) {
		super("My Jobs");

		setClusterType(SGE_DataConst.clusterTypeQueue);
		setMembersType(SGE_DataConst.clusterTypeMix);
		addState(AnsQueueState.Normal);

		setDetailedInfoPanel(DetailedInfoFactory.MyJobsDetailedInfoPanel);

		JobsQueue jobQueue = loadJobQueue(cluster.getJobMasterQueue(), userName);

		addQueue(jobQueue);
		processQueues();

		myJobsQueue.put("Jobs", jobQueue);
		listQuota = cluster.getQuotaMap().get(userName);

	}

	private JobsQueue loadJobQueue(JobMasterQueue jobMasterQueue, String userName) {
		JobsQueue myJobQueue = new JobsQueue(SGE_DataConst.myJobs);

		SortedMap<String, JobsQueue> jobsQueuesMap = jobMasterQueue.getJobQueues();
		for (Entry<String, JobsQueue> entry : jobsQueuesMap.entrySet()) {
			logger.finer("Expecting Job queue " + entry.getKey());

			JobsQueue queue = entry.getValue();
			for (Entry<Integer, Job> entryJob : queue.getAllmaps().entrySet()) {

				Job job = entryJob.getValue();

				if (myJobs(job, userName)) {

					myJobQueue.addJob(job);
				}
			}
		}

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

		DetailedInfoProp masterDiProp = super.getDetailedInfoProp();

		masterDiProp.setTitleMetric("Queue Name:");
		masterDiProp.setTitleValue(getName());

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

			// diProp.addChartData(quota.getLimit(), quota.getUsage(),
			// quota.getQuotaName());
			int[] data = { quota.getLimit() - quota.getUsage(), quota.getUsage(), quota.getLimit() };
			diProp.addSeries(quota.getQuotaName(), data, quota.getResource(), quota.getQuotaName() + " Avaiable Quota");
		}

		diProp.setDataTypeProgressBarChart();
		diProp.setChartDataTitle("");
		masterDiProp.addDetailedInfoProp(diProp);
	}

}
