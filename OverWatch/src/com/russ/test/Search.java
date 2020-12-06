/**
 * 
 */
package com.russ.test;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.gui.ClusterDataCollector;
import com.ansys.cluster.monitor.gui.tree.TreeBuilder;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.gui.tree.TreeUtil;
import com.russ.util.gui.tree.TreeUtilSearchItem;

/**
 * @author rmartine
 *
 */
public class Search {

	/**
	 * 
	 */
	public Search() {
		
	}

	public static void main(String[] args) throws IOException {

		SGE_MonitorProp mainProps = new SGE_MonitorProp();

		ClusterDataCollector worker = new ClusterDataCollector(mainProps);
		Cluster cluster = worker.retrieveClusterData();

		JTree tree = new JTree(new DefaultMutableTreeNode("Cluster(s)"));

		TreeBuilder tbmt = new TreeBuilder(mainProps, tree, cluster);
		tbmt.refreshTree();
		
		TreeUtil tu = new TreeUtil();
		ArrayList<TreeUtilSearchItem> list = tu.search(tree, "rmartine");

		list.size();

	}

}
