/**
 * 
 */
package com.ansys.cluster.monitor.data.interfaces;


/**
 * @author rmartine
 *
 */
public interface AnsQueueInterface {

	public String getMembersType();

	public int size();

	boolean containsKey(String node);
	
}
