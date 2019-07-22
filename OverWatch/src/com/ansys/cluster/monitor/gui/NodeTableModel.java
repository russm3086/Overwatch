/**
 * 
 */
package com.ansys.cluster.monitor.gui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.ansys.cluster.monitor.data.Job;
import com.ansys.cluster.monitor.data.JobsQueue;

/**
 * @author rmartine
 *
 */
public class NodeTableModel extends AbstractTableModel {
	/**
		 * 
		 */
	private static final long serialVersionUID = 2331629876169607226L;
	private ArrayList<Job> valueList;
	private String[] columnNames = { "Name", "Job ID", "Owner", "Cores", "State", "Duration (hrs)", "Target Queue" };
	private static final int COLUMN_NAME = 0;
	private static final int COLUMN_ID = 1;
	private static final int COLUMN_OWNER = 2;
	private static final int COLUMN_CORES = 3;
	private static final int COLUMN_STATE = 4;
	private static final int COLUMN_DURATION = 5;
	private static final int COLUMN_TARGET_QUEUE = 6;
	private int columnCount = 0;

	/**
	 * 
	 */
	public NodeTableModel(JobsQueue queue) {
		// TODO Auto-generated constructor stub
		ArrayList<Job> list = new ArrayList<Job>(queue.getJobs().values());
		setValueList(list);
		setColumnCount(columnNames.length);
	}

	public NodeTableModel(JobsQueue queue, int column) {
		// TODO Auto-generated constructor stub
		ArrayList<Job> list = new ArrayList<Job>(queue.getJobs().values());
		setValueList(list);
		setColumnCount(column);
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return valueList.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnCount;
	}

	public String getColumnName(int col) {
		return columnNames[col].toString();
	}

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case COLUMN_NAME:
		case COLUMN_OWNER:
		case COLUMN_STATE:
		case COLUMN_TARGET_QUEUE:
			return String.class;
		case COLUMN_ID:
		case COLUMN_CORES:
			return Integer.class;
		case COLUMN_DURATION:
			return Long.class;
		default:
			throw new IllegalArgumentException("Invalid column: " + column);
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub

		Job node = (Job) valueList.get(rowIndex);
		Object returnValue = null;

		switch (columnIndex) {
		case COLUMN_NAME:
			returnValue = node.getName();
			break;
		case COLUMN_ID:
			returnValue = node.getJobNumber();
			break;
		case COLUMN_OWNER:
			returnValue = node.getJobOwner();
			break;
		case COLUMN_CORES:
			returnValue = Integer.valueOf((node.getSlots()));
			break;
		case COLUMN_STATE:
			returnValue = node.getState();
			break;
		case COLUMN_DURATION:
			returnValue = Long.valueOf(node.getDuration().toHours());
			break;
		case COLUMN_TARGET_QUEUE:
			returnValue = node.getTargetQueue();
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

	public ArrayList<Job> getValueList() {
		return valueList;
	}

	public void setValueList(ArrayList<Job> valueList) {
		this.valueList = valueList;
	}

}
