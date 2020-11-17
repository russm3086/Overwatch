/**
 * 
 */
package com.ansys.cluster.monitor.data.interfaces;

import java.awt.Color;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

import com.ansys.cluster.monitor.data.NodeProp;
import com.ansys.cluster.monitor.gui.table.FUN_HostTableModel;
import com.ansys.cluster.monitor.gui.table.HostTableModel;
import com.ansys.cluster.monitor.gui.table.JobDetailTableModel;
import com.ansys.cluster.monitor.gui.table.JobHostTableModel;
import com.ansys.cluster.monitor.gui.table.JobPendingTableModel;
import com.ansys.cluster.monitor.gui.table.JobTableModel;
import com.ansys.cluster.monitor.gui.table.JobVisualTableModel;
import com.ansys.cluster.monitor.gui.table.MessageTableModel;
import com.ansys.cluster.monitor.gui.table.StateTableModel;
import com.ansys.cluster.monitor.gui.table.TableBuilder;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoFactory;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoProp;
import com.ansys.cluster.monitor.main.SGE_DataConst;

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
	// protected DecimalFormat decimalFormatter = new DecimalFormat(".##");
	protected NumberFormat numberFormmatter = NumberFormat.getInstance();
	protected DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	protected String durationFormat = "dd 'Day(s)' HH'h' mm'm'";
	protected String clusterType;
	protected boolean isNodeAvailable = true;
	protected boolean visualNode = false;
	protected int slotUnavailable = 0;
	protected String unitRes = SGE_DataConst.unitResCore;
	protected String strDetailedInfoPanel = DetailedInfoFactory.DetailedInfoPanel;
	protected double timeMultiple = 1000;

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

	protected void remove(StateAbstract state) {
		store.remove(Integer.valueOf(state.value), state);
	}

	public String getStateDescription() {
		return getState().getDescription();
	}

	public String getStateNames() {
		StringBuffer desc = new StringBuffer();
		for (Entry<Integer, StateAbstract> sa : store.entrySet()) {
			desc.append(sa.getValue().getName() + "\t");
		}
		return desc.toString();
	}

	public void displayStateDescriptions(DetailedInfoProp masterDiProp) {
		tableDisplay(masterDiProp, store, "States", TableBuilder.table_State);
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

	public ZoneId getZoneId() {
		return nodeProp.getZoneID();
	}

	public void setZoneId(ZoneId zoneId) {
		nodeProp.setZoneID(zoneId);
	}

	public String toString() {
		return getName();
	}

	protected String summaryOutput(String unit, Object field) {
		StringBuilder sb = new StringBuilder();
		sb.append(unit);
		sb.append(" ");
		sb.append(field);

		return sb.toString();
	}

	protected StringBuilder summaryOutput(String unit, Object field, Object value) {
		StringBuilder sb = new StringBuilder();
		sb.append(unit);
		sb.append(" ");
		sb.append(field);
		sb.append(" ");
		sb.append(value);

		return sb;
	}

	protected void textAreaDisplay(DetailedInfoProp masterDiProp, String content, String panelName) {
		if (content != null && content.length() > 0) {
			DetailedInfoProp diProp = new DetailedInfoProp();
			diProp.setPanelName(panelName);
			diProp.setDataTypeTextArea();
			diProp.addMetric(DetailedInfoProp.const_DataTypeTextArea, content);
			masterDiProp.addDetailedInfoProp(diProp);
		}
	}

	protected void createAvailableChartPanel(DetailedInfoProp masterDiProp, String panelName, String title, String unit,
			Number numAvail, Number numUnavail, Number numIdle) {

		DetailedInfoProp diProp = new DetailedInfoProp();
		diProp.setPanelName(panelName);
		diProp.setChartDataTitle(title);
		diProp.setChartDataUnit(unit);
		diProp.setDataTypePieChart();
		diProp.addChartData("Available", numAvail, new Color(0, 153, 0));
		diProp.addChartData("Unavailable", numUnavail, new Color(204, 0, 0));
		diProp.addChartData("Idle", numIdle, new Color(0, 0, 255));

		masterDiProp.addDetailedInfoProp(diProp);
	}

	protected void createAvailableChartPanel(DetailedInfoProp masterDiProp, String panelName, String title, String unit,
			Number numAvail, Number numUnavail) {

		DetailedInfoProp diProp = new DetailedInfoProp();
		diProp.setPanelName(panelName);
		diProp.setChartDataTitle(title);
		diProp.setChartDataUnit(unit);
		diProp.setDataTypePieChart();
		diProp.addChartData("Available", numAvail, new Color(0, 153, 0));
		diProp.addChartData("Unavailable", numUnavail, new Color(204, 0, 0));

		masterDiProp.addDetailedInfoProp(diProp);
	}

	protected void createAvailableBarChartPanel(DetailedInfoProp masterDiProp, String panelName, String title,
			String unit, SortedMap<String, AnsQueueAbstract> queueMaps) {

		DetailedInfoProp diProp = new DetailedInfoProp();
		diProp.setPanelName(panelName);
		diProp.setChartDataTitle(title);
		diProp.setChartDataUnit(unit);
		diProp.setDataTypeBarChart();

		for (Entry<String, AnsQueueAbstract> entry : queueMaps.entrySet()) {

			if (!entry.getValue().isVisualNode() && entry.getValue().getCoreFUN() > 0) {
				int funCores = entry.getValue().getCoreFUN();
				String queue = entry.getKey();
				diProp.addChartData(funCores, "FUN", queue);
			}
		}

		masterDiProp.addDetailedInfoProp(diProp);
	}

	protected void createPendingBarChartPanel(DetailedInfoProp masterDiProp, String panelName, String title,
			String unit, SortedMap<String, AnsQueueAbstract> queueMaps) {

		DetailedInfoProp diProp = new DetailedInfoProp();
		diProp.setPanelName(panelName);
		diProp.setChartDataTitle(title);
		diProp.setChartDataUnit(unit);
		diProp.setDataTypeBarChart();
		diProp.addChartDataPaint(new Color(224, 213, 0));

		for (Entry<String, AnsQueueAbstract> entry : queueMaps.entrySet()) {

			if (entry.getValue().getPendingJobsSize() > 0) {
				int pendingJobs = entry.getValue().getPendingJobsSize();
				String queueName = entry.getKey();
				diProp.addChartData(pendingJobs, "Pending", queueName);
			}
		}

		masterDiProp.addDetailedInfoProp(diProp);
	}

	protected void createPieChartPanel(DetailedInfoProp masterDiProp, String panelName, String title, String unit) {

		DetailedInfoProp diProp = new DetailedInfoProp();
		diProp.setPanelName(panelName);
		diProp.setChartDataTitle(title);
		diProp.setChartDataUnit(unit);
		diProp.setDataTypePieChart();

		masterDiProp.addDetailedInfoProp(diProp);
	}

	public void tableDisplay(DetailedInfoProp masterDiProp, SortedMap<?, ?> map, String panelName, String tableType) {

		ArrayList<?> list = new ArrayList<>(map.values());
		tableDisplay(masterDiProp, list, panelName, tableType);
	}

	public void tableDisplay(DetailedInfoProp masterDiProp, ArrayList<?> list, String panelName, String tableType) {
		if (list != null && list.size() > 0) {

			DetailedInfoProp diProp = new DetailedInfoProp();
			diProp.setPanelName(panelName + ": " + list.size());
			diProp.setDataTypeTable();
			AbstractTableModel tableModel = getTableModel(tableType, list);
			diProp.addMetric(tableType, tableModel);
			masterDiProp.addDetailedInfoProp(diProp);

		}
	}

	public AbstractTableModel getTableModel(String nodeType, ArrayList<?> list) {

		switch (nodeType) {

		case TableBuilder.table_Job_Host:
			JobHostTableModel jobHostTableModel = new JobHostTableModel(list);
			return jobHostTableModel;

		case TableBuilder.table_Job:
			JobTableModel jobTableModel = new JobTableModel(list);
			return jobTableModel;

		case TableBuilder.table_Job_Detail:
			JobDetailTableModel jobDetailTableModel = new JobDetailTableModel(list);
			return jobDetailTableModel;

		case TableBuilder.table_Job_Pending:
			JobPendingTableModel jobPendingTableModel = new JobPendingTableModel(list);
			return jobPendingTableModel;

		case TableBuilder.table_Job_Visual:
			JobVisualTableModel jobVisualTableModel = new JobVisualTableModel(list);
			return jobVisualTableModel;

		case TableBuilder.table_Host:
			HostTableModel hostTableModel = new HostTableModel(list);
			return hostTableModel;

		case TableBuilder.table_FUN_MQ:
			FUN_HostTableModel funHostTableModel = new FUN_HostTableModel(list);
			return funHostTableModel;

		case TableBuilder.table_State:
			StateTableModel stateTableModel = new StateTableModel(list);
			return stateTableModel;

		case TableBuilder.table_JOB_MSG:
			MessageTableModel msgTableModel = new MessageTableModel(list);
			return msgTableModel;

		default:
			return null;

		}

	}

	public String dateTimeFormatter(Temporal temporal) {
		String result = "";

		if (temporal != null) {
			result = dateFormatter.format(temporal);
		}
		return result;
	}

	/**
	 * @return the strDetailedInfoPanel
	 */
	public String getDetailedInfoPanel() {
		return strDetailedInfoPanel;
	}

	/**
	 * @param strDetailedInfoPanel the strDetailedInfoPanel to set
	 */
	public void setDetailedInfoPanel(String strDetailedInfoPanel) {
		this.strDetailedInfoPanel = strDetailedInfoPanel;
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
