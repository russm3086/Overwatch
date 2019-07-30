/**
 * 
 */
package com.ansys.cluster.monitor.data.interfaces;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.ansys.cluster.monitor.data.NodeProp;
import com.ansys.cluster.monitor.data.SGE_DataConst;

/**
 * @author rmartine
 *
 */
public abstract class ClusterNodeAbstract implements ClusterNodeInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9178140942708008301L;
	protected TreeMap<Integer, StateAbstract> store = new TreeMap<Integer, StateAbstract>((Collections.reverseOrder()));
	protected boolean boExclusive = false;
	protected String name;
	protected NodeProp nodeProp;
	protected DecimalFormat decimalFormatter = new DecimalFormat(".##");
	protected String status;
	protected String clusterType;
	protected boolean isNodeAvailable = true;
	protected boolean visualNode = false;
	protected int slotUnavailable = 0;
	protected String unitRes = SGE_DataConst.unitResCore;

	protected ClusterNodeAbstract() {

	}

	/**
	 * 
	 */
	public ClusterNodeAbstract(NodeProp nodeProp) {
		// TODO Auto-generated constructor stub
		this.nodeProp = nodeProp;

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
			desc.append(sa.getValue().getDescription());
			desc.append("\n\t\t");
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

	/**
	 * @return the slotUnavailable
	 */
	public int getSlotUnavailable() {
		return slotUnavailable;
	}

	/**
	 * @param slotUnavailable the slotUnavailable to set
	 */
	public void setSlotUnavailable(int slotUnavailable) {
		this.slotUnavailable = slotUnavailable;
	}

	/**
	 * @param slotUnavailable the slotUnavailable to set
	 */
	public void addSlotUnavailable(int slotUnavailable) {
		setSlotUnavailable(getSlotUnavailable() + slotUnavailable);
	}

	public String getQueueName() {
		return nodeProp.getQueueName();
	}

	public void setQueueName(String name) {
		nodeProp.setQueueName(name);
	}

	public String getIdentifier() {
		return getName();
	}

	/**
	 * @return the visualNode
	 */
	public boolean isVisualNode() {
		return visualNode;
	}

	/**
	 * @param visualNode the visualNode to set
	 */
	public void setVisualNode(boolean visualNode) {
		this.visualNode = visualNode;

		if (visualNode == true) {
			setUnitRes(SGE_DataConst.unitResSession);
		}
	}

	/**
	 * @return the unitRes
	 */
	public String getUnitRes() {
		return unitRes;
	}

	/**
	 * @param unitRes the unitRes to set
	 */
	public void setUnitRes(String unitRes) {
		this.unitRes = unitRes;
	}

	public String toString() {
		return getName();
	}

	/**
	 * 
	 */
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
