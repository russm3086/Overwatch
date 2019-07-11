/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import com.ansys.cluster.monitor.alert.AlertMonitor;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * @author rmartine
 *
 */
public class ClusterTreeListener implements TreeModelListener {
	private JTree tree;

	/**
	 * 
	 */
	public ClusterTreeListener(JTree tree) {
		// TODO Auto-generated constructor stub
		setTree(tree);
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		// TODO Auto-generated method stub

		SGE_MonitorProp mainProp = new SGE_MonitorProp();
		AlertMonitor alertMonitor = new AlertMonitor(mainProp);

		alertMonitor.scanMyJobs(tree);

		//alertMonitor.saveAlerts();

	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

}
