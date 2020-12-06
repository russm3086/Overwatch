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
public class MyJobDetailTableModel extends AbstractClusterNodeTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7109959638697421888L;
	protected static String[] jobColumnNames = { "Name", "Job ID", "Owner", "Cores", "Num. of Exec. Host", "Start Host",
			"Queue", "Duration", "Load", "Efficiency", "TTL" };

	public static final int COLUMN_NAME = 0;
	public static final int COLUMN_ID = 1;
	public static final int COLUMN_OWNER = 2;
	public static final int COLUMN_CORES = 3;
	public static final int COLUMN_EXEC_HOSTS = 4;
	public static final int COLUMN_START_HOST = 5;
	public static final int COLUMN_QUEUE = 6;
	public static final int COLUMN_DURATION = 7;
	public static final int COLUMN_LOAD = 8;
	public static final int COLUMN_EFFICIENCY = 9;
	public static final int COLUMN_TTL = 10;

	/**
	 * @param queue
	 */
	public MyJobDetailTableModel(JobsQueue queue) {
		this(new ArrayList<Job>(queue.getActiveJobs().values()), jobColumnNames.length);
		
	}

	/**
	 * @param queue
	 * @param column
	 */
	public MyJobDetailTableModel(JobsQueue queue, int column) {
		this(new ArrayList<Job>(queue.getActiveJobs().values()), column);
		
	}

	/**
	 * @param list
	 * @param column
	 */
	public MyJobDetailTableModel(ArrayList<?> list, int column) {
		super(list, jobColumnNames, column);
		
	}

	/**
	 * @param list
	 */
	public MyJobDetailTableModel(ArrayList<?> list) {
		super(list, jobColumnNames);
		
	}

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case COLUMN_NAME:
		case COLUMN_OWNER:
		case COLUMN_DURATION:
		case COLUMN_START_HOST:
		case COLUMN_QUEUE:
		case COLUMN_TTL:
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
		case COLUMN_START_HOST:
			returnValue = job.getStartHost();
			break;
		case COLUMN_QUEUE:
			returnValue = job.getQueueName();
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
		case COLUMN_TTL:
			returnValue = ttl(job.getJobSoftStopTime());
			break;
			
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

}
