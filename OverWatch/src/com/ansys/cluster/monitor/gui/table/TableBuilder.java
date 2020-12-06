package com.ansys.cluster.monitor.gui.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableBuilder {

	public enum TableModelType {
		JOB_TABLE_HOST, JOB_TABLE_PENDING, JOB_TABLE_VISUAL, JOB_TABLE, HOST_TABLE, HOST_STATE_TABLE, JOB_MSG_TABLE,
		FUN_TABLE_MQ, MY_JOB_DETAIL, MY_JOB_TABLE_VISUAL
	}

	public static final String table_Job_Host = "JOB_TABLE_HOST";
	public static final String table_Job_Pending = "JOB_TABLE_PENDING";
	public static final String table_Job_Visual = "JOB_TABLE_VISUAL";
	public static final String table_Job = "JOB_TABLE";
	public static final String table_Host = "HOST_TABLE";
	public static final String table_State = "HOST_STATE_TABLE";
	public static final String table_JOB_MSG = "JOB_MSG_TABLE";
	public static final String table_FUN_MQ = "FUN_TABLE_MQ";
	public static final String table_My_Job_Detail = "MY_JOB_DETAIL";
	public static final String table_My_Job_Visual = "MY_JOB_TABLE_VISUAL";

	public TableBuilder() {

	}

	public static JTable buildTable(String tableModelType, AbstractTableModel tableModel) {

		return buildTable(TableModelType.valueOf(tableModelType), tableModel);
	}

	public static JTable buildTable(TableModelType tableModelType, AbstractTableModel tableModel) {
		JTable jtable = null;

		switch (tableModelType) {

		case HOST_TABLE:
		case HOST_STATE_TABLE:
		case JOB_TABLE_HOST:
		case FUN_TABLE_MQ:
			jtable = buildTable(tableModelType, tableModel, 0, String.class);
			break;

		case JOB_TABLE:
		case JOB_TABLE_PENDING:
		case JOB_TABLE_VISUAL:
		case MY_JOB_TABLE_VISUAL:
		case JOB_MSG_TABLE:
			jtable = buildTable(tableModelType, tableModel, 1, Integer.class);
			break;

		case MY_JOB_DETAIL:
			jtable = buildTable(tableModelType, tableModel, 1, Object.class);
			break;
		}

		return jtable;

	}

	public static JTable buildTable(TableModelType tableModelType, AbstractTableModel tableModel, int columnIndexToSort,
			Class<?> defaultRendererClass) {

		JTable table = new JTable(tableModel);
		table.setAutoCreateRowSorter(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setFillsViewportHeight(true);

		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));

		sorter.setSortKeys(sortKeys);
		sorter.sort();

		TableCellRenderer renderer = getCellRenderer(tableModelType);
		table.setDefaultRenderer(String.class, renderer);
		table.setDefaultRenderer(Double.class, renderer);
		table.setDefaultRenderer(Integer.class, renderer);
		table.setDefaultRenderer(Object.class, renderer);

		return table;
	}

	public static TableCellRenderer getCellRenderer(TableModelType tableModelType) {

		switch (tableModelType) {

		case MY_JOB_DETAIL:
		case MY_JOB_TABLE_VISUAL:

			MyJobTableCellRenderer renderer = new MyJobTableCellRenderer();
			renderer.setHorizontalAlignment(JLabel.CENTER);
			return renderer;

		default:

			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			return centerRenderer;

		}
	}
}
