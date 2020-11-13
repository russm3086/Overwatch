/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.io.Serializable;

/**
 * @author rmartine
 *
 */
public class Quota implements Serializable {

	private String quotaName;
	private String user;
	private String resource;
	private String queues;
	private int limit;
	private int usage;

	public Quota() {

	}

	/**
	 * @param quotaName
	 * @param user
	 * @param resource
	 * @param queues
	 * @param limit
	 * @param usage
	 */
	public Quota(String quotaName, String user, String resource, String queues, int limit, int usage) {
		this.quotaName = quotaName;
		this.user = user;
		this.resource = resource;
		this.queues = queues;
		this.limit = limit;
		this.usage = usage;
	}

	/**
	 * @return the quotaName
	 */
	public String getQuotaName() {
		return quotaName;
	}

	/**
	 * @param quotaName the quotaName to set
	 */
	public void setQuotaName(String quotaName) {
		this.quotaName = quotaName;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}

	/**
	 * @return the queues
	 */
	public String getQueues() {
		return queues;
	}

	/**
	 * @param queues the queues to set
	 */
	public void setQueues(String queues) {
		this.queues = queues;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the usage
	 */
	public int getUsage() {
		return usage;
	}

	/**
	 * @param usage the usage to set
	 */
	public void setUsage(int usage) {
		this.usage = usage;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5156214089882808898L;

}
