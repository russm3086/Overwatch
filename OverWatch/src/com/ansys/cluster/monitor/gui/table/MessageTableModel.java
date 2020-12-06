/**
 * 
 */
package com.ansys.cluster.monitor.gui.table;

import java.util.ArrayList;

import com.ansys.cluster.monitor.data.JobMessage;

/**
 * @author rmartine
 *
 */
public class MessageTableModel extends AbstractClusterNodeTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8238797483942066534L;
	protected static String[] mesColumnNames = { "Number", "Message" };
	private static final int COLUMN_NUMBER = 0;
	private static final int COLUMN_MESSAGE = 1;

	public MessageTableModel(ArrayList<?> list) {
		super(list, mesColumnNames);
		
	}

	/**
	 * @param list
	 * @param columnNames
	 */
	public MessageTableModel(ArrayList<?> list, String[] columnNames) {
		super(list, columnNames);
		
	}

	/**
	 * @param list
	 * @param columnNames
	 * @param column
	 */
	public MessageTableModel(ArrayList<?> list, String[] columnNames, int column) {
		super(list, columnNames, column);
		
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		JobMessage msg = (JobMessage) valueList.get(rowIndex);
		Object returnValue = null;

		switch (columnIndex) {
		case COLUMN_NUMBER:
			returnValue = msg.getMessageNumber();
			break;
		case COLUMN_MESSAGE:
			returnValue = msg.getMessage();
			break;

		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

	@Override
	public Class<?> getColumnClass(int column) {
		
		switch (column) {
		case COLUMN_NUMBER:
			return Integer.class;
		case COLUMN_MESSAGE:
			return String.class;
		default:
			throw new IllegalArgumentException("Invalid column: " + column);
		}
	}

}
