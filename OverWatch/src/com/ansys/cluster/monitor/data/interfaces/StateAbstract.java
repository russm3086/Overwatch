/**
 * 
 */
package com.ansys.cluster.monitor.data.interfaces;

import java.awt.Color;
import java.io.Serializable;
import java.util.Collections;
import java.util.TreeMap;
import java.util.logging.Logger;


/**
 * @author rmartine
 *
 */
public abstract class StateAbstract implements StateInterface, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2531425786952109794L;
	private static final String sourceClass = StateAbstract.class.getName();
	private static final Logger logger = Logger.getLogger(sourceClass);
	protected int value;
	protected String name;
	protected String description;
	protected Color color;

	/**
	 * 
	 */
	protected StateAbstract(String name, int value, String description, Color color) {

		setValue(value);
		setName(name);
		setDescription(description);
		setColor(color);

		logger.finest("Create State: " + name + " value: " + value + " Description: " + description);
	}

	protected StateAbstract(StateAbstract state) {

		setState(state);
	}

	public static TreeMap<Integer, StateAbstract> parseCode(String code, StateParserInterface parser) {
		TreeMap<Integer, StateAbstract> store = new TreeMap<Integer, StateAbstract>((Collections.reverseOrder()));
		StateAbstract state = null;

		if (code != null) {
			if (code.length() >= 1) {

				char[] c = code.toCharArray();

				for (int i = 0; i < c.length; i++) {

						state = parser.parseCode(c[i]);
						store.put(state.value, state);
				}
			}
		}
		return store;
	}

	private void setState(StateAbstract state) {

		setValue(state.value);
		setName(state.name);
		setDescription(state.description);
		setColor(state.color);

	}

	public boolean between(Object object1, Object object2) {
		boolean result = false;
		int value1 = ((StateAbstract) object1).getValue();
		int value2 = ((StateAbstract) object2).getValue();

		logger.finest("Evaulating if " + getValue() + " >= " + value1 + " and <= " + value2);
		if ((this.value >= value1) && (this.value <= value2)) {
			result = true;
		}

		logger.finest("Evaluation result " + result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ansys.cluster.monitor.data.interfaces.StateInterface#equals(java.lang.
	 * Object)
	 */
	public boolean equals(StateAbstract ab) {
		logger.entering(sourceClass, "equals", ab);
		boolean result = false;
		
		if (ab.getValue() == this.getValue()) {
			
			result = true;
		}
		logger.exiting(sourceClass, "equals", result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.interfaces.StateInterface#getValue()
	 */
	@Override
	public int getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.interfaces.StateInterface#setValue(int)
	 */
	@Override
	public void setValue(int value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.interfaces.StateInterface#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ansys.cluster.monitor.data.interfaces.StateInterface#setName(java.lang.
	 * String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ansys.cluster.monitor.data.interfaces.StateInterface#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ansys.cluster.monitor.data.interfaces.StateInterface#setDescription(java.
	 * lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ansys.cluster.monitor.data.interfaces.StateInterface#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}
