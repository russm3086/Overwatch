package com.ansys.cluster.monitor.data.interfaces;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.data.JobMessage;
import com.ansys.cluster.monitor.data.NodeProp;

public interface JobInterface {

	Duration getDuration();

	int getJobNumber();

	ArrayList<Host> getList();

	void addHost(Host host);

	void addState(StateAbstract state);

	ArrayList<JobMessage> getMessages();

	String getJobName();

	String getJobOwner();

	void setTargetQueue(String queue);

	String getTargetQueue();

	String getStartHost();

	public void setStartHost(String host);

	double getJobPriority();

	/**
	 * Start time (64bit GMT unix time stamp in milliseconds).
	 * 
	 * @return
	 */
	ZonedDateTime getJobStartTime();

	/**
	 * Submission time (64bit GMT unix time stamp in milliseconds).
	 * 
	 * @return
	 */
	ZonedDateTime getJobSubmissionTime();

	ZonedDateTime getJobHardStopTime();

	ZonedDateTime getJobSoftStopTime();

	void setJobHardStopTime(ZonedDateTime jobHardStopTime);

	void setJobSoftStopTime(ZonedDateTime jobSoftStopTime);

	String getIdentifier();

	String getJobState();

	NodeProp getJB_hard_queue_list();

	public int getNumExecHosts();

}