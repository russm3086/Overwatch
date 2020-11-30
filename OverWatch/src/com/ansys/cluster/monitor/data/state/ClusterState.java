/**
 * 
 */
package com.ansys.cluster.monitor.data.state;

import java.awt.Color;

import com.ansys.cluster.monitor.data.interfaces.StateAbstract;

/**
 * @author rmartine
 *
 */
public class ClusterState extends StateAbstract {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1771439096321832595L;
	private static final String NormalStateDesc = "Cluster is running normal";

	/**
	 * @param name
	 * @param value
	 * @param description
	 * @param color
	 */
	public ClusterState(String name, int value, String description, Color color) {
		super(name, value, description, color);
	}

	/**
	 * @param state
	 */
	public ClusterState(StateAbstract state) {
		super(state);
	}

	public static final ClusterState Normal = new ClusterState("Normal State", 5900, NormalStateDesc, Color.GREEN);

	
}
