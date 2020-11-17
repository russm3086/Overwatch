/**
 * 
 */
package com.ansys.cluster.monitor.gui.table;

import java.util.ArrayList;

import com.ansys.cluster.monitor.data.Job;
import com.ansys.cluster.monitor.data.JobsQueue;
import com.russ.util.UnitConversion;

/**
 * @author rmartine
 *
 */
public class JobTableModel extends AbstractClusterNodeTableModel {
	/**
		 * 
		 */
	private static final long serialVersionUID = 2331629876169607226L;
	protected static String[] jobColumnNames = { "Name", "Job ID", "Owner", "Cores", "Num. of Exec. Host", "State",
			"Duration", "Load", "Efficiency" };
	protected static final int COLUMN_NAME = 0;
	protected static final int COLUMN_ID = 1;
	protected static final int COLUMN_OWNER = 2;
	protected static final int COLUMN_CORES = 3;
	protected static final int COLUMN_EXEC_HOSTS = 4;
	protected static final int COLUMN_STATE = 5;
	protected static final int COLUMN_DURATION = 6;
	protected static final int COLUMN_LOAD = 7;
	protected static final int COLUMN_EFFICIENCY = 8;

	/**
	 * 
	 */
	public JobTableModel(JobsQueue queue) {
		this(new ArrayList<Job>(queue.getActiveJobs().values()), jobColumnNames.length);
	}

	public JobTableModel(JobsQueue queue, int column) {
		this(new ArrayList<Job>(queue.getActiveJobs().values()), column);
	}

	public JobTableModel(ArrayList<?> list, int column) {
		super(list, jobColumnNames, column);
	}

	public JobTableModel(ArrayList<?> list) {
		super(list, jobColumnNames);
	}

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case COLUMN_NAME:
		case COLUMN_OWNER:
		case COLUMN_STATE:
		case COLUMN_DURATION:
			return String.class;
		case COLUMN_LOAD:
		case COLUMN_EFFICIENCY:
			return Double.class;
		case COLUMN_ID:
		case COLUMN_CORES:
		case COLUMN_EXEC_HOSTS:
			return Integer.class;
		default:
			throw new IllegalArgumentException("Invalid column: " + column);
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub

		Job job = (Job) valueList.get(rowIndex);
		Object returnValue = new String("?");

		switch (columnIndex) {
		case COLUMN_NAME:
			returnValue = job.getName();
			break;
		case COLUMN_ID:
			returnValue = job.getJobNumber();
			break;
		case COLUMN_OWNER:
			returnValue = job.getJobOwner();
			break;
		case COLUMN_CORES:
			returnValue = Integer.valueOf((job.getSlots()));
			break;
		case COLUMN_EXEC_HOSTS:
			returnValue = Integer.valueOf((job.getNumExecHosts()));
			break;
		case COLUMN_STATE:
			returnValue = job.getState();
			break;
		case COLUMN_DURATION:
			returnValue = durationOutput(job.getDuration());
			break;
		case COLUMN_LOAD:
			returnValue = job.getHostLoad();
			break;
		case COLUMN_EFFICIENCY:
			returnValue = UnitConversion.round(job.getEfficiency(), 1);
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

}
