package com.ansys.cluster.monitor.data;

import java.util.SortedMap;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;

public interface MasterQueue {

	boolean containsKey(String queueName);
	
	public SortedMap<String, AnsQueueAbstract> getQueues() ;


}