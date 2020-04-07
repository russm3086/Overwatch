/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data;

import java.util.SortedMap;
import java.util.TreeMap;
import java.awt.Color;
import java.awt.Paint;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.ansys.cluster.monitor.data.state.ClusterState;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoFactory;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoProp;

/**
 * 
 * @author rmartine
 * @since
 */
public class Cluster extends AnsQueueAbstract {
	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);
	private static final long serialVersionUID = -3265482837785245141L;

	private SortedMap<String, MasterQueue> masterQueue = new TreeMap<String, MasterQueue>(Collections.reverseOrder());

	String name;

	public Cluster(String clusterName, HostMasterQueue hostMasterQueue, JobMasterQueue jobMasterQueue) {
		super();
		logger.entering(sourceClass, "Constructor");

		setName(clusterName);

		setDetailedInfoPanel(DetailedInfoFactory.ClusterDetailedInfoPanel);

		setJobMasterQueue(jobMasterQueue);
		setHostMasterQueue(hostMasterQueue);

		setClusterType(SGE_DataConst.clusterTypeCluster);

		addState(ClusterState.Normal);

		logger.exiting(sourceClass, "Constructor");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public void addState(StateAbstract state) {
		addState(state, ClusterState.Normal);
	}

	/**
	 * @return the hostMasterQueue
	 */
	public HostMasterQueue getHostMasterQueue() {
		return (HostMasterQueue) masterQueue.get(SGE_DataConst.mqEntryQueues);
	}

	/**
	 * @param hostMasterQueue the hostMasterQueue to set
	 */
	public void setHostMasterQueue(HostMasterQueue hostMasterQueue) {
		masterQueue.put(SGE_DataConst.mqEntryQueues, hostMasterQueue);
	}

	/**
	 * @return the jobMasterQueue
	 */
	public JobMasterQueue getJobMasterQueue() {
		return (JobMasterQueue) masterQueue.get(SGE_DataConst.mqEntryJobs);
	}

	/**
	 * @param jobMasterQueue the jobMasterQueue to set
	 */
	public void setJobMasterQueue(JobMasterQueue jobMasterQueue) {
		masterQueue.put(SGE_DataConst.mqEntryJobs, jobMasterQueue);
	}

	public SortedMap<String, MasterQueue> getMasterQueue() {
		return masterQueue;
	}

	@Override
	public DetailedInfoProp getDetailedInfoProp() {

		DetailedInfoProp masterDiProp = new DetailedInfoProp();
		masterDiProp.setTitleMetric("Cluster Name: ");
		masterDiProp.setTitleValue(getName());

		createAvailableChartPanel(masterDiProp, SGE_DataConst.unitResCore,
				"Total " + getHostMasterQueue().getCoreTotal() + " " + SGE_DataConst.unitResCore,
				SGE_DataConst.unitResCore.toLowerCase(), getHostMasterQueue().getCoreAvailable(),
				getHostMasterQueue().getCoreUnavailable());

		createAvailableBarChartPanel(masterDiProp, "F.U.N. Fully Unallocated Nodes",
				"Total " + getHostMasterQueue().getFullyUnallocatedComputeHostsCore() + " " + SGE_DataConst.unitResCore,
				SGE_DataConst.unitResCore.toLowerCase(), getHostMasterQueue().getQueues());

		createAvailableChartPanel(masterDiProp, SGE_DataConst.unitResSession,
				"Total " + getHostMasterQueue().getSessionTotal() + " " + SGE_DataConst.unitResSession,
				SGE_DataConst.unitResSession.toLowerCase(), getHostMasterQueue().getSessionAvailable(),
				getHostMasterQueue().getSessionUnavailable());

		displayJobsPie(masterDiProp);

		createAvailableChartPanel(masterDiProp, "Memory",
				"Total " + decimalFormatter.format(getHostMasterQueue().getTotalMem()) + " MB", "mb",
				getHostMasterQueue().getAvailableMem(),
				getHostMasterQueue().getTotalMem() - getHostMasterQueue().getAvailableMem());

		displayNodesPie(masterDiProp);

		displayErrorJobs(masterDiProp, getJobMasterQueue().findErrorJobs());
		displayPendingJobs(masterDiProp, getJobMasterQueue().findPendingJobs());
		displayIdleJobs(masterDiProp, getJobMasterQueue().findIdleJobs());
		displayUnavailableVisualHosts(masterDiProp, getHostMasterQueue().findUnavailableVisualHosts());
		displayUnavailableComputeHosts(masterDiProp, getHostMasterQueue().findUnavailableComputeHosts());

		dislayJobsBubble(masterDiProp);

		return masterDiProp;
	}

	private void displayJobsPie(DetailedInfoProp masterDiProp) {
		DetailedInfoProp jobDiProp = new DetailedInfoProp();
		jobDiProp.setPanelName(SGE_DataConst.panelTitleJob);
		jobDiProp.addChartData("Active", getJobMasterQueue().getActiveJobsCount(), new Color(0, 153, 0));
		jobDiProp.addChartData("Pending", getJobMasterQueue().getPendingJobsCount(), new Color(255, 255, 0));
		jobDiProp.addChartData("Idle", getJobMasterQueue().getIdleJobsCount(), new Color(0, 0, 255));
		jobDiProp.addChartData("Error", getJobMasterQueue().getErrorJobsCount(), new Color(204, 0, 0));
		jobDiProp.setChartDataTitle(
				"Total: " + getJobMasterQueue().getTotalJobsCount() + " " + SGE_DataConst.panelTitleJob.toLowerCase());
		jobDiProp.setChartDataUnit(SGE_DataConst.panelTitleJob.toLowerCase());
		jobDiProp.setDataTypePieChart();
		masterDiProp.addDetailedInfoProp(jobDiProp);
	}

	private void displayNodesPie(DetailedInfoProp masterDiProp) {
		int availableComputeHost = getHostMasterQueue().findAvailableComputeHosts().size();
		int unAvailableComputeHost = getHostMasterQueue().findUnavailableComputeHosts().size();
		int availableVisualHost = getHostMasterQueue().findAvailableVisualHosts().size();
		int unAvailableVisualHost = getHostMasterQueue().findUnavailableVisualHosts().size();
		int total = availableComputeHost + unAvailableComputeHost + availableVisualHost + unAvailableVisualHost;

		DetailedInfoProp jobDiProp = new DetailedInfoProp();
		jobDiProp.setPanelName("Cluster Node(s)");
		// Dark Green
		jobDiProp.addChartData("Available Compute Host", availableComputeHost, new Color(0, 153, 0));
		// Dark Red
		jobDiProp.addChartData("Unavailable Visual Host", unAvailableVisualHost, new Color(255, 51, 51));
		// Light Red
		jobDiProp.addChartData("Unavailable Compute Host", unAvailableComputeHost, new Color(204, 0, 0));
		// light Green
		jobDiProp.addChartData("Available Visual Host", availableVisualHost, new Color(0, 255, 51));

		jobDiProp.setChartDataTitle("Total: " + total + " nodes");
		jobDiProp.setChartDataUnit("nodes");
		jobDiProp.setDataTypePieChart();
		masterDiProp.addDetailedInfoProp(jobDiProp);
	}

	private void dislayJobsBubble(DetailedInfoProp masterDiProp) {

		DetailedInfoProp jobDiProp = new DetailedInfoProp();
		SortedMap<Integer, Job> mapActiveJob = getJobMasterQueue().findActiveJobs();
		SortedMap<Integer, Job> mapIdleJob = getJobMasterQueue().findIdleJobs();

		generateSeries(mapActiveJob, jobDiProp, "Active", new Color(0, 153, 0, 100));
		generateSeries(mapIdleJob, jobDiProp, "Idle", new Color(0, 0, 255, 130));

		jobDiProp.setDataTypeBubbleChart();
		jobDiProp.get_xAxisLabel("Host Load");
		jobDiProp.get_yAxisLabel("Durations Hours");

		masterDiProp.addDetailedInfoProp(jobDiProp);
	}

	private void generateSeries(SortedMap<Integer, Job> map, DetailedInfoProp jobDiProp, String seriesName,
			Paint paint) {

		double[] xArray = new double[map.size()];
		double[] yArray = new double[map.size()];
		double[] zArray = new double[map.size()];

		int i = 0;
		for (Entry<Integer, Job> entry : map.entrySet()) {
			Job job = entry.getValue();

			xArray[i] = job.getHostLoad();
			yArray[i] = ((double) job.getDuration().toHours());
			zArray[i] = job.getSlots();

			// System.out.println("Load: " + xArray[i] + " Duration: " + yArray[i] + " core:
			// " + zArray[i]);

			i += 1;
		}
		double series[][] = { xArray, yArray, zArray };

		jobDiProp.addSeries(seriesName, series, paint);

	}

	@Override
	public int size() {
		int size = getJobMasterQueue().size() + getHostMasterQueue().size();
		return size;
	}

	@Override
	public String getToolTip() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("Cluster: ");
		sb.append(getName());
		sb.append(" ");
		sb.append("Available Cores:");
		sb.append(getHostMasterQueue().getCoreAvailable());
		sb.append("Available Sessions:");
		sb.append(" ");
		sb.append(getHostMasterQueue().getCoreAvailable());
		return sb.toString();
	}

}
