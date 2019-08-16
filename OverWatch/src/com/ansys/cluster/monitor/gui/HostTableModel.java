/**
 * 
 */
package com.ansys.cluster.monitor.gui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.data.HostQueue;

/**
 * @author rmartine
 *
 */
public class HostTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2331629876169607226L;
	private ArrayList<Host> valueList;
	private String[] columnNames = { "Name", "Load", "Aval. Mem", "Aval. Res", "Tot. Res", "Core(s)", "State" };
	private static final int COLUMN_NAME = 0;
	private static final int COLUMN_LOAD = 1;
	private static final int COLUMN_AVAL_MEM = 2;
	private static final int COLUMN_AVAL_RES = 3;
	private static final int COLUMN_TOT_RES = 4;
	private static final int COLUMN_CORES = 5;
	private static final int COLUMN_STATE = 6;
	private int columnCount = 0;

	/**
	 * 
	 */
	public HostTableModel(HostQueue queue) {
		this(new ArrayList<Host>(queue.getHosts().values()));
	}

	public HostTableModel(HostQueue queue, int column) {
		this(new ArrayList<Host>(queue.getHosts().values()), column);
	}

	public HostTableModel(ArrayList<Host> list) {
		setValueList(list);
		setColumnCount(columnNames.length);
	}

	public HostTableModel(ArrayList<Host> list, int column) {
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
		case COLUMN_LOAD:
		case COLUMN_AVAL_MEM:
		case COLUMN_AVAL_RES:
		case COLUMN_STATE:
			return String.class;
		case COLUMN_TOT_RES:
		case COLUMN_CORES:
			return Integer.class;

		default:
			throw new IllegalArgumentException("Invalid column: " + column);
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub

		Host host = (Host) valueList.get(rowIndex);
		Object returnValue = null;

		switch (columnIndex) {
		case COLUMN_NAME:
			returnValue = host.getName();
			break;
		case COLUMN_LOAD:
			returnValue = host.getFormattedLoad();
			break;
		case COLUMN_AVAL_MEM:
			returnValue = host.getFormattedAvalMem();
			break;
		case COLUMN_CORES:
			returnValue = host.getM_Core();
			break;
		case COLUMN_STATE:
			returnValue = host.getState();
			break;
		case COLUMN_AVAL_RES:
			returnValue = host.getSlotUnused();
			break;
		case COLUMN_TOT_RES:
			returnValue = host.getSlotTotal();
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

	public ArrayList<Host> getValueList() {
		return valueList;
	}

	public void setValueList(ArrayList<Host> valueList) {
		this.valueList = valueList;
	}

}
