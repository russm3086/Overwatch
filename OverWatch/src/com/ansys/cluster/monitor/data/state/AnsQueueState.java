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
public class AnsQueueState extends StateAbstract {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7822234941347683698L;
	private static final String NormalStateDesc = "Queue is in a normal state";

	protected AnsQueueState(StateAbstract state) {
		super(state);
	}

	public AnsQueueState(String name, int value, String description, Color color) {
		super(name, value, description, color);

	}

	public static final AnsQueueState Normal = new AnsQueueState("Normal State", 4900, NormalStateDesc, Color.GREEN);

}
