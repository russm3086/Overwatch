/**
 * 
 */
package com.ansys.cluster.monitor.alert;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.swing.tree.TreePath;

import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;

/**
 * @author rmartine
 *
 */
public class Alert implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3398848605384294942L;
	private ClusterNodeAbstract node;
	private Condition condition;
	private String attribute;
	private String value;
	private Pattern pattern;
	private TreePath treePath;
	private TreeMap<Integer, StateAbstract> store;
	private String identifier;

	/**
	 * 
	 * @param node      - the ClusterNodeAbstract node to base the alert on.
	 * @param condition - the condition to evaluate with
	 * @param attribute - the attribute
	 * @param value
	 */
	public Alert(String identifier, ClusterNodeAbstract node, TreePath treePath, Condition condition,
			TreeMap<Integer, StateAbstract> store) {
		// TODO Auto-generated constructor stub
		setIdentifier(identifier);
		setNode(node);
		setTreePath(treePath);
		setCondition(condition);
		setStore(store);
	}

	public Alert(ClusterNodeAbstract node, TreePath treePath, Pattern pattern, String attribute, String value) {
		// TODO Auto-generated constructor stub
		setNode(node);
		setTreePath(treePath);
		setPattern(pattern);
		setAttribute(attribute);
		setValue(value);
	}

	public boolean match(String value) {
		boolean result = false;
		if (pattern == null) {
			result = Condition.evaluate(this.getValue(), value, condition);
		} else {
			result = Condition.evalute(value, pattern);
		}
		return result;
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
	 * @return the condition
	 */
	public Condition getCondition() {
		return condition;
	}

	/**
	 * @param condition the condition to set
	 */
	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	/**
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the pattern
	 */
	public Pattern getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public TreePath getTreePath() {
		return treePath;
	}

	public void setTreePath(TreePath treePath) {
		this.treePath = treePath;
	}

	public TreeMap<Integer, StateAbstract> getStore() {
		return store;
	}

	public void setStore(TreeMap<Integer, StateAbstract> store) {
		this.store = store;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

}
