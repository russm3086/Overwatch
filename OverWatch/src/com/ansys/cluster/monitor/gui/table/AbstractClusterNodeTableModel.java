/**
 * 
 */
package com.ansys.cluster.monitor.gui.table;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.ansys.cluster.monitor.main.SGE_DataConst;
import com.russ.util.TimeUtil;

/**
 * @author rmartine
 *
 */
public abstract class AbstractClusterNodeTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6408863277337232288L;
	/**
		 * 
		 */
	protected ArrayList<?> valueList;
	protected String[] columnNames;
	private int columnCount = 0;
	protected String durationFormat = "dd 'Day(s)' HH'h' mm'm'";

	/**
	 * 
	 */

	public AbstractClusterNodeTableModel(ArrayList<?> list, String[] columnNames) {
		setValueList(list);
		setColumnNames(columnNames);
		setColumnCount(columnNames.length);
	}

	public AbstractClusterNodeTableModel(ArrayList<?> list, String[] columnNames, int column) {
		setValueList(list);
		setColumnNames(columnNames);
		setColumnCount(column);
	}

	public abstract Class<?> getColumnClass(int column);

	@Override
	public int getRowCount() {

		return valueList.size();
	}

	public String getColumnName(int col) {
		return columnNames[col].toString();
	}

	public ArrayList<?> getValueList() {
		return valueList;
	}

	public void setValueList(ArrayList<?> valueList) {
		this.valueList = valueList;
	}

	public void setColumnNamesAndCount(String[] columnNames) {
		setColumnNames(columnNames);
		setColumnCount(columnNames.length);
	}

	/**
	 * @return the columnCount
	 */
	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	/**
	 * @return the columnNames
	 */
	public String[] getColumnNames() {
		return columnNames;
	}

	/**
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	protected String durationOutput(Duration duration) {
		return TimeUtil.formatDuration(duration.toMillis(), durationFormat);
	}

	protected String formatZonedDateTime(ZonedDateTime zonedDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SGE_DataConst.stopTimeFormat);
		return zonedDateTime.format(formatter);
	}

	protected String ttl(ZonedDateTime zonedDateTime) {
		ZonedDateTime currentTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
		ZonedDateTime finishTime = zonedDateTime;
		if (currentTime != null && finishTime != null) {

			Duration duration = Duration.between(currentTime, finishTime);
			return durationOutput(duration);
		}

		return "NaN";
	}
}
