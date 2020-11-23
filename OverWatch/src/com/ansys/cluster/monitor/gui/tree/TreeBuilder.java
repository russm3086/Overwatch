/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.json.JSONException;

import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.data.MasterQueue;
import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.gui.Console;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.gui.tree.TreeStateProps;
import com.russ.util.gui.tree.TreeUtil;

/**
 * @author rmartine
 *
 */
public class TreeBuilder {
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	private SGE_MonitorProp mainProps = null;
	private DefaultTreeModel model;
	private JTree tree;
	private TreeStateProps tsProps;
	private Cluster cluster;

	/**
	 * 
	 */
	public TreeBuilder(SGE_MonitorProp mainProps, JTree tree, Cluster cluster) {
		this.mainProps = mainProps;
		this.tree = tree;
		this.cluster = cluster;
	}

	public boolean isNewCluster(Cluster newCluster, JTree current) {
		logger.entering(sourceClass, "isNewCluster");

		boolean result = false;
		DefaultTreeModel model = (DefaultTreeModel) current.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		TreeNode firstChild = null;

		if (root.getChildCount() > 0) {

			firstChild = root.getFirstChild();
			String name = firstChild.toString();
			result = !newCluster.getName().equalsIgnoreCase(name);
		} else if (root.getChildCount() == 0) {

			result = true;
		}

		logger.exiting(sourceClass, "isNewCluster", result);
		return result;
	}

	public void refreshTree() throws IOException {
		logger.entering(sourceClass, "refreshTree");

		TreeUtil tu = new TreeUtil();
		tsProps = tu.saveTreeState(tree);
		boolean isNewCluster = isNewCluster(cluster, tree);

		buildTree();

		if (isNewCluster || tsProps.size() == 0) {

			tu.expandTreeToLevel(tree, mainProps.getGuiTreeExpansionLevel());
			tree.setSelectionRow(1);

		} else {

			tu.applyTreeState(tree, tsProps);
		}

		
		logger.entering(sourceClass, "refreshTree");
	}

	private void buildTree() throws IOException {

		synchronized (tree) {

			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

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

			TreePath path = tree.getNextMatch(cluster.getName(), 0, Position.Bias.Forward);

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
	public static LinkedHashMap<String, AnsQueueAbstract> sortMasterQueue(MasterQueue masterQueue) {

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
		return tree;
	}

	public void setjTree(JTree jTree) {
		this.tree = jTree;
	}

}
