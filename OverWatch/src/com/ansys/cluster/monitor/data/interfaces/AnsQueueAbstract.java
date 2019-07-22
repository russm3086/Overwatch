/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data.interfaces;

import java.util.SortedMap;
import java.util.logging.Logger;

import javax.swing.JTable;

import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.ansys.cluster.monitor.data.state.AnsQueueState;

/**
 * 
 * @author rmartine
 * @since
 */
public class AnsQueueAbstract extends ClusterNodeAbstract implements AnsQueueInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -364220974815470506L;

	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);
	protected double total_np_load = 0;
	protected double np_load = 0;
	private int nodesAvailable = 0;
	private double freeMem = 0;
	private double totalMem = 0;
	private String membersType;
	private JTable table;

	public AnsQueueAbstract(ClusterNodeAbstract node) {
		this(node.getQueueName(), node.getClusterType());
	}

	public AnsQueueAbstract(String name, String membersType) {
		setName(name);
		setMembersType(membersType);
		setup();
	}

	private void setup() {
		setClusterType(SGE_DataConst.clusterTypeQueue);
		addState(AnsQueueState.Normal);
	}

	public void addAvailableNodes() {
		nodesAvailable += 1;
	}

	public void addFreeMem(double freeMem) {
		setFreeMem(getFreeMem() + freeMem);
	}

	protected void addQueue(AnsQueueAbstract queue) {

		switch (queue.getMembersType()) {
		case SGE_DataConst.clusterTypeJob:
			addSlotTotal(queue.getSlotTotal());
			break;

		case SGE_DataConst.clusterTypeHost:
			addSlotTotal(queue.getSlotTotal());
			addSlotRes(queue.getSlotRes());
			addSlotUsed(queue.getSlotUsed());
			addNp_load(queue.getNp_Load());
			addFreeMem(queue.getFreeMem());

			break;
		}

	}

	public void addNp_load(double np_load) {

		setNp_Load(getNp_Load() + np_load);
		this.total_np_load += np_load;
	}

	/**
	 * @param freeSlots the freeSlots to set
	 */
	public void addSlotAvailable(int slotAvailable) {
		this.slotAvailable += slotAvailable;
	}

	/**
	 * @param slotRes the slotRes to set
	 */
	public void addSlotRes(int slotRes) {
		setSlotRes(getSlotRes() + slotRes);
	}

	/**
	 * @param slotTotal the slotTotal to set
	 */
	public void addSlotTotal(int slotTotal) {
		setSlotTotal(getSlotTotal() + slotTotal);
	}

	/**
	 * @param slotUsed the slotUsed to set
	 */
	public void addSlotUsed(int slotUsed) {
		setSlotUsed(getSlotUsed() + slotUsed);
	}

	@Override
	public void addState(StateAbstract state) {
		addState(state, AnsQueueState.Normal);
	}

	public void addTotalMem(double totalMem) {
		setTotalMem(getTotalMem() + totalMem);
	}

	public int getAvailableNodes() {
		return nodesAvailable;
	}

	public double getFreeMem() {
		return freeMem;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	public String getMembersType() {
		return membersType;
	}

	/**
	 * @return the nodesAvailable
	 */
	public int getNodesAvailable() {
		return nodesAvailable;
	}

	/**
	 * @return the np_load
	 */
	public double getNp_Load() {
		return np_load;
	}

	/**
	 * @return the total_np_load
	 */
	public double getTotal_np_load() {
		return total_np_load;
	}

	public double getTotalMem() {
		return totalMem;
	}

	/**
	 * @param freeMem the freeMem to set
	 */
	public void setFreeMem(double freeMem) {
		this.freeMem = freeMem;
	}

	public void setMembersType(String membersType) {
			this.membersType = membersType;
	}

	/**
	 * @param nodesAvailable the nodesAvailable to set
	 */
	public void setNodesAvailable(int nodesAvailable) {
		this.nodesAvailable = nodesAvailable;
	}

	/**
	 * @param np_load the np_load to set
	 */
	public void setNp_Load(double np_load) {
		this.np_load = np_load;
	}

	/**
	 * @param total_np_load the total_np_load to set
	 */
	public void setTotal_np_load(double total_np_load) {
		this.total_np_load = total_np_load;
	}

	/**
	 * @param totalMem the totalMem to set
	 */
	public void setTotalMem(double totalMem) {
		this.totalMem = totalMem;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public String getQueueName() {
		return getName();
	}

	public String toString() {
		return getName();
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsKey(String node) {
		// TODO Auto-generated method stub
		return false;
	}

	public SortedMap<String, ClusterNodeAbstract> getNodes() {
		// TODO Auto-generated method stub
		return null;
	}

}
