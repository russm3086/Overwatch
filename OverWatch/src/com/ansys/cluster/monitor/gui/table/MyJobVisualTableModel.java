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
public class MyJobVisualTableModel extends AbstractClusterNodeTableModel {
	/**
		 * 
		 */
	private static final long serialVersionUID = 2331629876169607226L;
	protected static String[] jobColumnNames = { "Name", "Job ID", "Owner", "Cores", "State", "Duration", "Host",
			"TTL" };
	public static final int COLUMN_NAME = 0;
	public static final int COLUMN_ID = 1;
	public static final int COLUMN_OWNER = 2;
	public static final int COLUMN_CORES = 3;
	public static final int COLUMN_STATE = 4;
	public static final int COLUMN_DURATION = 5;
	public static final int COLUMN_HOST = 6;
	public static final int COLUMN_TTL = 7;

	/**
	 * 
	 */
	public MyJobVisualTableModel(JobsQueue queue) {
		this(new ArrayList<Job>(queue.getActiveJobs().values()), jobColumnNames.length);
	}

	public MyJobVisualTableModel(JobsQueue queue, int column) {
		this(new ArrayList<Job>(queue.getActiveJobs().values()), column);
	}

	public MyJobVisualTableModel(ArrayList<?> list, int column) {
		super(list, jobColumnNames, column);
	}

	public MyJobVisualTableModel(ArrayList<?> list) {
		super(list, jobColumnNames);
	}

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case COLUMN_NAME:
		case COLUMN_OWNER:
		case COLUMN_STATE:
		case COLUMN_HOST:
		case COLUMN_DURATION:
		case COLUMN_TTL:
			return String.class;
		case COLUMN_ID:
		case COLUMN_CORES:
			return Integer.class;
		default:
			throw new IllegalArgumentException("Invalid column: " + column);
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

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
			returnValue = durationOutput(job.getDuration());
			break;
		case COLUMN_HOST:
			returnValue = job.getStartHost();
			break;
		case COLUMN_TTL:
			returnValue = ttl(job.getJobSoftStopTime());
			break;

		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

}
