/**
 * 
 */
package com.ansys.cluster.monitor.data.interfaces;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.NodeProp;

/**
 * @author rmartine
 *
 */
public abstract class ClusterNodeAbstract implements ClusterNodeInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9178140942708008301L;
	private String sourceClass = this.getClass().getName();
	private Logger logger = Logger.getLogger(sourceClass);
	protected TreeMap<Integer, StateAbstract> store = new TreeMap<Integer, StateAbstract>((Collections.reverseOrder()));
	protected boolean boExclusive = false;
	protected String name;
	public NodeProp nodeProp;
	protected DecimalFormat decimalFormatter = new DecimalFormat(".##");
	protected String status;
	protected String clusterType;
	protected boolean isNodeAvailable = true;
	protected int slotRes = 0;
	protected int slotUsed = 0;
	protected int slotTotal = 0;
	protected int slotAvailable = 0;

	protected ClusterNodeAbstract() {

	}

	/**
	 * 
	 */
	public ClusterNodeAbstract(NodeProp nodeProp) {
		// TODO Auto-generated constructor stub
		this.nodeProp = nodeProp;

	}

	public String availableSlotsPercent() {

		float calc = (getSlotAvailable() * 100) / getSlotTotal();
		String result = decimalFormatter.format(calc) + "%";
		return result;
	}

	public boolean isExclusive() {
		return boExclusive;
	}

	public void setBoExclusive(boolean boExclusive) {
		if (isExclusive() != true) {
			this.boExclusive = boExclusive;
		}
	}

	public NodeProp getNodeProp() {
		// TODO Auto-generated method stub
		return nodeProp;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	public String getStateCode() {
		
		return nodeProp.getState();
	}
	
	public StateAbstract getState() {

		return store.firstEntry().getValue();
	}

	public TreeMap<Integer, StateAbstract> getStates() {

		return store;
	}

	protected void addState(TreeMap<Integer, StateAbstract> state, StateAbstract defaultState) {
		// TODO Auto-generated method stub
		for (Entry<Integer, StateAbstract> entry : state.entrySet()) {

			addState(entry.getValue(), defaultState);
		}
	}

	protected void addState(StateAbstract state, StateAbstract defaultState) {

		if ((defaultState != null) && (store.containsKey(defaultState.getValue()))) {
			store.remove(defaultState.getValue());
		}

		if (store.size() >= 1 && state.equals(defaultState))
			return;

		if (!store.containsKey(Integer.valueOf(state.value))) {

			logger.finest("Adding state" + state);
			store.put(Integer.valueOf(state.value), state);
		}
	}

	public String getStateDescription() {
		return getState().getDescription();
	}

	public String getStateNames() {
		StringBuffer desc = new StringBuffer();
		for (Entry<Integer, StateAbstract> sa : store.entrySet()) {
			desc.append("\t" + sa.getValue().getName());
		}
		return desc.toString();
	}

	public String getStateDescriptions() {
		StringBuffer desc = new StringBuffer();
		for (Entry<Integer, StateAbstract> sa : store.entrySet()) {
			desc.append("\n\t" + sa.getValue().getDescription());
		}
		return desc.toString();
	}

	public String getStatus() {
		return status;
	}

	/**
	 * @return the clusterType
	 */
	public String getClusterType() {
		return clusterType;
	}

	/**
	 * @param clusterType the clusterType to set
	 */
	public void setClusterType(String clusterType) {
		this.clusterType = clusterType;
	}

	public boolean hasState(StateAbstract state) {

		return store.containsKey(state.value);

	}

	/**
	 * @return the isNodeAvailable
	 */
	public boolean isNodeAvailable() {
		return isNodeAvailable;
	}

	/**
	 * @param isNodeAvailable the isNodeAvailable to set
	 */
	public void setNodeAvailable(boolean isNodeAvailable) {
		this.isNodeAvailable = isNodeAvailable;
	}

	public String getQueueName() {
		return nodeProp.getQueueName();
	}

	public String getIdentifier() {
		return getName();
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
		return slotRes;
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

	/**
	 * @param slotRes the slotRes to set
	 */
	public void setSlotRes(int slotRes) {
		this.slotRes = slotRes;
	}

	/**
	 * @param slotTotal the slotTotal to set
	 */
	public void setSlotTotal(int slotTotal) {
		this.slotTotal = slotTotal;
	}

	/**
	 * @param slotUsed the slotUsed to set
	 */
	public void setSlotUsed(int slotUsed) {
		this.slotUsed = slotUsed;
	}

	public String getMetaData() {
		StringBuffer output = new StringBuffer();

		output.append("\nName: " + getName());
		output.append("\nCluster Type: " + getClusterType());
		output.append("\nStates: " + getStateNames());
		output.append("\nExclusive: " + isExclusive());
		output.append("\nAvailable: " + isNodeAvailable());
		output.append("\nQueue: " + getQueueName());

		return output.toString();
	}

}
