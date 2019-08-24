/**
 * 
 */
package com.ansys.cluster.monitor.gui.table;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.russ.util.gui.tree.TreeUtil;
import com.russ.util.gui.tree.TreeUtilSearchItem;

/**
 * @author rmartine
 *
 */
public class TableMouseListener implements MouseListener {
	private JTable table;
	private int indexSrchColumn;
	private JTree tree;
	public static final int SEARCH_ALL_COLUMNS = -1;

	/**
	 * 
	 */
	public TableMouseListener(JTable table, JTree tree) {
		this(table, 0, tree);
	}

	public TableMouseListener(JTable table, int indexSrchColumn, JTree tree) {
		// TODO Auto-generated constructor stub
		setTable(table);
		setIndexSrchColumn(indexSrchColumn);
		setTree(tree);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getClickCount() == 2) {
			int row = table.rowAtPoint(e.getPoint());
			int column = table.columnAtPoint(e.getPoint());

			int columnIndex = table.convertColumnIndexToModel(column);
			Object value = table.getValueAt(row, column);

			search(columnIndex, value);
		} else {
			// do something else
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void search(int column, Object value) {
		if (column == getIndexSrchColumn() || getIndexSrchColumn() == SEARCH_ALL_COLUMNS) {

			String columnName = table.getColumnName(column);
			String searchQuery = "(?)" + columnName + ": " + String.valueOf(value);

			TreeUtil tu = new TreeUtil();
			ArrayList<TreeUtilSearchItem> list = tu.search(getTree(), searchQuery);

			if (list.size() > 0) {

				TreeUtilSearchItem tusi = list.get(0);
				TreePath treePath = tusi.getPath();

				tree.setSelectionPath(treePath);
				tree.scrollPathToVisible(treePath);

				System.out.println(treePath);
			}
		}
	}

	/**
	 * @return the table
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(JTable table) {
		this.table = table;
	}

	/**
	 * @return the indexSrchColumn
	 */
	public int getIndexSrchColumn() {
		return indexSrchColumn;
	}

	/**
	 * @param indexSrchColumn the indexSrchColumn to set
	 */
	public void setIndexSrchColumn(int indexSrchColumn) {
		this.indexSrchColumn = indexSrchColumn;
	}

	/**
	 * @return the tree
	 */
	public JTree getTree() {
		return tree;
	}

	/**
	 * @param tree the tree to set
	 */
	public void setTree(JTree tree) {
		this.tree = tree;
	}

}
