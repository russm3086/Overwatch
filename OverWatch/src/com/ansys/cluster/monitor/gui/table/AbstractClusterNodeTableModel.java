/**
 * 
 */
package com.ansys.cluster.monitor.gui.table;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * @author rmartine
 *
 */
public abstract class AbstractClusterNodeTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6408863277337232288L;
	/**
		 * 
		 */
	protected ArrayList<?> valueList;
	protected String[] columnNames;
	private int columnCount = 0;

	/**
	 * 
	 */
	

	public AbstractClusterNodeTableModel(ArrayList<?> list, String[] columnNames) {
		setValueList(list);
		setColumnNames(columnNames);
		setColumnCount(columnNames.length);
	}

	public AbstractClusterNodeTableModel(ArrayList<?> list, String[] columnNames, int column) {
		setValueList(list);
		setColumnNames(columnNames);
		setColumnCount(column);
	}

	public abstract Class<?> getColumnClass(int column);
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return valueList.size();
	}

	public String getColumnName(int col) {
		return columnNames[col].toString();
	}

	public ArrayList<?> getValueList() {
		return valueList;
	}

	public void setValueList(ArrayList<?> valueList) {
		this.valueList = valueList;
	}

	/**
	 * @return the columnCount
	 */
	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	/**
	 * @return the columnNames
	 */
	public String[] getColumnNames() {
		return columnNames;
	}

	/**
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

}
