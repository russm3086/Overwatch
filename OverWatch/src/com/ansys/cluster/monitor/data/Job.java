/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.interfaces.JobInterface;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.ansys.cluster.monitor.data.state.JobState;
import com.ansys.cluster.monitor.gui.table.TableBuilder;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoProp;

/**
 * 
 * @author rmartine
 * @since
 */
public class Job extends ClusterNodeAbstract implements JobInterface {

	// TODO session information
	/**
	 * 
	 */
	private static final long serialVersionUID = 3221740341615040025L;
	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);
	private ArrayList<Host> hostList = new ArrayList<Host>();
	private double hostLoad = 0.0;

	/**
	 * Contains the resources the job is using
	 */
	private ArrayList<NodeProp> resourceList = new ArrayList<NodeProp>();

	/**
	 * 
	 */
	public Job(NodeProp nodeProp) {
		// TODO Auto-generated constructor stub
		super(nodeProp);
		setName(nodeProp.getJobName());
		addState(JobState.parseCode(nodeProp.getJobState()), JobState.Queued);
		setStatus();
		setClusterType(SGE_DataConst.clusterTypeJob);

	}

	private boolean checkExclusivity(NodeProp nodeProp) {
		boolean result = false;

		String gruName = (String) nodeProp.get("GRU_name");
		if (gruName != null && gruName.equalsIgnoreCase("exclusive"))
			result = true;

		logger.finer("Job: " + getJobNumber() + " Exclusivity: " + result);
		return result;
	}

	private void resourceCheck() {

		NodeProp jaTaskProp = (NodeProp) nodeProp.get("JB_ja_tasks");
		if (jaTaskProp != null) {
			processResourcesList(jaTaskProp);
			processScaledUsageList(jaTaskProp);

		}

	}

	private ArrayList<NodeProp> retrieveNodePropList(String key) {

		ArrayList<NodeProp> nodePropList = new ArrayList<NodeProp>();
		ArrayList<?> list = (ArrayList<?>) nodeProp.get(key);
		if (list != null) {

			for (Object object : list) {

				NodeProp nodeProp = (NodeProp) object;
				nodePropList.add(nodeProp);
			}

		}
		return nodePropList;
	}

	private String printNodePropList(ArrayList<NodeProp> nodePropList) {
		ArrayList<String> allowedList = new ArrayList<String>();
		allowedList.add("ST_name");
		allowedList.add("VA_variable");
		allowedList.add("VA_value");

		return printNodePropList(nodePropList, allowedList, false, 1000);
	}

	private String printNodePropList(ArrayList<NodeProp> nodePropList, ArrayList<String> allowedList,
			boolean showFields, int intLength) {

		StringBuilder sb = new StringBuilder();
		for (NodeProp prop : nodePropList) {

			for (Entry<Object, Object> e : prop.entrySet()) {

				String key = (String) e.getKey();
				String value = (String) e.getValue();

				if (allowedList.contains(key)) {
					if (showFields) {
						sb.append(key);
						sb.append(": ");

					}
					sb.append(value);
					sb.append(" ");
				}
			}
		}

		if (intLength != -1 && sb.length() > intLength) {
			sb = new StringBuilder(sb.substring(0, Math.min(sb.length(), intLength)));
			sb.append(" ...");
		}

		return sb.toString();
	}

	private void processScaledUsageList(NodeProp jaTaskProp) {

		ArrayList<?> JAT_scaled_usage_list = (ArrayList<?>) jaTaskProp.get("JAT_scaled_usage_list");

		if (JAT_scaled_usage_list != null) {

			for (Object prop : JAT_scaled_usage_list) {

				NodeProp props = (NodeProp) prop;
				String key = (String) props.get("UA_name");
				String value = (String) props.get("UA_value");
				this.nodeProp.putLog(key, value);
			}
		}

	}

	private void processResourcesList(NodeProp jaTaskProp) {

		processResourcesList(jaTaskProp, "JAT_granted_resources_list");
		processResourcesList(jaTaskProp, "JAT_granted_destin_identifier_list");
	}

	private void processResourcesList(NodeProp jaTaskProp, String element) {

		Object data = jaTaskProp.get(element);
		ArrayList<NodeProp> resourceList = new ArrayList<NodeProp>();
		if (data != null) {

			String instance = data.getClass().getSimpleName();

			switch (instance) {

			case "ArrayList":

				ArrayList<?> jat_grant_res_list = (ArrayList<?>) data;
				for (Object propObj : jat_grant_res_list) {

					NodeProp propGrantRes = (NodeProp) propObj;
					resourceList.add(propGrantRes);

					setBoExclusive(checkExclusivity(propGrantRes));
				}

				break;

			case "NodeProp":
				NodeProp propGrantRes = (NodeProp) data;
				resourceList.add(propGrantRes);
				break;
			}

			setResourceList(resourceList);

		}
	}

	@Override
	public Duration getDuration() {

		LocalDateTime startTime = LocalDateTime.now(Clock.systemDefaultZone());
		if (getJobStartTime() != null) {
			startTime = getJobStartTime();
		}
		if (getJobSubmissionTime() != null) {
			startTime = getJobSubmissionTime();
		}

		LocalDateTime finishTime = LocalDateTime.now(Clock.systemDefaultZone());

		Duration duration = Duration.between(startTime, finishTime);

		return duration;
	}

	public void setStatus() {
		// TODO Auto-generated method stub

		status = "ID: " + getJobNumber() + " Duration: " + getDuration().toHours() + " hours";
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getJobName());
		sb.append("\t");
		sb.append(getJobNumber());
		return sb.toString();
	}

	private String getHostList() {
		StringBuffer sb = new StringBuffer();
		if (getList().size() > 0) {
			for (Host host : getList()) {
				sb.append(host);
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	public void mergeData(NodeProp prop) {
		this.nodeProp.putAll(prop);
		resourceCheck();
		setStatus();
	}

	@Override
	public int getJobNumber() {
		return nodeProp.getJobNumber();
	}

	public ArrayList<NodeProp> getResourceList() {
		return resourceList;
	}

	public void setResourceList(ArrayList<NodeProp> resourceList) {
		this.resourceList = resourceList;
	}

	@Override
	public ArrayList<Host> getList() {
		return hostList;
	}

	@Override
	public void addHost(Host host) {
		addHostLoad(host.getAvgLoad());
		if (getJobIdleThreshold() > getHostLoad() || hasState(JobState.Error)) {
			addState(JobState.Idle);
		} else {
			if (hasState(JobState.Idle)) {
				remove(JobState.Idle);
				addState(JobState.RunningState);
			}
		}

		this.hostList.add(host);
	}

	@Override
	public void addState(StateAbstract state) {
		addState(state, JobState.RunningState);
	}

	@Override
	public ArrayList<JobMessage> getMessages() {
		ArrayList<JobMessage> list = nodeProp.getJobMessages();
		return list;
	}

	@Override
	public String getJobName() {
		return nodeProp.getJobName();
	}

	@Override
	public String getJobOwner() {
		return nodeProp.getJobOwner();
	}

	@Override
	public String getTargetQueue() {
		return nodeProp.getTargetQueue();
	}

	@Override
	public void setTargetQueue(String queue) {
		nodeProp.setTargetQueue(queue);
	}

	@Override
	public String getStartHost() {
		return nodeProp.getStartHost();
	}

	public void setStartHost(String host) {
		nodeProp.setStartHost(host);
	}

	@Override
	public double getJobPriority() {
		return nodeProp.getJobPriority();
	}

	public int getSlots() {
		return nodeProp.getSlots();
	}

	@Override
	public LocalDateTime getJobStartTime() {
		return nodeProp.getJobStartTime();
	}

	@Override
	public LocalDateTime getJobSubmissionTime() {
		return nodeProp.getJobSubmissionTime();
	}

	@Override
	public String getIdentifier() {
		return String.valueOf(getJobNumber());
	}

	@Override
	public NodeProp getJB_hard_queue_list() {
		return (NodeProp) nodeProp.get("JB_hard_queue_list");
	}

	public double getJobIdleThreshold() {
		return nodeProp.getJobIdleThreshold();
	}

	public double getCPU() {
		return nodeProp.getDoubleProperty("cpu");
	}

	public double getMem() {
		return nodeProp.getDoubleProperty("mem");
	}

	public double getIO() {
		return nodeProp.getDoubleProperty("io");
	}

	public double getIOW() {
		return nodeProp.getDoubleProperty("iow");
	}

	public double getIoOps() {
		return nodeProp.getDoubleProperty("ioops");
	}

	public double getVmem() {
		return nodeProp.getDoubleProperty("vmem");
	}

	public double getMaxVmem() {
		return nodeProp.getDoubleProperty("maxvmem");
	}

	public double getMemVmm() {
		return nodeProp.getDoubleProperty("memvmm");
	}

	public ArrayList<NodeProp> getSubmissionCommandLine() {
		ArrayList<NodeProp> JB_submission_command_line_list = retrieveNodePropList("JB_submission_command_line");

		return JB_submission_command_line_list;
	}

	public ArrayList<NodeProp> getJobEnv() {
		ArrayList<NodeProp> JB_Env_list = retrieveNodePropList("JB_env_list");
		return JB_Env_list;
	}

	public String getMetaData() {
		StringBuffer output = new StringBuffer();
		output.append(super.getMetaData());
		output.append("\nDuration: " + getDuration().toHours());
		output.append("\nJob ID: " + getJobNumber());
		output.append("\nExecute Hosts: " + getHostList());
		output.append("\nJob Owner: " + getJobOwner());
		output.append("\nTarget Queue: " + getTargetQueue());
		output.append("\nStart Host: " + getStartHost());
		output.append("\nJob Priority: " + getJobPriority());
		output.append("\n" + getUnitRes() + ": " + getSlots());
		output.append("\nJob Start Time: " + getJobStartTime());
		output.append("\nJob Submission Time: " + getJobSubmissionTime());

		return output.toString();

	}

	@Override
	public String getJobState() {
		return nodeProp.getJobState();

	}

	/**
	 * @return the hostLoad
	 */
	public double getHostLoad() {
		return hostLoad;
	}

	/**
	 * @param hostLoad the hostLoad to set
	 */
	public void setHostLoad(double hostLoad) {
		this.hostLoad = hostLoad;
	}

	public void addHostLoad(double hostLoad) {
		double avg = ((getHostLoad() * hostList.size() + hostLoad)) / (hostList.size() + 1);
		setHostLoad(avg);
	}

	@Override
	public DetailedInfoProp getDetailedInfoProp() {
		DetailedInfoProp masterDiProp = new DetailedInfoProp();
		masterDiProp.setTitleMetric("Job Name: ");
		masterDiProp.setTitleValue(getName());

		DetailedInfoProp jobDiProp = new DetailedInfoProp();
		jobDiProp.setPanelName("Job Info");
		jobDiProp.addMetric("Owner: ", getJobOwner());
		jobDiProp.addMetric("Job #: ", getJobNumber());
		jobDiProp.addMetric("Job Priority: ", getJobPriority());
		jobDiProp.addMetric("Job Status Code: ", getJobState());

		if (getTargetQueue() != null)
			jobDiProp.addMetric("Target Queue: ", getTargetQueue());

		if (getStartHost() != null) {
			jobDiProp.addMetric("Start Host: ", getStartHost());
			jobDiProp.addMetric("Host Load: ", decimalFormatter.format(getHostLoad()));
		}

		jobDiProp.addMetric(getUnitRes() + ": ", getSlots());
		jobDiProp.addMetric("Job Submission: ", getJobSubmissionTime());
		jobDiProp.addMetric("Exclusive: ", isExclusive());
		jobDiProp.addMetric("Duration: ", getDuration().toHours());
		masterDiProp.addDetailedInfoProp(jobDiProp);

		DetailedInfoProp scaledDiProp = new DetailedInfoProp();
		scaledDiProp.setPanelName("Scaled Usage");
		scaledDiProp.addMetric("CPU: ", getCPU());
		scaledDiProp.addMetric("Memory: ", getMem());
		scaledDiProp.addMetric("IO: ", getIO());
		masterDiProp.addDetailedInfoProp(scaledDiProp);

		displayStateDescriptions(masterDiProp);
		displayHosts(masterDiProp);
		displayMessages(masterDiProp);
		displaySubmissionCommandLine(masterDiProp);
		displayJobEnv(masterDiProp);

		return masterDiProp;
	}

	public void displayHosts(DetailedInfoProp masterDiProp) {
		tableDisplay(masterDiProp, hostList, "Host(s)", TableBuilder.table_Host);
	}

	public void displayMessages(DetailedInfoProp masterDiProp) {
		tableDisplay(masterDiProp, getMessages(), "Message(s)", TableBuilder.table_JOB_MSG);
	}

	public void displaySubmissionCommandLine(DetailedInfoProp masterDiProp) {
		textAreaDisplay(masterDiProp, printNodePropList(getSubmissionCommandLine()), "Submission Command Line");
	}

	public void displayJobEnv(DetailedInfoProp masterDiProp) {
		textAreaDisplay(masterDiProp, printNodePropList(getJobEnv()), "Job Environment");
	}

}
