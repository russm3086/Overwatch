/**
 * 
 */
package com.ansys.cluster.monitor.gui;

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
	protected static String[] jobColumnNames = { "Name", "Job ID", "Owner", "Cores", "State", "Duration (hrs)", "Target Queue" };
	private static final int COLUMN_NAME = 0;
	private static final int COLUMN_ID = 1;
	private static final int COLUMN_OWNER = 2;
	private static final int COLUMN_CORES = 3;
	private static final int COLUMN_STATE = 4;
	private static final int COLUMN_DURATION = 5;
	private static final int COLUMN_TARGET_QUEUE = 6;

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
		super(list, jobColumnNames,column);
	}

	public JobTableModel(ArrayList<?> list) {
		super(list, jobColumnNames);
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

		Job job = (Job) valueList.get(rowIndex);
		Object returnValue = null;

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
		case COLUMN_TARGET_QUEUE:
			returnValue = job.getTargetQueue();
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

}
