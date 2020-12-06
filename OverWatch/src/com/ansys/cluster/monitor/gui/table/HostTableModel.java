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
public class HostTableModel extends AbstractClusterNodeTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2331629876169607226L;
	private static String[] hostColumnNames = { "Name", "Load", "Core(s)", "Aval. Mem", "Aval. Res", "Tot. Res",
			"State" };
	private static final int COLUMN_NAME = 0;
	private static final int COLUMN_LOAD = 1;
	private static final int COLUMN_CORES = 2;
	private static final int COLUMN_AVAL_MEM = 3;
	private static final int COLUMN_AVAL_RES = 4;
	private static final int COLUMN_TOT_RES = 5;
	private static final int COLUMN_STATE = 6;

	/**
	 * 
	 */
	public HostTableModel(HostQueue queue) {
		this(new ArrayList<Host>(queue.getHosts().values()), hostColumnNames.length);
	}

	public HostTableModel(HostQueue queue, int column) {
		this(new ArrayList<Host>(queue.getHosts().values()), column);
	}

	public HostTableModel(ArrayList<?> list) {
		super(list, hostColumnNames);
	}

	public HostTableModel(ArrayList<Host> list, int column) {
		super(list, hostColumnNames, column);
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
			returnValue = host.getSlotTotal() - host.getSlotUnavailable();
			break;
		case COLUMN_TOT_RES:
			returnValue = host.getSlotTotal();
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

}
