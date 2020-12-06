/**
 * 
 */
package com.ansys.cluster.monitor.gui.table;

import java.util.ArrayList;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;

/**
 * @author rmartine
 *
 */
public class StateTableModel extends AbstractClusterNodeTableModel {
	/**
		 * 
		 */
	private static final long serialVersionUID = 2331629876169607226L;
	private static String[] stateColumnNames = { "Name", "Description" };
	private static final int COLUMN_NAME = 0;
	private static final int COLUMN_DESCRIPTION = 1;

	/**
	 * 
	 */
	public StateTableModel(ArrayList<?> list) {
		super(list, stateColumnNames);
	}

	public StateTableModel(ArrayList<?> list, int column) {
		super(list, stateColumnNames, column);
	}

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case COLUMN_NAME:
		case COLUMN_DESCRIPTION:
			return String.class;
		default:
			throw new IllegalArgumentException("Invalid column: " + column);
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		

		StateAbstract stateAbstract = (StateAbstract) valueList.get(rowIndex);
		Object returnValue = null;

		switch (columnIndex) {
		case COLUMN_NAME:
			returnValue = stateAbstract.getName();
			break;
		case COLUMN_DESCRIPTION:
			returnValue = stateAbstract.getDescription();
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

}
