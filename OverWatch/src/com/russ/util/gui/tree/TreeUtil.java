/**
 * 
 */
package com.russ.util.gui.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;

/**
 * @author rmartine
 *
 */
public class TreeUtil {
	/**
	 * The name of current class. To be used with the logging subsystem.
	 */
	private final String sourceClass = this.getClass().getName();

	/**
	 * The Logger instance. All log messages from this class are handle here.
	 */
	private Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */
	public TreeUtil() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<TreeUtilSearchItem> search(JTree tree, String search) {
		logger.entering(sourceClass, "search");

		Pattern pattern = Pattern.compile(search);
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		TreePath path = new TreePath(root);

		logger.exiting(sourceClass, "search");

		return search(path, pattern);

	}

	public ArrayList<TreeUtilSearchItem> search(TreePath path, Pattern pattern) {
		logger.entering(sourceClass, "search", path);

		ArrayList<TreeUtilSearchItem> list = new ArrayList<TreeUtilSearchItem>();

		TreeNode treeNode = (TreeNode) path.getLastPathComponent();
		Object object = ((DefaultMutableTreeNode) treeNode).getUserObject();
		logger.finer("Traversing " + object);

		if (!(object instanceof String)) {

			ClusterNodeAbstract node = (ClusterNodeAbstract) object;
			Matcher matcher = search(node.getMetaData(), pattern);

			if (matcher != null) {
				TreeUtilSearchItem searchItem = new TreeUtilSearchItem(node, path, matcher);
				list.add(searchItem);
			}
		}

		if (treeNode.getChildCount() >= 0) {
			Enumeration<?> enumeration = treeNode.children();
			while (enumeration.hasMoreElements()) {
				TreeNode childNode = (TreeNode) enumeration.nextElement();
				TreePath childPath = path.pathByAddingChild(childNode);

				list.addAll(search(childPath, pattern));
			}
		}

		logger.exiting(sourceClass, "search", list);
		return list;
	}

	public Matcher search(String source, Pattern pattern) {
		logger.entering(sourceClass, "search");
		Matcher result = null;

		logger.finest("Searching metadata\n" + source);
		Matcher matcher = pattern.matcher(source);
		if (matcher.find()) {
			logger.finest("Found: " + matcher.toString());
			result = matcher;
		}

		logger.exiting(sourceClass, "search", result);

		return result;
	}

	public void expandTreeToLevel(JTree tree, int nodeLevel) {
		expandTree(tree, true, false, nodeLevel);
	}

	public void expandTreeToNode(JTree tree, TreePath path) {

		expandAll(tree, path, true, true, 0, 0);
	}

	public void expandAllTree(JTree tree) {
		expandTree(tree, true, true, 0);
	}

	public void collapseTreeToLevel(JTree tree, int nodeLevel) {
		expandTree(tree, false, false, nodeLevel);
	}

	public void collapseAllTree(JTree tree) {
		expandTree(tree, true, true, 0);
	}

	private void expandTree(JTree tree, boolean expand, boolean allNodes, int level) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll(tree, new TreePath(root), expand, allNodes, level, 0);
	}

	private void expandAll(JTree tree, TreePath path, boolean expand, boolean allNodes, int level, int currentLevel) {
		TreeNode node = (TreeNode) path.getLastPathComponent();
		if (expand && level < currentLevel && level > 0 && !allNodes)
			return;

		if (node.getChildCount() >= 0) {
			Enumeration<?> enumeration = node.children();
			while (enumeration.hasMoreElements()) {
				TreeNode n = (TreeNode) enumeration.nextElement();
				TreePath childPath = path.pathByAddingChild(n);

				expandAll(tree, childPath, expand, allNodes, level, childPath.getPathCount());
			}
		}

		if (expand) {
			tree.expandPath(path);
		} else {
			tree.collapsePath(path);
		}
	}

	// is path1 descendant of path2
	public static boolean isDescendant(TreePath path1, TreePath path2) {
		int count1 = path1.getPathCount();
		int count2 = path2.getPathCount();
		if (count1 <= count2)
			return false;
		while (count1 != count2) {
			path1 = path1.getParentPath();
			count1--;
		}
		return path1.equals(path2);
	}

	public static String getExpansionState(JTree tree, int row) {
		TreePath rowPath = tree.getPathForRow(row);
		StringBuffer buf = new StringBuffer();
		int rowCount = tree.getRowCount();
		for (int i = row; i < rowCount; i++) {
			TreePath path = tree.getPathForRow(i);
			if (i == row || isDescendant(path, rowPath)) {
				if (tree.isExpanded(path)) {
					buf.append(",");
					buf.append(String.valueOf(i - row));

				}
			} else
				break;
		}
		return buf.toString();
	}

	public static void restoreExpanstionState(JTree tree, int row, String expansionState) {
		StringTokenizer stok = new StringTokenizer(expansionState, ",");
		while (stok.hasMoreTokens()) {
			int token = row + Integer.parseInt(stok.nextToken());
			tree.expandRow(token);
		}
	}

	/**
	 * 
	 * Save the expansion state of a tree.
	 * 
	 * @param tree
	 * 
	 * @return expanded tree path as Enumeration
	 * 
	 */

	public static Enumeration<TreePath> saveExpansionState(JTree tree) {
		return tree.getExpandedDescendants(new TreePath(tree.getModel().getRoot()));
	}

	/**
	 * 
	 * Restore the expansion state of a JTree.
	 * 
	 * @param tree
	 * 
	 * @param enumeration an Enumeration of expansion state. You can get it using
	 *                    {@link #saveExpansionState(javax.swing.JTree)}.
	 * 
	 */

	public static void loadExpansionState(JTree tree, Enumeration<TreePath> enumeration) {

		if (enumeration != null) {

			while (enumeration.hasMoreElements()) {
				TreePath treePath = (TreePath) enumeration.nextElement();
				tree.expandPath(treePath);
			}
		}
	}

}
