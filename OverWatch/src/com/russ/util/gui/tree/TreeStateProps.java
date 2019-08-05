/**
 * 
 */
package com.russ.util.gui.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.tree.TreePath;

import com.russ.util.AbstractProp;

/**
 * @author rmartine
 *
 */
public class TreeStateProps extends AbstractProp {
	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */
	private static final long serialVersionUID = 8366166941939347671L;

	/**
	 * 
	 */
	public TreeStateProps() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param defaults
	 */
	public TreeStateProps(Properties defaults) {
		super(defaults);
		// TODO Auto-generated constructor stub
	}

	public void setSelectedPaths(TreePath[] treePaths) {
		setArrayProperty("SelectedPaths", treePaths);
	}

	public TreePath[] getSelectedPaths() {

		Object[] object = getArrayProperty("SelectedPaths");
		TreePath[] treePath = Arrays.copyOf(object, object.length, TreePath[].class);
		return treePath;
	}

	public void setExpandedState(Enumeration<TreePath> enTreePath) {
		putLog("ExpandedState", enTreePath);
	}

	public Enumeration<TreePath> getExpandedState() {
		Enumeration<TreePath> enTreePath = null;

		try {

			Enumeration<?> es = (Enumeration<?>) getLog("ExpandedState");
			ArrayList<TreePath> list = new ArrayList<TreePath>();

			while (es.hasMoreElements()) {

				TreePath tp = (TreePath) es.nextElement();
				list.add(tp);
			}

			Enumeration<TreePath> enumTree = Collections.enumeration(list);

			enTreePath = enumTree;

		} catch (ClassCastException e) {

			logger.log(Level.FINEST, "Error retrieving data", e);

		}

		return enTreePath;
	}

}
