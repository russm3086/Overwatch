/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import java.io.IOException;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.json.JSONException;

import com.ansys.cluster.monitor.data.AnsQueue;
import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.gui.Console;

/**
 * @author rmartine
 *
 */
public class TreeBuilder {
	private JTree jTree;
	private DefaultTreeModel model;
	/**
	 * The name of current class. To be used with the logging subsystem.
	 */
	private final String sourceClass = this.getClass().getName();

	/**
	 * The Logger instance. All log messages from this class are handle here.
	 */
	private Logger logger = Logger.getLogger(sourceClass);

	public TreeBuilder(JTree jTree) {
		// TODO Auto-generated constructor stub
		setjTree(jTree);

	}

	/**
	 * 
	 */

	public void buildTree(Cluster cluster) throws JSONException, IOException {

		synchronized (jTree) {

			DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();

			removeSpecificCluster(cluster, model, true);

			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			DefaultMutableTreeNode top = new DefaultMutableTreeNode(cluster);
			model.insertNodeInto(top, root, root.getChildCount());
			buildTree(model, top, cluster);

			model.reload();

			Console.setStatusLabel("Tree Created");
		}
	}

	private void removeSpecificCluster(Cluster cluster, DefaultTreeModel model, boolean boClearRoot) {

		if (boClearRoot) {

			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			root.removeAllChildren();

		} else {

			TreePath path = jTree.getNextMatch(cluster.getName(), 0, Position.Bias.Forward);

			if (path != null) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (node.getParent() != null) {
					if (cluster.getName().equalsIgnoreCase(node.toString()))
						model.removeNodeFromParent(node);
				}
			}
		}
		model.reload();

	}

	private void buildTree(DefaultTreeModel model, DefaultMutableTreeNode root, Cluster cluster)
			throws JSONException, IOException {
		logger.entering(sourceClass, "buildTree", root);
		DefaultMutableTreeNode masterQueueNode = null;

		ConcurrentHashMap<String, AnsQueue> masterQueue = cluster.getMasterQueue();
		for (Entry<String, AnsQueue> masterMap : masterQueue.entrySet()) {

			AnsQueue queue = masterMap.getValue();
			logger.finest("Creating master queue branch " + queue);
			masterQueueNode = new DefaultMutableTreeNode(queue);
			createNodes(model, masterQueueNode, queue);

			model.insertNodeInto(masterQueueNode, root, root.getChildCount());
		}

		logger.exiting(sourceClass, "buildTree", root);
	}

	private void createNodes(DefaultTreeModel model, DefaultMutableTreeNode nodeQueue, AnsQueue queue) {
		logger.entering(sourceClass, "createNodes", nodeQueue);
		DefaultMutableTreeNode nodeBranch = null;

		SortedMap<String, ClusterNodeAbstract> nodes = queue.getNodes();

		for (Entry<String, ClusterNodeAbstract> node : nodes.entrySet()) {
			logger.finer("Creating Queue branch " + node.getValue());
			nodeBranch = new DefaultMutableTreeNode(node.getValue());

			createNode(model, nodeBranch, (AnsQueue) node.getValue());
			model.insertNodeInto(nodeBranch, nodeQueue, nodeQueue.getChildCount());
		}

		logger.exiting(sourceClass, "createNodes");
	}

	private void createNode(DefaultTreeModel model, DefaultMutableTreeNode nodeBranch, AnsQueue queue) {
		logger.entering(sourceClass, "createNode");
		DefaultMutableTreeNode nodeChild = null;

		SortedMap<String, ClusterNodeAbstract> nodes = queue.getNodes();
		for (Entry<String, ClusterNodeAbstract> node : nodes.entrySet()) {

			logger.finer("Creating leaf " + node.getKey());
			nodeChild = new DefaultMutableTreeNode(node.getValue());
			model.insertNodeInto(nodeChild, nodeBranch, nodeBranch.getChildCount());
		}

		logger.exiting(sourceClass, "createNode");
	}

	public DefaultTreeModel getModel() {
		return model;
	}

	public void setModel(DefaultTreeModel model) {
		this.model = model;
	}

	public JTree getjTree() {
		return jTree;
	}

	public void setjTree(JTree jTree) {
		this.jTree = jTree;
	}

}
