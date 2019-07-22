/**
 * 
 */
package com.ansys.cluster.monitor.data.interfaces;

/**
 * @author rmartine
 *
 */
public interface AnsQueueInterfaceOld {

	public String getMembersType();

	public int size();

	public boolean containsKey(String queueName);

	public AnsQueueAbstract get(String queueName);

	public void put(String queueName, AnsQueueAbstract queue);

}
