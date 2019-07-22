/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;

/**
 * @author rmartine
 *
 */
public class JobsQueue extends AnsQueueAbstract {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6349369496228918137L;
	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);
	private SortedMap<String, Job> jobs = new TreeMap<String, Job>();

	public JobsQueue(String name) {
		super(name, SGE_DataConst.clusterTypeJob);
		// TODO Auto-generated constructor stub
	}

	public JobsQueue(Job job) {

		super(job);
		addJob(job);
	}

	public void addJob(Job job) {
		logger.entering(sourceClass, "addJob", job);

		jobs.put(String.valueOf(job.getJobNumber()), job);

		addSlotTotal(job.getSlots());

		logger.finest("Adding " + job + " to queue " + getQueueName());
	}

	/**
	 * @return the jobs
	 */
	public SortedMap<String, Job> getJobs() {
		return jobs;
	}

	/**
	 * @param jobs the jobs to set
	 */
	public void setJobs(SortedMap<String, Job> jobs) {
		this.jobs = jobs;
	}

	public String getSummary() {
		StringBuilder summary = new StringBuilder();
		summary.append("Queue Name: \t" + getName() + "\n");
		summary.append("\n# of Jobs: \t" + jobs.size());

		return summary.toString();
	}

	public String getStatus() {

		String status = "# of jobs: " + jobs.size() + "  Used Slots: " + getSlotUsed();
		return status;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return jobs.size();
	}

	@Override
	public boolean containsKey(String queueName) {
		// TODO Auto-generated method stub
		return jobs.containsKey(queueName);
	}
	public SortedMap<String, ClusterNodeAbstract> getNodes() {
		// TODO Auto-generated method stub
		
		SortedMap<String, ClusterNodeAbstract> map = new TreeMap<String, ClusterNodeAbstract>();
		map.putAll(jobs);
		
		return map;
	}

}
