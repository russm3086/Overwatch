package com.ansys.cluster.monitor.data.interfaces;

import java.util.ArrayList;

import com.ansys.cluster.monitor.data.Job;

public interface HostInterface {

	double getLoad();

	void setStatus();

	String getSummary();

	ArrayList<Job> getListJob();

	int JobCount();

	void addJob(Job job);

	void setListJob(ArrayList<Job> listJob);

	void addState(StateAbstract state);

	double getAvgLoad();

	int getSlotTotal();

	int getSlotReserved();

	int getSlotUsed();

	String getMemTotal();

	double getMemTotalNum();

	String getMemUsedNumStr();

	double getMemUsedNum();

	double getMemFreeNum();

	String getMemFreeNumStr();

	int getMachineCore();

	double getNp_load_avg();

	String getMetaData();

}