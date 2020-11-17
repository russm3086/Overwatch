package com.ansys.cluster.monitor.gui.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableBuilder {
	public static final String table_Job_Host = "JOB_TABLE_HOST";
	public static final String table_Job_Pending = "JOB_TABLE_PENDING";
	public static final String table_Job_Visual = "JOB_TABLE_VISUAL";
	public static final String table_Job_Detail = "JOB_DETAIL_TABLE";
	public static final String table_Job = "JOB_TABLE";
	public static final String table_Host = "HOST_TABLE";
	public static final String table_State = "HOST_STATE_TABLE";
	public static final String table_JOB_MSG = "JOB_MSG_TABLE";
	public static final String table_FUN_MQ = "FUN_TABLE_MQ";

	public TableBuilder() {
		// TODO Auto-generated constructor stub
	}

	public static JTable buildTable(String tableModleName, AbstractTableModel tableModel) {
		JTable jtable = null;

		switch (tableModleName) {

		case table_Job_Host:
			jtable = buildTable(tableModel, 0, String.class);
			break;

		case table_Job:
		case table_Job_Pending:
		case table_Job_Visual:
		case table_Job_Detail:
			jtable = buildTable(tableModel, 1, Integer.class);
			break;

		case table_Host:
		case table_State:
			jtable = buildTable(tableModel, 0, String.class);
			break;

		case table_FUN_MQ:
			jtable = buildTable(tableModel, 0, String.class);

		case table_JOB_MSG:
			jtable = buildTable(tableModel, 0, Integer.class);
			break;
		}

		return jtable;

	}

	public static JTable buildTable(AbstractTableModel tableModel, int columnIndexToSort,
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

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(defaultRendererClass, centerRenderer);

		return table;
	}

}
