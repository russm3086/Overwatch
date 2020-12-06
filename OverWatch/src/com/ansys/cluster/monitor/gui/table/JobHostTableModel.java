/**
 * 
 */
package com.ansys.cluster.monitor.gui.table;

import java.util.ArrayList;

import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.data.HostQueue;

/**
 * @author rmartine
 *
 */
public class JobHostTableModel extends AbstractClusterNodeTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2331629876169607226L;
	private static String[] hostColumnNames = { "Name", "Load", "Used Core(s)", "Used Mem", "Aval. Cores", "Aval. Mem",
			"State" };
	private static final int COLUMN_NAME = 0;
	private static final int COLUMN_LOAD = 1;
	private static final int COLUMN_CORES_USED = 2;
	private static final int COLUMN_MEM_USED =3;
	private static final int COLUMN_CORES_AVAL = 4;
	private static final int COLUMN_MEM_AVAL = 5;
	private static final int COLUMN_STATE = 6;

	/**
	 * 
	 */
	public JobHostTableModel(HostQueue queue) {
		this(new ArrayList<Host>(queue.getHosts().values()), hostColumnNames.length);
	}

	public JobHostTableModel(HostQueue queue, int column) {
		this(new ArrayList<Host>(queue.getHosts().values()), column);
	}

	public JobHostTableModel(ArrayList<?> list) {
		super(list, hostColumnNames);
	}

	public JobHostTableModel(ArrayList<Host> list, int column) {
		super(list, hostColumnNames, column);
	}

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case COLUMN_NAME:
		case COLUMN_LOAD:
		case COLUMN_MEM_AVAL:
		case COLUMN_MEM_USED:
		case COLUMN_STATE:
			return String.class;
		case COLUMN_CORES_USED:
		case COLUMN_CORES_AVAL:
			return Integer.class;

		default:
			throw new IllegalArgumentException("Invalid column: " + column);
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		

		Host host = (Host) valueList.get(rowIndex);
		Object returnValue = null;

		switch (columnIndex) {
		case COLUMN_NAME:
			returnValue = host.getName();
			break;
		case COLUMN_LOAD:
			returnValue = host.getFormattedLoad();
			break;
		case COLUMN_MEM_USED:
			returnValue = host.getMemUsedNum();
			break;
		case COLUMN_CORES_USED:
			returnValue = host.getSlotUsed();
			break;
		case COLUMN_STATE:
			returnValue = host.getState();
			break;
		case COLUMN_CORES_AVAL:
			returnValue = host.getSlotTotal() - host.getSlotUsed();
			break;
		case COLUMN_MEM_AVAL:
			returnValue = host.getFormattedAvalMem();
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

}
