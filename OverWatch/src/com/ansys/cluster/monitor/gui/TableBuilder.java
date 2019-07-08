package com.ansys.cluster.monitor.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.ansys.cluster.monitor.data.AnsQueue;
import com.ansys.cluster.monitor.data.SGE_DataConst;

public class TableBuilder {

	public TableBuilder() {
		// TODO Auto-generated constructor stub
	}

	public static void  buildTable(AnsQueue queue) {

		if (queue.getTable() == null && queue.getMembersType().equalsIgnoreCase(SGE_DataConst.clusterTypeJob)) {

			NodeTableModel model;
			if(queue.getName() != SGE_DataConst.noNameJobQueue) {
				model = new NodeTableModel(queue, 6);
			}else {
				model = new NodeTableModel(queue);
			}

			JTable table = new JTable(model);
			table.setAutoCreateRowSorter(true);

			TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
			table.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>();

			int columnIndexToSort = 1;
			sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));

			sorter.setSortKeys(sortKeys);
			sorter.sort();
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment( JLabel.CENTER );
			table.setDefaultRenderer(Long.class, centerRenderer);
			table.setDefaultRenderer(Integer.class, centerRenderer);
			
			
			queue.setTable(table);
			
		}
	}

}
