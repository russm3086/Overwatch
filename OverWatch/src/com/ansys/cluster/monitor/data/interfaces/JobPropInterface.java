package com.ansys.cluster.monitor.data.interfaces;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import com.ansys.cluster.monitor.data.JobMessage;

public interface JobPropInterface extends Serializable {

	String getJobStateTranslated();

	int getJobNumber();

	double getJobPriority();

	String getJobName();

	String getJobOwner();

	String getJobState();

	ZonedDateTime getJobStartTime();

	ZonedDateTime getJobSubmissionTime();

	public void setQueueName(String queueName);

	public String getQueueName();

	String getJobQueueName();

	void setJobQueueName(String queue);

	String getJclassName();

	int getSlots();

	String getStartHost();

	void setStartHost(String start_Host);

	boolean isMyJob();

	void setMyJobTrue();

	void setMyJobFalse();

	public void setJobMessages(ArrayList<JobMessage> list);

	public ArrayList<JobMessage> getJobMessages();

	public void addJobMessage(JobMessage msg);

	public double getJobIdleThreshold();

	public void setJobIdleThreshold(double value);

	public String getTargetQueue();

	public void setTargetQueue(String queue);

	public ZoneId getZoneID();

	public void setZoneID(ZoneId zoneId);

}