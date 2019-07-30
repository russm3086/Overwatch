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
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.interfaces.JobInterface;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.ansys.cluster.monitor.data.state.JobState;

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

		if (idleExecNodes()) {

			logger.finer("Adding JobState: idle");
			addState(JobState.Idle);
		}

		logger.exiting(sourceClass, "checkIdleStatus");
	}

	public boolean idleExecNodes() {
		boolean result = false;

		if (hostList != null) {
			for (Host host : hostList) {

				logger.finer("Host: " + host + " load: " + host.getLoad());

				if (host.getLoad() <= nodeProp.getJobIdleThreshold()) {
					result = true;

				} else {
					return false;
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.ClusterNode#Summary()
	 */
	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		String summary = "Job Name:\t\t" + getJobName();
		summary += "\nOwner:\t\t" + getJobOwner();

		if (getTargetQueue() != null)
			summary += "\nTarget Queue:\t\t" + getTargetQueue();

		if (getStartHost() != null)
			summary += "\nStart Host\t\t" + getStartHost();

		summary += "\nJob #:\t\t" + getJobNumber();
		summary += "\nJob Priority: \t\t" + getJobPriority();
		summary += "\n" + getUnitRes() + ":\t\t" + getSlots();

		if (getJobStartTime() != null)
			summary += "\nJob Start:\t\t" + getJobStartTime();
		
		summary += "\nJob Submission:\t" + getJobSubmissionTime();
		summary += "\nExclusive:\t\t" + isExclusive();
		summary += "\nDuration:\t\t" + getDuration().toHours() + " hours";
		summary += "\n\nState:\t\t" + getStateDescriptions();

		if (hostList.size() > 0)
			summary += "\nHost(s):" + getHostList();

		if (getMessages().length() > 0)
			summary += "\n\nMessages:\n" + getMessages();

		return summary;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getJobName());
		sb.append("    ");
		sb.append(getJobNumber());
		return sb.toString();
	}

	private String getHostList() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		for (Host host : hostList) {
			sb.append(host);
			sb.append("\t");
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
	public ArrayList<Host> getList() {
		return hostList;
	}

	@Override
	public void addHost(Host host) {
		this.hostList.add(host);
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

}
