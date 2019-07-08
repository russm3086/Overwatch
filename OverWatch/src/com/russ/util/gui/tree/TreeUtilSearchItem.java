/**
 * 
 */
package com.russ.util.gui.tree;

import java.util.regex.Matcher;

import javax.swing.tree.TreePath;

import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;

/**
 * @author rmartine
 *
 */
public class TreeUtilSearchItem {
	private ClusterNodeAbstract node;
	private TreePath path;
	private Matcher matcher;

	/**
	 * 
	 * @param node    - <code>ClusterNodeAbstract</code> object that has been found
	 * @param path    - <code>TreePath</code> the path to the found object
	 * @param matcher - <code>Matcher</code> the engine that performs match
	 *                operations on a character sequence by interpreting a Pattern
	 */
	public TreeUtilSearchItem(ClusterNodeAbstract node, TreePath path, Matcher matcher) {
		// TODO Auto-generated constructor stub
		setNode(node);
		setPath(path);
		setMatcher(matcher);
	}

	/**
	 * @return the node
	 */
	public ClusterNodeAbstract getNode() {
		return node;
	}

	/**
	 * @param node the node to set
	 */
	public void setNode(ClusterNodeAbstract node) {
		this.node = node;
	}

	/**
	 * @return the path
	 */
	public TreePath getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(TreePath path) {
		this.path = path;
	}

	/**
	 * @return the matcher
	 */
	public Matcher getMatcher() {
		return matcher;
	}

	/**
	 * @param matcher the matcher to set
	 */
	public void setMatcher(Matcher matcher) {
		this.matcher = matcher;
	}

}
