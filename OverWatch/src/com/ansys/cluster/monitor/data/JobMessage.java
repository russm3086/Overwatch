/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.LinkedHashSet;

import com.ansys.cluster.monitor.data.interfaces.JobMessageInterface;

/**
 * @author rmartine
 *
 */
public class JobMessage implements JobMessageInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = -522097227511249732L;
	private NodeProp prop = new NodeProp();

	public JobMessage(NodeProp prop) {

		this.prop = prop;
	}

	/* (non-Javadoc)
	 * @see com.ansys.cluster.monitor.data.JobMessageInterface#getJobList()
	 */
	@Override
	public LinkedHashSet<?> getJobList() {
		
		LinkedHashSet<?> list = (LinkedHashSet<?>) prop.getLinkedHashSet("MES_job_number_list");
		
		return list;
	}


	/* (non-Javadoc)
	 * @see com.ansys.cluster.monitor.data.JobMessageInterface#getMessageNumber()
	 */
	@Override
	public int getMessageNumber() {

		return prop.getIntProperty("MES_message_number");
	}

	/* (non-Javadoc)
	 * @see com.ansys.cluster.monitor.data.JobMessageInterface#getMessage()
	 */
	@Override
	public String getMessage() {

		StringBuffer sb = new StringBuffer();

		sb.append(prop.get("MES_message_number"));
		sb.append(": ");
		sb.append(prop.get("MES_message"));
		
		return sb.toString();
	}

	public String toString() {
		
		return getMessage();
	}
	
}
