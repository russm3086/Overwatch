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
public class JobDetailTableModel extends JobTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7109959638697421888L;
	protected static String[] jobColumnNames = { "Name", "Job ID", "Owner", "Cores", "Num. of Exec. Host", "Start Host",
			"Queue", "State", "Duration", "Load", "Efficiency" };

	protected static final int COLUMN_START_HOST = 5;
	protected static final int COLUMN_QUEUE = 6;
	protected static final int COLUMN_STATE = 7;
	protected static final int COLUMN_DURATION = 8;
	protected static final int COLUMN_LOAD = 9;
	protected static final int COLUMN_EFFICIENCY = 10;

	/**
	 * @param queue
	 */
	public JobDetailTableModel(JobsQueue queue) {
		super(queue);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param queue
	 * @param column
	 */
	public JobDetailTableModel(JobsQueue queue, int column) {
		super(queue, column);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param list
	 * @param column
	 */
	public JobDetailTableModel(ArrayList<?> list, int column) {
		super(list, column);
		setColumnNames(jobColumnNames);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param list
	 */
	public JobDetailTableModel(ArrayList<?> list) {
		super(list);
		setColumnNames(jobColumnNames);
		// TODO Auto-generated constructor stub
	}

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case COLUMN_NAME:
		case COLUMN_OWNER:
		case COLUMN_STATE:
		case COLUMN_DURATION:
		case COLUMN_START_HOST:
		case COLUMN_QUEUE:
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
		case COLUMN_START_HOST:
			returnValue = job.getStartHost();
			break;
		case COLUMN_QUEUE:
			returnValue = job.getQueueName();
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
