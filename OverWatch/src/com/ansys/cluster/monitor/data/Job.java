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
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.interfaces.JobInterface;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.ansys.cluster.monitor.data.state.JobState;
import com.russ.test.DetailedInfoProp;

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
	// private ArrayList<Host> hostList = new ArrayList<Host>();
	private HashMap<Host, Double> hostMap = new HashMap<Host, Double>();
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
		if (((String) nodeProp.get("GRU_name")).equalsIgnoreCase("exclusive"))
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

		return printNodePropList(nodePropList, allowedList, false, 200);
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

		Object data = jaTaskProp.get("JAT_granted_resources_list");
		if (data != null) {

			ArrayList<NodeProp> resourceList = new ArrayList<NodeProp>();
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

		status = "Name: " + getJobName() + "\tID: " + getJobNumber() + "\tOwner: " + getJobOwner() + "\tExclusive: "
				+ isExclusive() + "\tDuration: " + getDuration().toHours() + " hours";
	}

	@Override
	public void checkIdleStatus() {
		logger.entering(sourceClass, "checkIdleStatus");

		logger.exiting(sourceClass, "checkIdleStatus");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.ClusterNode#Summary()
	 */
	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();

		sb.append(outputFormatter("Job Name:", getJobName()));
		sb.append(outputFormatter("Owner:", getJobOwner()));

		if (getTargetQueue() != null)
			sb.append(outputFormatter("Target Queue:", getTargetQueue()));

		if (getStartHost() != null) {
			sb.append(outputFormatter("Start Host:", getStartHost()));
			sb.append(outputFormatter("Host Load:", decimalFormatter.format(getHostLoad())));
		}

		sb.append(outputFormatter("Job #:", getJobNumber()));
		sb.append(outputFormatter("Job Priority:", getJobPriority()));
		sb.append(outputFormatter("Job Status Code:", getJobState()));
		sb.append(outputFormatter(getUnitRes(), getSlots()));

		if (getJobStartTime() != null)
			sb.append(outputFormatter("Job Start:", getJobStartTime()));

		sb.append(outputFormatter("Job Submission:", getJobSubmissionTime()));
		sb.append(outputFormatter("Exclusive:", isExclusive()));
		sb.append(summaryOutput("Duration:", getDuration().toHours(), "hours"));
		sb.append(outputFormatter("State:", getStateDescriptions()));

		sb.append("Scaled Usage:\n");
		sb.append(outputFormatter("CPU:", getCPU()));
		sb.append(outputFormatter("Memory:", getMem()));
		sb.append(outputFormatter("IO:", getIO()));

		if (hostMap.size() > 0) {
			sb.append("\nHost(s):");
			sb.append(getHostList());
		}

		if (getMessages().length() > 0) {
			sb.append("\nMessages:\n");
			sb.append(getMessages());
		}

		if (getSubmissionCommandLine().size() > 0) {
			sb.append("\nSubmission Commandline:\n");
			sb.append(printNodePropList(getSubmissionCommandLine()));
		}

		if (getJobEnv().size() > 0) {
			sb.append("\nEnvironment Settings:\n");
			sb.append(printNodePropList(getJobEnv()));
		}

		return sb.toString();
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
		sb.append("\n");
		for (Entry<Host, Double> entry : hostMap.entrySet()) {
			sb.append(entry.getKey());
			sb.append(" load:\t");
			sb.append(entry.getValue());
			sb.append("\n");
		}
		return sb.toString();
	}

	public void mergeData(NodeProp prop) {
		this.nodeProp.putAll(prop);
		resourceCheck();
		setStatus();
		checkIdleStatus();
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
	public HashMap<Host, Double> getList() {
		return hostMap;
	}

	@Override
	public void addHost(Host host) {
		addHostLoad(host.getAvgLoad());
		if (getJobIdleThreshold() > getHostLoad()) {
			addState(JobState.Idle);
		} else {
			if (hasState(JobState.Idle)) {
				remove(JobState.Idle);
			}
		}

		Double load = new Double(host.getAvgLoad());
		this.hostMap.put(host, load);
	}

	@Override
	public void addState(StateAbstract state) {
		addState(state, JobState.RunningState);
	}

	@Override
	public String getMessages() {
		StringBuffer sb = new StringBuffer();
		ArrayList<JobMessage> list = nodeProp.getJobMessages();

		if (list != null) {
			for (JobMessage jm : list) {

				sb.append("\tMessage: " + jm.getMessage());
				sb.append("\n");
			}
		}
		return sb.toString();
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
		output.append("\nJob Id: " + getJobNumber());
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
		double avg = ((getHostLoad() * hostMap.size() + hostLoad)) / (hostMap.size() + 1);
		setHostLoad(avg);
	}

	@Override
	public DetailedInfoProp getDetailedInfoProp() {
		// TODO Auto-generated method stub
		return null;
	}

}
