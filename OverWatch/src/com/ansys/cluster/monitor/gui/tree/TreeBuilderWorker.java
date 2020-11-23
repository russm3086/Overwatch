/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import java.io.IOException;
import java.util.SortedMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.json.JSONException;

import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.data.MasterQueue;
import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.gui.Console;

/**
 * @author rmartine
 *
 */
public class TreeBuilderWorker {
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

	public TreeBuilderWorker(JTree jTree) {
		// TODO Auto-generated constructor stub
		setjTree(jTree);

	}

	/**
	 * 
	 */

	public void buildTree(Cluster cluster) throws IOException {

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

		SortedMap<String, MasterQueue> masterQueue = cluster.getMasterQueue();
		for (Entry<String, MasterQueue> masterMap : masterQueue.entrySet()) {

			MasterQueue queue = masterMap.getValue();
			logger.finest("Creating master queue branch " + queue);
			masterQueueNode = new DefaultMutableTreeNode(queue);
			parseMasterQueue(model, masterQueueNode, queue);

			model.insertNodeInto(masterQueueNode, root, root.getChildCount());
		}

		logger.exiting(sourceClass, "buildTree", root);
	}

	/**
	 * 
	 * @param model
	 * @param nodeQueue
	 * @param queue
	 */
	private void parseMasterQueue(DefaultTreeModel model, DefaultMutableTreeNode nodeQueue, MasterQueue masterQueue) {
		logger.entering(sourceClass, "createNodes", nodeQueue);
		DefaultMutableTreeNode nodeBranch = null;

		sortMasterQueue(masterQueue);
		LinkedHashMap<String, AnsQueueAbstract> queues = sortMasterQueue(masterQueue);

		for (Entry<String, AnsQueueAbstract> queue : queues.entrySet()) {
			logger.finer("Creating Queue branch " + queue.getValue());

			nodeBranch = new DefaultMutableTreeNode(queue.getValue());
			createNode(model, nodeBranch, queue.getValue());
			model.insertNodeInto(nodeBranch, nodeQueue, nodeQueue.getChildCount());
			logger.finer("Created Queue branch " + queue.getValue());
		}

		logger.exiting(sourceClass, "createNodes");
	}

	/**
	 * Groups the nodes into compute and visual
	 * 
	 * @param masterQueue
	 * @return
	 */
	private LinkedHashMap<String, AnsQueueAbstract> sortMasterQueue(MasterQueue masterQueue) {
		logger.entering(sourceClass, "sortMasterQueue", masterQueue);

		LinkedHashMap<String, AnsQueueAbstract> visQueues = new LinkedHashMap<String, AnsQueueAbstract>();

		LinkedHashMap<String, AnsQueueAbstract> sortedQueue = new LinkedHashMap<String, AnsQueueAbstract>();

		SortedMap<String, AnsQueueAbstract> queues = masterQueue.getQueues();

		sortedQueue.putAll(queues);

		for (Iterator<Map.Entry<String, AnsQueueAbstract>> it = sortedQueue.entrySet().iterator(); it.hasNext();) {

			Map.Entry<String, AnsQueueAbstract> entry = it.next();
			if (entry.getValue().isVisualNode() == true) {

				it.remove();
				visQueues.put(entry.getKey(), entry.getValue());
			}
		}

		sortedQueue.putAll(visQueues);
		logger.exiting(sourceClass, "sortMasterQueue");

		return sortedQueue;
	}

	private void createNode(DefaultTreeModel model, DefaultMutableTreeNode nodeBranch, AnsQueueAbstract queue) {
		logger.entering(sourceClass, "createNode");
		DefaultMutableTreeNode nodeChild = null;

		SortedMap<Object, ClusterNodeAbstract> nodes = queue.getNodes();

		if (nodes != null) {
			for (Entry<Object, ClusterNodeAbstract> node : nodes.entrySet()) {

				logger.finer("Creating leaf " + node.getKey());
				nodeChild = new DefaultMutableTreeNode(node.getValue());
				model.insertNodeInto(nodeChild, nodeBranch, nodeBranch.getChildCount());
			}
		} else {

			logger.finer("No nodes are found");
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
