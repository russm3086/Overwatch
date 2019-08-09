/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data.interfaces;

import java.util.SortedMap;

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
	protected double total_np_load = 0;
	protected double np_load = 0;
	private int nodesAvailable = 0;
	private double freeMem = 0;
	private double totalMem = 0;
	protected int sessionUsed = 0;
	protected int sessionTotal = 0;
	protected int sessionAvailable = 0;
	protected int sessionUnavailable = 0;
	protected int slotReserved = 0;
	protected int slotUsed = 0;
	protected int slotTotal = 0;
	protected int slotAvailable = 0;
	private String membersType;
	private JTable table;

	public AnsQueueAbstract(ClusterNodeAbstract node) {
		this(node.getQueueName(), node.getClusterType());
		setVisualNode(node.isVisualNode());
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

	public void addAvailableNodes(int nodes) {
		nodesAvailable += nodes;
	}

	public void addFreeMem(double freeMem) {
		setFreeMem(getFreeMem() + freeMem);
	}

	protected void calcQueue(AnsQueueAbstract queue) {

		switch (queue.getMembersType()) {
		case SGE_DataConst.clusterTypeJob:
			addSlotTotal(queue.getSlotTotal());
			break;

		case SGE_DataConst.clusterTypeHost:
			if (queue.isVisualNode()) {

				addSessionTotal(queue.getSessionTotal());
				addSessionAvailable(queue.getSessionAvailable());
				addSessionUsed(queue.getSessionUsed());
				addSessionUnavailable(queue.getSessionUnavailable());

			} else {

				addSlotTotal(queue.getSlotTotal());
				addSlotRes(queue.getSlotRes());
				addSlotUsed(queue.getSlotUsed());
				addSlotAvailable(queue.getSlotAvailable());
				addSlotUnavailable(queue.getSlotUnavailable());
				addTotalMem(queue.getTotalMem());
			}

			addNp_load(queue.getNp_Load());
			addFreeMem(queue.getFreeMem());
			addAvailableNodes(queue.getAvailableNodes());

			break;
		}

	}

	/**
	 * @return the freeSlots
	 */
	public int getSlotAvailable() {
		return slotAvailable;
	}

	/**
	 * @return the slotRes
	 */
	public int getSlotRes() {
		return slotReserved;
	}

	/**
	 * @return the slotTotal
	 */
	public int getSlotTotal() {
		return slotTotal;
	}

	/**
	 * @return the slotUsed
	 */
	public int getSlotUsed() {
		return slotUsed;
	}

	/**
	 * @param slotAvailable the slotAvailable to set
	 */
	public void setSlotAvailable(int slotAvailable) {
		this.slotAvailable = slotAvailable;
	}

	public String availableSlotsPercent() {
		String result = "0%";
		try {
			float calc = (getSlotAvailable() * 100) / getSlotTotal();
			result = decimalFormatter.format(calc) + "%";
		} catch (java.lang.ArithmeticException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return result;
	}

	public void addNp_load(double np_load) {

		setNp_Load(getNp_Load() + np_load);
		this.total_np_load += np_load;
	}

	/**
	 * @param freeSlots the freeSlots to set
	 */
	public void addSlotAvailable(int slotAvailable) {
		setSlotAvailable(getSlotAvailable() + slotAvailable);
	}

	/**
	 * @param slotRes the slotRes to set
	 */
	public void setSlotRes(int slotReserved) {
		this.slotReserved = slotReserved;
	}

	/**
	 * @param slotTotal the slotTotal to set
	 */
	public void setSlotTotal(int slotTotal) {
		this.slotTotal = slotTotal;
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
	public void setSlotUsed(int slotUsed) {
		this.slotUsed = slotUsed;
	}

	public void addSlotUsed(int slotUsed) {
		setSlotUsed(getSlotUsed() + slotUsed);
	}

	/**
	 * @param slotRes the slotRes to set
	 */
	public void addSlotRes(int slotRes) {
		setSlotRes(getSlotRes() + slotRes);
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
