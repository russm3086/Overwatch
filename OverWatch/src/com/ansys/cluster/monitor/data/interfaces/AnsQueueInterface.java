/**
 * 
 */
package com.ansys.cluster.monitor.data.interfaces;

import com.ansys.cluster.monitor.data.AnsQueue;

/**
 * @author rmartine
 *
 */
public interface AnsQueueInterface {

	public String getMembersType();

	public int size();

	public boolean containsKey(String queueName);

	public AnsQueue get(String queueName);

	public void put(String queueName, AnsQueue queue);

}
