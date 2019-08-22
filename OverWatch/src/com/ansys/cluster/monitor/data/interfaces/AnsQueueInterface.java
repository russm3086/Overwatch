/**
 * 
 */
package com.ansys.cluster.monitor.data.interfaces;

import java.util.SortedMap;

/**
 * @author rmartine
 *
 */
public interface AnsQueueInterface {

	public String getMembersType();

	public int size();

	boolean containsKey(String node);

	public SortedMap<Object, ClusterNodeAbstract> getNodes();

}
