package com.ansys.cluster.monitor.data.interfaces;

import java.io.Serializable;
import java.util.LinkedHashSet;


public interface JobMessageInterface extends Serializable{

	LinkedHashSet<?> getJobList();

	int getMessageNumber();

	String getMessage();
	
	void setMessage(String msg);

}