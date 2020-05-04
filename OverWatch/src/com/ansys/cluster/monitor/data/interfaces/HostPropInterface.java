package com.ansys.cluster.monitor.data.interfaces;

import java.io.Serializable;
import java.time.ZoneId;

public interface HostPropInterface extends Serializable{

	/**
	 * @return the np_load_avg
	 */
	double getNp_load_avg();

	/**
	 * @param np_load_avg
	 *            the np_load_avg to set
	 */
	void setNp_load_avg(double np_load_avg);

	/**
	 * @return the np_load_short
	 */
	double getNp_load_short();

	/**
	 * @param np_load_short
	 *            the np_load_short to set
	 */
	void setNp_load_short(double np_load_short);

	/**
	 * @return the np_load_long
	 */
	double getNp_load_long();

	/**
	 * @param np_load_long
	 *            the np_load_long to set
	 */
	void setNp_load_long(double np_load_long);

	/**
	 * @return the hostname
	 */
	String getHostname();

	/**
	 * @param hostname
	 *            the hostname to set
	 */
	void setHostname(String hostname);

	/**
	 * @return the queueType
	 */
	String getSlotType();

	/**
	 * @param queueType
	 *            the queueType to set
	 */
	void setSlotType(String queueType);

	/**
	 * @return the slotReserv
	 */
	int getSlotReserved();

	/**
	 * @param slotReserv
	 *            the slotReserv to set
	 */
	void setSlotReserved(int slotRes);

	/**
	 * @return the slotUsed
	 */
	int getSlotUsed();

	/**
	 * @param slotUsed
	 *            the slotUsed to set
	 */
	void setSlotUsed(int slotUsed);

	/**
	 * @return the slotTotal
	 */
	int getSlotTotal();

	/**
	 * @param slotTotal
	 *            the slotTotal to set
	 */
	void setSlotTotal(int slotTotal);

	/**
	 * @return the arch
	 */
	String getArch();

	/**
	 * @param arch
	 *            the arch to set
	 */
	void setArch(String arch);

	/**
	 * @return the state
	 */
	String getState();

	/**
	 * @param state
	 *            the state to set
	 */
	void setState(String states);

	/**
	 * @return the memTotal
	 */
	String getMemTotal();

	/**
	 * @param memTotal
	 *            the memTotal to set
	 */
	void setMemTotal(String memTotal);

	double getMemTotalNum();

	void setMemTotalNum(double memTotal);

	/**
	 * @return the memUsed
	 */
	String getMemUsed();

	/**
	 * @param memUsed
	 *            the memUsed to set
	 */
	void setMemUsed(String memUsed);

	/**
	 * @return the memUsed
	 */
	double getMemUsedNum();

	/**
	 * @param memUsed
	 *            the memUsed to set
	 */
	void setMemUseNumd(double memUsed);

	/**
	 * @return the memFree
	 */
	String getMemFree();

	/**
	 * @param memFree
	 *            the memFree to set
	 */
	void setMemFree(String memFree);

	void setMemFreeNum(double memFree);

	double getMemFreeNum();

	void setNumProc(int numProc);

	int getNumProc();

	void setM_Socket(int m_socket);

	int getM_Socket();

	void setM_Core(int m_core);

	int getM_Core();

	void setM_Thread(int m_thread);

	int getM_Thread();

	void setSwapTotal(double swap_total);

	double getSwapTotal();

	void setSwapUsed(double swap_used);

	double getSwapUsed();

	void setHostQueueName(String queueName);

	String getHostQueueName();
	
	public void setQueueName(String queueName);

	public String getQueueName();

	public ZoneId getZoneID();

	public void setZoneID(ZoneId zoneId);


}