/**
 * 
 */
package com.ansys.cluster.monitor.data.interfaces;

import java.io.Serializable;

import com.ansys.cluster.monitor.data.AnsQueue;

/**
 * @author rmartine
 *
 */
public interface AnsQueueInterface extends Serializable {

	public String getMembersType();

	public int size();

	public boolean containsKey(String queueName);

	public AnsQueue get(String queueName);

	public void put(String queueName, AnsQueue queue);

}
