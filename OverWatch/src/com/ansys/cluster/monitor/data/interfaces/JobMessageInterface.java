package com.ansys.cluster.monitor.data.interfaces;

import java.util.LinkedHashSet;


public interface JobMessageInterface {

	LinkedHashSet<?> getJobList();

	int getMessageNumber();

	String getMessage();

}