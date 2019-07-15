package com.ansys.cluster.monitor.data.interfaces;

import java.io.Serializable;

public interface StateInterface extends Serializable{

	public static StateAbstract parseCode(String code) {
		return null;
	}

	public static StateAbstract parseCode(char code) {
		return null;
	}

	boolean equals(Object ox);

	/**
	 * @return the value
	 */
	int getValue();

	/**
	 * @param value the value to set
	 */
	void setValue(int value);

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @param name the name to set
	 */
	void setName(String name);

	/**
	 * @return the description
	 */
	String getDescription();

	/**
	 * @param description the description to set
	 */
	void setDescription(String description);

	String toString();

}