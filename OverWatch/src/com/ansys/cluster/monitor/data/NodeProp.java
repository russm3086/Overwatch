/**
 * 
 */
package com.ansys.cluster.monitor.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.HostPropInterface;
import com.ansys.cluster.monitor.data.interfaces.JobPropInterface;
import com.russ.util.AbstractProp;

/**
 * @author rmartine
 *
 */
public class NodeProp extends AbstractProp implements JobPropInterface, HostPropInterface {
	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */
	private static final long serialVersionUID = 4937849029618069333L;

	/**
	 * 
	 */
	public NodeProp() {
		logger.finer("Initializing using default settings");
	}

	/**
	 * @param defaults
	 */
	public NodeProp(Properties defaults) {
		super(defaults);
		logger.finer("Initializing using Properties");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getNp_load_avg()
	 */
	@Override
	public double getNp_load_avg() {

		return getDoubleProperty(SGE_DataConst.np_load_avg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setNp_load_avg(double)
	 */
	@Override
	public void setNp_load_avg(double np_load_avg) {

		setDoubleProperty(SGE_DataConst.np_load_avg, np_load_avg);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getNp_load_short()
	 */
	@Override
	public double getNp_load_short() {
		return getDoubleProperty(SGE_DataConst.np_load_short);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ansys.cluster.monitor.data.hostPropInterface#setNp_load_short(double)
	 */
	@Override
	public void setNp_load_short(double np_load_short) {

		setDoubleProperty(SGE_DataConst.np_load_short, np_load_short);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getNp_load_long()
	 */
	@Override
	public double getNp_load_long() {

		return getDoubleProperty(SGE_DataConst.np_load_long);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setNp_load_long(double)
	 */
	@Override
	public void setNp_load_long(double np_load_long) {

		setDoubleProperty(SGE_DataConst.np_load_long, np_load_long);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getHostname()
	 */
	@Override
	public String getHostname() {

		return getLogProperty(SGE_DataConst.hostname);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setHostname(java.lang.
	 * String)
	 */
	@Override
	public void setHostname(String hostname) {

		setLogProperty(SGE_DataConst.hostname, hostname);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getSlotType()
	 */
	@Override
	public String getSlotType() {

		return getLogProperty(SGE_DataConst.queueType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setSlotType(java.lang.
	 * String)
	 */
	@Override
	public void setSlotType(String queueType) {

		setLogProperty(SGE_DataConst.queueType, queueType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getSlotReserv()
	 */
	@Override
	public int getSlotReserved() {

		return getIntProperty(SGE_DataConst.slotRes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setSlotReserv(int)
	 */
	@Override
	public void setSlotReserved(int slotRes) {

		setIntProperty(SGE_DataConst.slotRes, slotRes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getSlotUsed()
	 */
	@Override
	public int getSlotUsed() {

		return getIntProperty(SGE_DataConst.slotUsed);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setSlotUsed(int)
	 */
	@Override
	public void setSlotUsed(int slotUsed) {
		setIntProperty(SGE_DataConst.slotUsed, slotUsed);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getSlotTotal()
	 */
	@Override
	public int getSlotTotal() {
		return getIntProperty(SGE_DataConst.slotTotal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setSlotTotal(int)
	 */
	@Override
	public void setSlotTotal(int slotTotal) {
		setIntProperty(SGE_DataConst.slotTotal, slotTotal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getArch()
	 */
	@Override
	public String getArch() {

		return getLogProperty(SGE_DataConst.arch);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ansys.cluster.monitor.data.hostPropInterface#setArch(java.lang.String)
	 */
	@Override
	public void setArch(String arch) {
		setLogProperty(SGE_DataConst.arch, arch);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getState()
	 */
	@Override
	public String getState() {
		return getLogProperty(SGE_DataConst.states);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ansys.cluster.monitor.data.hostPropInterface#setState(java.lang.String)
	 */
	@Override
	public void setState(String states) {
		setLogProperty(SGE_DataConst.states, states);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getMemTotal()
	 */
	@Override
	public String getMemTotal() {
		return getLogProperty(SGE_DataConst.memTotal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setMemTotal(java.lang.
	 * String)
	 */
	@Override
	public void setMemTotal(String memTotal) {
		setLogProperty(SGE_DataConst.memTotal, memTotal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getMemTotalNum()
	 */
	@Override
	public double getMemTotalNum() {
		return getDoubleProperty(SGE_DataConst.memTotalNum);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setMemTotalNum(double)
	 */
	@Override
	public void setMemTotalNum(double memTotal) {
		setDoubleProperty(SGE_DataConst.memTotalNum, memTotal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getMemUsed()
	 */
	@Override
	public String getMemUsed() {
		return getLogProperty(SGE_DataConst.memUsed);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ansys.cluster.monitor.data.hostPropInterface#setMemUsed(java.lang.String)
	 */
	@Override
	public void setMemUsed(String memUsed) {
		setLogProperty(SGE_DataConst.memUsed, memUsed);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getMemUsedNum()
	 */
	@Override
	public double getMemUsedNum() {
		return getDoubleProperty(SGE_DataConst.memUsedNum);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setMemUseNumd(double)
	 */
	@Override
	public void setMemUseNumd(double memUsed) {
		setDoubleProperty(SGE_DataConst.memUsedNum, memUsed);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getMemFree()
	 */
	@Override
	public String getMemFree() {
		return getLogProperty(SGE_DataConst.memFree);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ansys.cluster.monitor.data.hostPropInterface#setMemFree(java.lang.String)
	 */
	@Override
	public void setMemFree(String memFree) {
		setLogProperty(SGE_DataConst.memFree, memFree);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setMemFreeNum(double)
	 */
	@Override
	public void setMemFreeNum(double memFree) {
		setDoubleProperty(SGE_DataConst.memFreeNum, memFree);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getMemFreeNum()
	 */
	@Override
	public double getMemFreeNum() {

		return getDoubleProperty(SGE_DataConst.memFreeNum);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setNumProc(int)
	 */
	@Override
	public void setNumProc(int numProc) {

		setIntProperty(SGE_DataConst.numProc, numProc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getNumProc()
	 */
	@Override
	public int getNumProc() {
		return getIntProperty(SGE_DataConst.numProc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setM_Socket(int)
	 */
	@Override
	public void setM_Socket(int m_socket) {
		setIntProperty(SGE_DataConst.m_socket, m_socket);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getM_Socket()
	 */
	@Override
	public int getM_Socket() {
		return getIntProperty(SGE_DataConst.m_socket);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setM_Core(int)
	 */
	@Override
	public void setM_Core(int m_core) {
		setIntProperty(SGE_DataConst.m_core, m_core);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getM_Core()
	 */
	@Override
	public int getM_Core() {
		return getIntProperty(SGE_DataConst.m_core);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setM_Thread(int)
	 */
	@Override
	public void setM_Thread(int m_thread) {
		setIntProperty(SGE_DataConst.m_thread, m_thread);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getM_Thread()
	 */
	@Override
	public int getM_Thread() {
		return getIntProperty(SGE_DataConst.m_thread);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setSwapTotal(double)
	 */
	@Override
	public void setSwapTotal(double swap_total) {
		setDoubleProperty(SGE_DataConst.swap_total, swap_total);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getSwapTotal()
	 */
	@Override
	public double getSwapTotal() {
		return getDoubleProperty(SGE_DataConst.swap_total);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#setSwapUsed(double)
	 */
	@Override
	public void setSwapUsed(double swap_used) {
		setDoubleProperty(SGE_DataConst.swap_used, swap_used);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.hostPropInterface#getSwapUsed()
	 */
	@Override
	public double getSwapUsed() {
		return getDoubleProperty(SGE_DataConst.swap_used);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.JobPropInterface#getJobStateTranslated()
	 */
	@Override
	public String getJobStateTranslated() {
		return getLogProperty(SGE_DataConst.json_job_state_translated);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.JobPropInterface#getJobNumber()
	 */
	@Override
	public int getJobNumber() {
		return getIntProperty(SGE_DataConst.json_job_number);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.JobPropInterface#getJobPriority()
	 */
	@Override
	public double getJobPriority() {
		return getDoubleProperty(SGE_DataConst.json_job_priority);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.JobPropInterface#getJobName()
	 */
	@Override
	public String getJobName() {
		return getLogProperty(SGE_DataConst.json_job_name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.JobPropInterface#getJobOwner()
	 */
	@Override
	public String getJobOwner() {
		return getLogProperty(SGE_DataConst.json_job_owner);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.JobPropInterface#getJobState()
	 */
	@Override
	public String getJobState() {
		return getLogProperty(SGE_DataConst.json_job_state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.JobPropInterface#getJobStartTime()
	 */
	@Override
	public LocalDateTime getJobStartTime() {
		return getDateProperty(SGE_DataConst.json_job_start_time);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.JobPropInterface#getJobSubmissionTime()
	 */
	@Override
	public LocalDateTime getJobSubmissionTime() {
		// TODO Use Instant
		return getDateProperty(SGE_DataConst.json_job_submission_time);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.JobPropInterface#getJclassName()
	 */
	@Override
	public String getJclassName() {
		return getLogProperty(SGE_DataConst.json_job_jclass_name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.JobPropInterface#getSlots()
	 */
	@Override
	public int getSlots() {
		return getIntProperty(SGE_DataConst.json_slots);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.JobPropInterface#getStartHost()
	 */
	@Override
	public String getStartHost() {
		return getLogProperty(SGE_DataConst.json_job_start_host);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.JobPropInterface#setStartHost(java.lang.
	 * String)
	 */
	@Override
	public void setStartHost(String start_Host) {
		setLogProperty(SGE_DataConst.json_job_start_host, start_Host);
	}

	@Override
	public void setHostQueueName(String queueName) {
		// TODO Auto-generated method stub
		setLogProperty(SGE_DataConst.json_queue, queueName);
	}

	@Override
	public String getHostQueueName() {
		// TODO Auto-generated method stub
		return getLogProperty(SGE_DataConst.json_queue);
	}

	@Override
	public String getJobQueueName() {
		// TODO Auto-generated method stub
		return getLogProperty(SGE_DataConst.json_job_queue_name);
	}

	@Override
	public void setJobQueueName(String queue) {
		// TODO Auto-generated method stub
		setLogProperty(SGE_DataConst.json_job_queue_name, queue);
	}

	@Override
	public void setQueueName(String queueName) {
		// TODO Auto-generated method stub
		setHostQueueName(queueName);
	}

	@Override
	public String getQueueName() {
		// TODO Auto-generated method stub
		return getHostQueueName();
	}

	@Override
	public boolean isMyJob() {
		// TODO Auto-generated method stub
		return getBoolProperty(SGE_DataConst.job_MyJob);
	}

	@Override
	public void setMyJobTrue() {
		// TODO Auto-generated method stub
		setBoolProperty(SGE_DataConst.job_MyJob, true);
	}

	@Override
	public void setMyJobFalse() {
		// TODO Auto-generated method stub
		setBoolProperty(SGE_DataConst.job_MyJob, false);
	}

	@Override
	public void addJobMessage(JobMessage msg) {
		// TODO Auto-generated method stub
		ArrayList<JobMessage> jobMsg = getJobMessages();
		if (jobMsg != null) {

			jobMsg.add(msg);
		}else {
			
			ArrayList<JobMessage> list = new ArrayList<JobMessage>();
			list.add(msg);
			setJobMessages(list);
		}
	}

	@Override
	public void setJobMessages(ArrayList<JobMessage> list) {
		// TODO Auto-generated method stub
		setArrayList(SGE_DataConst.job_Messages, list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<JobMessage> getJobMessages() {
		// TODO Auto-generated method stub
		return (ArrayList<JobMessage>) getArrayList(SGE_DataConst.job_Messages);
	}

	@Override
	public double getJobIdleThreshold() {
		// TODO Auto-generated method stub	
		return getDoubleProperty(SGE_DataConst.job_IdleThreshold);
	}

	@Override
	public void setJobIdleThreshold(double value) {
		// TODO Auto-generated method stub
		setDoubleProperty(SGE_DataConst.job_IdleThreshold, value);
	}

	@Override
	public String getTargetQueue() {
		// TODO Auto-generated method stub
		return getLogProperty(SGE_DataConst.queueTarget);
	}

	@Override
	public void setTargetQueue(String queue) {
		// TODO Auto-generated method stub
		setLogProperty(SGE_DataConst.queueTarget, queue);
	}

}
