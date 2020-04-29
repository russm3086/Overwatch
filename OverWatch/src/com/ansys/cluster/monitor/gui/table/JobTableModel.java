/**
 * 
 */
package com.ansys.cluster.monitor.gui.table;

import java.util.ArrayList;

import com.ansys.cluster.monitor.data.Job;
import com.ansys.cluster.monitor.data.JobsQueue;

/**
 * @author rmartine
 *
 */
public class JobTableModel extends AbstractClusterNodeTableModel {
	/**
		 * 
		 */
	private static final long serialVersionUID = 2331629876169607226L;
	protected static String[] jobColumnNames = { "Name", "Job ID", "Owner", "Cores", "State", "Duration (hrs)", "Load",
			"Efficiency" };
	private static final int COLUMN_NAME = 0;
	private static final int COLUMN_ID = 1;
	private static final int COLUMN_OWNER = 2;
	private static final int COLUMN_CORES = 3;
	private static final int COLUMN_STATE = 4;
	private static final int COLUMN_DURATION = 5;
	private static final int COLUMN_LOAD = 6;
	private static final int COLUMN_EFFICIENCY = 7;

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
			return String.class;
		case COLUMN_LOAD:
		case COLUMN_EFFICIENCY:
			return Double.class;
		case COLUMN_ID:
		case COLUMN_CORES:
		case COLUMN_DURATION:
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
		case COLUMN_STATE:
			returnValue = job.getState();
			break;
		case COLUMN_DURATION:
			returnValue = Long.valueOf(job.getDuration().toHours());
			break;
		case COLUMN_LOAD:
			returnValue = job.getHostLoad();
			break;
		case COLUMN_EFFICIENCY:
			returnValue = job.getEfficiency();
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

}
