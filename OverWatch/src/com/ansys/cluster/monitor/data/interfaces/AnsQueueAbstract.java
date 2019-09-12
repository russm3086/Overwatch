/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data.interfaces;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.data.Job;
import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.ansys.cluster.monitor.data.state.AnsQueueState;
import com.ansys.cluster.monitor.gui.table.TableBuilder;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoProp;

/**
 * 
 * @author rmartine
 * @since
 */
public abstract class AnsQueueAbstract extends ClusterNodeAbstract implements AnsQueueInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -364220974815470506L;
	// protected double total_np_load = 0;
	protected double np_load = 0;
	// private int nodesAvailable = 0;

	/**
	 * Memory
	 */
	private double unAvailableMem = 0;
	private double availableMem = 0;
	private double totalMem = 0;

	/**
	 * Session data
	 */
	private int sessionUsed = 0;
	private int sessionTotal = 0;
	private int sessionAvailable = 0;
	private int sessionUnavailable = 0;

	/**
	 * cores data
	 */

	/**
	 * 
	 */
	private int coreReserved = 0;
	private int coreUsed = 0;
	private int coreTotal = 0;
	private int coreAvailable = 0;
	private int coreUnavailable = 0;
	private int coreFUN = 0;

	/**
	 * 
	 */
	private String membersType;

	private ArrayList<Host> fullyUnallocatedComputeHosts = new ArrayList<Host>();
	private SortedMap<String, Host> availableComputeHosts = new TreeMap<String, Host>();
	private SortedMap<String, Host> unAvailableComputeHosts = new TreeMap<String, Host>();

	private SortedMap<String, Host> availableVisualHosts = new TreeMap<String, Host>();
	private SortedMap<String, Host> unAvailableVisualHosts = new TreeMap<String, Host>();

	private SortedMap<Integer, Job> activeJobs = new TreeMap<Integer, Job>();
	private SortedMap<Integer, Job> pendingJobs = new TreeMap<Integer, Job>();
	private SortedMap<Integer, Job> errorJobs = new TreeMap<Integer, Job>();
	private SortedMap<Integer, Job> idleJobs = new TreeMap<Integer, Job>();

	private SortedMap<Integer, Job> activeSessionJobs = new TreeMap<Integer, Job>();
	private SortedMap<Integer, Job> pendingSessionJobs = new TreeMap<Integer, Job>();
	private SortedMap<Integer, Job> errorSessionJobs = new TreeMap<Integer, Job>();

	public AnsQueueAbstract(ClusterNodeAbstract node) {
		this(node.getQueueName(), node.getClusterType());
		setVisualNode(node.isVisualNode());
	}

	public AnsQueueAbstract(String name, String membersType) {
		setName(name);
		setMembersType(membersType);
		setup();
	}

	public AnsQueueAbstract() {

	}

	private void setup() {
		setClusterType(SGE_DataConst.clusterTypeQueue);
		addState(AnsQueueState.Normal);
	}

	@Override
	public void addState(StateAbstract state) {
		addState(state, AnsQueueState.Normal);
	}

	public void addTotalMem(double totalMem) {
		setTotalMem(getTotalMem() + totalMem);
	}

	public String getMembersType() {
		return membersType;
	}

	/**
	 * @return the np_load
	 */
	public double getNp_Load() {
		return np_load;
	}

	public double getTotalMem() {
		return totalMem;
	}

	public void setMembersType(String membersType) {
		this.membersType = membersType;
	}

	/**
	 * @param np_load the np_load to set
	 */
	public void setNp_Load(double np_load) {
		this.np_load = np_load;
	}

	/**
	 * @param totalMem the totalMem to set
	 */
	public void setTotalMem(double totalMem) {
		this.totalMem = totalMem;
	}

	public String getQueueName() {
		return getName();
	}

	public String toString() {
		return getName();
	}

	/**
	 * @return the unAvailableMem
	 */
	public double getUnAvailableMem() {
		return unAvailableMem;
	}

	/**
	 * @param unAvailableMem the unAvailableMem to set
	 */
	public void setUnAvailableMem(double unAvailableMem) {
		this.unAvailableMem = unAvailableMem;
	}

	/**
	 * @return the availableMem
	 */
	public double getAvailableMem() {
		return availableMem;
	}

	/**
	 * @param availableMem the availableMem to set
	 */
	public void setAvailableMem(double availableMem) {
		this.availableMem = availableMem;
	}

	public void addAvailableMem(double availableMem) {
		setAvailableMem(getAvailableMem() + availableMem);
	}

	/**
	 * @return the coreReserved
	 */
	public int getCoreReserved() {
		return coreReserved;
	}

	/**
	 * @param coreReserved the coreReserved to set
	 */
	public void setCoreReserved(int coreReserved) {
		this.coreReserved = coreReserved;
	}

	public void addCoreReserved(int coreReserved) {
		setCoreReserved(getCoreReserved() + coreReserved);
	}

	/**
	 * @return the coreUsed
	 */
	public int getCoreUsed() {
		return coreUsed;
	}

	/**
	 * @param coreUsed the coreUsed to set
	 */
	public void setCoreUsed(int coreUsed) {
		this.coreUsed = coreUsed;
	}

	public void addCoreUsed(int coreUsed) {
		setCoreUsed(getCoreUsed() + coreUsed);
	}

	/**
	 * @return the coreTotal
	 */
	public int getCoreTotal() {
		return coreTotal;
	}

	/**
	 * @param coreTotal the coreTotal to set
	 */
	public void setCoreTotal(int coreTotal) {
		this.coreTotal = coreTotal;
	}

	public void addCoreTotal(int coreTotal) {
		setCoreTotal(getCoreTotal() + coreTotal);
	}

	/**
	 * @return the coreAvailable
	 */
	public int getCoreAvailable() {
		return coreAvailable;
	}

	/**
	 * @param coreAvailable the coreAvailable to set
	 */
	public void setCoreAvailable(int coreAvailable) {
		this.coreAvailable = coreAvailable;
	}

	/**
	 * @param coreAvailable the coreAvailable to set
	 */
	public void addCoreAvailable(int coreAvailable) {
		setCoreAvailable(getCoreAvailable() + coreAvailable);
	}

	/**
	 * @return the coreUnavailable
	 */
	public int getCoreUnavailable() {
		return coreUnavailable;
	}

	/**
	 * @param coreUnavailable the coreUnavailable to set
	 */
	public void setCoreUnavailable(int coreUnavailable) {
		this.coreUnavailable = coreUnavailable;
	}

	public void addCoreUnavailable(int coreUnavailable) {
		setCoreUnavailable(getCoreUnavailable() + coreUnavailable);
	}

	/**
	 * @return the sessionUsed
	 */
	public int getSessionUsed() {
		return sessionUsed;
	}

	/**
	 * @param sessionUsed the sessionUsed to set
	 */
	public void addSessionUsed(int sessionUsed) {
		this.sessionUsed += sessionUsed;
	}

	public void setSessionTotal(int sessionTotal) {
		this.sessionTotal = sessionTotal;
	}

	/**
	 * @return the sessionTotal
	 */
	public int getSessionTotal() {
		return sessionTotal;
	}

	/**
	 * @param sessionTotal the sessionTotal to set
	 */
	public void addSessionTotal(int sessionTotal) {
		setSessionTotal(getSessionTotal() + sessionTotal);
	}

	/**
	 * @param sessionTotal the sessionTotal to set
	 */
	public void addSessionTotal() {
		setSessionTotal(getSessionTotal() + 1);
	}

	/**
	 * @return the sessionAvailable
	 */
	public int getSessionAvailable() {
		return sessionAvailable;
	}

	/**
	 * @param sessionAvailable the sessionAvailable to set
	 */
	public void addSessionAvailable(int sessionAvailable) {
		setSessionAvailable(getSessionAvailable() + sessionAvailable);
	}

	/**
	 * @param sessionAvailable the sessionAvailable to set
	 */
	public void addSessionAvailable() {
		setSessionAvailable(getSessionAvailable() + 1);
	}

	/**
	 * @param sessionAvailable the sessionAvailable to set
	 */
	public void setSessionAvailable(int sessionAvailable) {
		this.sessionAvailable = sessionAvailable;
	}

	/**
	 * @return the sessionUnavailable
	 */
	public int getSessionUnavailable() {
		return sessionUnavailable;
	}

	/**
	 * @param sessionUnavailable the sessionUnavailable to set
	 */
	public void setSessionUnavailable(int sessionUnavailable) {
		this.sessionUnavailable = sessionUnavailable;
	}

	/**
	 * @param sessionUnavailable the sessionUnavailable to set
	 */
	public void addSessionUnavailable(int sessionUnavailable) {
		setSessionUnavailable(getSessionUnavailable() + sessionUnavailable);
	}

	/**
	 * @param sessionUnavailable the sessionUnavailable to set
	 */
	public void addSessionUnavailable() {
		setSessionUnavailable(getSessionUnavailable() + 1);
	}

	public void displayUnavailableVisualHosts(DetailedInfoProp mainDiProp) {
		displayUnavailableVisualHosts(mainDiProp, getUnavailableVisualHosts());
	}

	public void displayUnavailableVisualHosts(DetailedInfoProp mainDiProp, SortedMap<String, Host> map) {
		tableDisplay(mainDiProp, map, "Unavailable Visual host(s)", TableBuilder.table_Host);
	}

	public void displayUnavailableComputeHosts(DetailedInfoProp mainDiProp) {
		displayUnavailableComputeHosts(mainDiProp, getUnavailableComputeHosts());
	}

	public void displayUnavailableComputeHosts(DetailedInfoProp mainDiProp, SortedMap<String, Host> map) {
		tableDisplay(mainDiProp, map, "Unavailable Compute host(s)", TableBuilder.table_Host);
	}

	public void displayFullyUnallocatedNodes(DetailedInfoProp mainDiProp) {
		displayFullyUnallocatedNodes(mainDiProp, getFullyUnallocatedComputeHosts());
	}

	public void displayFullyUnallocatedNodes(DetailedInfoProp mainDiProp, ArrayList<Host> list) {
		tableDisplay(mainDiProp, list, "F.U.N. ", TableBuilder.table_FUN);
	}

	public void displayPendingJobs(DetailedInfoProp mainDiProp) {
		displayPendingJobs(mainDiProp, getPendingJobs());
	}

	public void displayPendingJobs(DetailedInfoProp mainDiProp, SortedMap<Integer, Job> map) {
		tableDisplay(mainDiProp, map, "Pending Jobs", TableBuilder.table_Job);
	}

	public void displayActiveJobs(DetailedInfoProp mainDiProp) {
		displayActiveJobs(mainDiProp, getActiveJobs());
	}

	public void displayActiveJobs(DetailedInfoProp mainDiProp, SortedMap<Integer, Job> map) {
		tableDisplay(mainDiProp, map, "Active Jobs", TableBuilder.table_Job);
	}

	public void displayErrorJobs(DetailedInfoProp mainDiProp) {
		displayErrorJobs(mainDiProp, getErrorJobs());
	}

	public void displayErrorJobs(DetailedInfoProp mainDiProp, SortedMap<Integer, Job> map) {
		tableDisplay(mainDiProp, map, "Error Jobs", TableBuilder.table_Job);
	}

	public void displayIdleJobs(DetailedInfoProp mainDiProp) {
		displayIdleJobs(mainDiProp, getIdleJobs());
	}

	public void displayIdleJobs(DetailedInfoProp mainDiProp, SortedMap<Integer, Job> map) {
		tableDisplay(mainDiProp, map, "Idle Jobs", TableBuilder.table_Job);
	}

	public void displayActiveSessionJobs(DetailedInfoProp mainDiProp) {
		displayActiveSessionJobs(mainDiProp, getActiveSessionJobs());
	}

	public void displayActiveSessionJobs(DetailedInfoProp mainDiProp, SortedMap<Integer, Job> map) {
		tableDisplay(mainDiProp, map, "Active Sessions", TableBuilder.table_Job);
	}

	public void displayErrorSessionJobs(DetailedInfoProp mainDiProp) {
		displayErrorSessionJobs(mainDiProp, getErrorSessionJobs());
	}

	public void displayErrorSessionJobs(DetailedInfoProp mainDiProp, SortedMap<Integer, Job> map) {
		tableDisplay(mainDiProp, map, "Error Sessions", TableBuilder.table_Job);
	}

	public void displayPendingSessionJobs(DetailedInfoProp mainDiProp) {
		displayPendingSessionJobs(mainDiProp, getPendingSessionJobs());
	}

	public void displayPendingSessionJobs(DetailedInfoProp mainDiProp, SortedMap<Integer, Job> map) {
		tableDisplay(mainDiProp, map, "Idle Sessions", TableBuilder.table_Job);
	}

	/**
	 * ******** Map Data
	 * 
	 * ******* Available Host
	 */

	/**
	 * 
	 * @param name
	 * @param host
	 */

	public void addAvailableVisualHosts(String name, Host host) {
		getAvailableVisualHosts().put(name, host);
	}

	/**
	 * 
	 * @param map
	 */
	public void addAvailableVisualHosts(SortedMap<String, Host> map) {
		getAvailableVisualHosts().putAll(map);
	}

	/**
	 * @param availableHost the availableHost to set
	 */
	public void setAvailableVisualHosts(SortedMap<String, Host> availableHosts) {
		this.availableVisualHosts = availableHosts;
	}

	/**
	 * @return the availableHost
	 */
	public SortedMap<String, Host> getAvailableVisualHosts() {
		return availableVisualHosts;
	}

	public int getAvailableVisualHostsSize() {
		return getAvailableVisualHosts().size();
	}

	/**
	 * Disabled Host
	 */
	/**
	 * @param disabledhosts the disabledhosts to set
	 */
	public void setUnavailableVisualHosts(SortedMap<String, Host> unAvailableVisualHosts) {
		this.unAvailableVisualHosts = unAvailableVisualHosts;
	}

	public void addUnvailableVisualHostsMap(SortedMap<String, Host> unAvailableVisualHosts) {
		getUnavailableVisualHosts().putAll(unAvailableVisualHosts);
	}

	public void addUnavailableVisualHosts(String strHost, Host host) {
		getUnavailableVisualHosts().put(strHost, host);
	}

	/**
	 * @return the disabledNode
	 */
	public SortedMap<String, Host> getUnavailableVisualHosts() {
		return unAvailableVisualHosts;
	}

	public int getUnavailableVisualHostsSize() {
		return getUnavailableVisualHosts().size();
	}

	public int getFullyUnallocatedComputeHostsSize() {
		return fullyUnallocatedComputeHosts.size();
	}

	public void addFullyUnallocatedComputeHosts(Host host) {
		fullyUnallocatedComputeHosts.add(host);
	}

	/**
	 * @return the fullyUnallocatedComputeHosts
	 */
	public ArrayList<Host> getFullyUnallocatedComputeHosts() {
		return fullyUnallocatedComputeHosts;
	}

	/**
	 * @param fullyUnallocatedComputeHosts the fullyUnallocatedComputeHosts to set
	 */
	public void setFullyUnallocatedComputeHosts(ArrayList<Host> fullyUnallocatedComputeHosts) {
		this.fullyUnallocatedComputeHosts = fullyUnallocatedComputeHosts;
	}

	public void addCoreFUN(int coreFun) {
		setCoreFUN(getCoreFUN() + coreFun);
	}

	/**
	 * @return the coreFUN
	 */
	public int getCoreFUN() {
		return coreFUN;
	}

	/**
	 * @param coreFUN the coreFUN to set
	 */
	public void setCoreFUN(int coreFUN) {
		this.coreFUN = coreFUN;
	}

	/**
	 * 
	 * @param name
	 * @param host
	 */

	public void addAvailableComputeHosts(String name, Host host) {
		getAvailableComputeHosts().put(name, host);
	}

	/**
	 * 
	 * @param map
	 */
	public void addAvailableComputeHosts(SortedMap<String, Host> map) {
		getAvailableComputeHosts().putAll(map);
	}

	/**
	 * @param availableHost the availableHost to set
	 */
	public void setAvailableComputeHosts(SortedMap<String, Host> availableHosts) {
		this.availableComputeHosts = availableHosts;
	}

	/**
	 * @return the availableHost
	 */
	public SortedMap<String, Host> getAvailableComputeHosts() {
		return availableComputeHosts;
	}

	public int getAvailableComputeHostsSize() {
		return getAvailableComputeHosts().size();
	}

	/**
	 * ******* Error Hosts
	 */

	/**
	 * Disabled Host
	 */
	/**
	 * @param disabledhosts the disabledhosts to set
	 */
	public void setUnavailableComputeHosts(SortedMap<String, Host> unAvailableComputeHosts) {
		this.unAvailableComputeHosts = unAvailableComputeHosts;
	}

	public void addUnavailableComputeHostsMap(SortedMap<String, Host> unAvailableComputeHosts) {
		getUnavailableComputeHosts().putAll(unAvailableComputeHosts);
	}

	public void addUnavailableComputeHosts(String strHost, Host host) {
		getUnavailableComputeHosts().put(strHost, host);
	}

	/**
	 * @return the disabledNode
	 */
	public SortedMap<String, Host> getUnavailableComputeHosts() {
		return unAvailableComputeHosts;
	}

	public int getUnavailableComputeHostsSize() {
		return getUnavailableComputeHosts().size();
	}

	/**
	 * ********Error Session JObs
	 */

	/**
	 * 
	 * @param jobId
	 * @param job
	 */
	public void addErrorSessionJobs(Integer jobId, Job job) {
		getErrorSessionJobs().put(jobId, job);
	}

	public void addErrorSessionJobsMap(SortedMap<Integer, Job> map) {
		getErrorSessionJobs().putAll(map);
	}

	public void setErrorSessionJobsMap(SortedMap<Integer, Job> map) {
		this.errorSessionJobs = map;
	}

	public SortedMap<Integer, Job> getErrorSessionJobs() {
		return errorSessionJobs;
	}

	public int getErrorSessionJobsSize() {
		return getErrorSessionJobs().size();
	}

	/**
	 * ********Pending Sessions Jobs
	 */

	/**
	 * 
	 * @param jobId
	 * @param job
	 */
	public void addPendingSessionJobs(Integer jobId, Job job) {
		getPendingSessionJobs().put(jobId, job);
	}

	public void addPendingSessionJobsMap(SortedMap<Integer, Job> map) {
		getPendingSessionJobs().putAll(map);
	}

	public void setPendingSessionJobsMap(SortedMap<Integer, Job> map) {
		this.pendingSessionJobs = map;
	}

	public SortedMap<Integer, Job> getPendingSessionJobs() {
		return pendingSessionJobs;
	}

	public int getPendingSessionJobsSize() {
		return getPendingSessionJobs().size();
	}

	/**
	 * ********Active Session Jobs
	 */

	/**
	 * 
	 * @param jobId
	 * @param job
	 */
	public void addActiveSessionJobs(Integer jobId, Job job) {
		getActiveSessionJobs().put(jobId, job);
	}

	public void addActiveSessionJobsMap(SortedMap<Integer, Job> map) {
		getActiveSessionJobs().putAll(map);
	}

	public void setActiveSessionJobs(SortedMap<Integer, Job> map) {
		this.activeSessionJobs = map;
	}

	public SortedMap<Integer, Job> getActiveSessionJobs() {
		return activeSessionJobs;
	}

	public int getActiveSessionJobsSize() {
		return getActiveSessionJobs().size();
	}

	/**
	 * ********Error Jobs
	 */

	public void addErrorJobs(Integer jobId, Job errorJob) {
		getErrorJobs().put(jobId, errorJob);
	}

	public void addErrorJobsMap(SortedMap<Integer, Job> errorJobs) {
		getErrorJobs().putAll(errorJobs);
	}

	/**
	 * @param errorJobs the errorJobs to set
	 */
	public void setErrorJobs(SortedMap<Integer, Job> errorJobs) {
		this.errorJobs = errorJobs;
	}

	/**
	 * @return the errorJobs
	 */
	public SortedMap<Integer, Job> getErrorJobs() {
		return errorJobs;
	}

	public int getErrorJobsSize() {
		return getErrorJobs().size();
	}

	/**
	 * *******Idle Job
	 */

	public void addIdleJobs(Integer jobId, Job job) {
		getIdleJobs().put(jobId, job);
	}

	public void addIdleJobsMap(SortedMap<Integer, Job> idleJobs) {
		getIdleJobs().putAll(idleJobs);
	}

	public void setIdleJobs(SortedMap<Integer, Job> idleJobs) {
		this.idleJobs = idleJobs;
	}

	/**
	 * @return the idleJobs
	 */
	public SortedMap<Integer, Job> getIdleJobs() {
		return idleJobs;
	}

	public int getIdleJobsSize() {
		return idleJobs.size();
	}

	/**
	 *********** Pending Job
	 */

	public void addPendingJobs(Integer jobId, Job pendingJob) {
		getPendingJobs().put(jobId, pendingJob);
	}

	public void addPendingJobsMap(SortedMap<Integer, Job> map) {
		getPendingJobs().putAll(map);
	}

	public void setPendingJobss(SortedMap<Integer, Job> map) {
		this.idleJobs = map;
	}

	/**
	 * 
	 * @return
	 */
	public SortedMap<Integer, Job> getPendingJobs() {
		return pendingJobs;
	}

	public int getPendingJobsSize() {
		return getPendingJobs().size();
	}

	/**
	 *********** Active Jobs
	 */

	/**
	 * @param activeJobs the activeJobs to set
	 */
	public void addActiveJobs(int jobID, Job activeJob) {
		getActiveJobs().put(jobID, activeJob);
	}

	public void addActiveJobsMap(SortedMap<Integer, Job> map) {
		getActiveJobs().putAll(map);
	}

	public void setActiveJobsMap(SortedMap<Integer, Job> map) {
		this.activeJobs = map;
	}

	/**
	 * @return the activeJobs
	 */
	public SortedMap<Integer, Job> getActiveJobs() {
		return activeJobs;
	}

	public int getActiveJobsSize() {
		return getActiveJobs().size();
	}

	public abstract int size();

	@Override
	public boolean containsKey(String node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SortedMap<Object, ClusterNodeAbstract> getNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	public DetailedInfoProp getDetailedInfoProp() {
		// TODO Auto-generated method stub
		return null;
	}

}
