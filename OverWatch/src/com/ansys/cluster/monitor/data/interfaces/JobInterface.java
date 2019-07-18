package com.ansys.cluster.monitor.data.interfaces;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.ansys.cluster.monitor.data.Host;

public interface JobInterface {

	Duration getDuration();

	void checkIdleStatus();

	int getJobNumber();

	ArrayList<Host> getList();

	void addHost(Host host);

	void addState(StateAbstract state);

	String getMessages();

	String getJobName();

	String getJobOwner();

	String getTargetQueue();

	String getStartHost();

	double getJobPriority();

	LocalDateTime getJobStartTime();

	LocalDateTime getJobSubmissionTime();

	String getIdentifier();

	String getJobState();

}