package com.ansys.cluster.monitor.data.interfaces;

import java.util.ArrayList;

import com.ansys.cluster.monitor.data.Job;

public interface HostInterface {

	double getLoad();

	void setStatus();

	ArrayList<Job> getListJob();

	int JobCount();

	void addJob(Job job);

	void setListJob(ArrayList<Job> listJob);

	void addState(StateAbstract state);

	double getAvgLoad();

	int getSlotTotal();

	int getSlotReserved();

	int getSlotUsed();

	int getSlotUnused();

	String getMemTotal();

	double getMemTotalNum();

	String getMemUsedNumStr();

	double getMemUsedNum();

	double getMemFreeNum();

	String getMemFreeNumStr();

	int getM_Core();

	double getNp_load_avg();

	String getMetaData();

}