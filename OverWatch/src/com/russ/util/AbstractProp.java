/**
  * Copyright (c) 2018 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.russ.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author rmartine
 * @since
 */
public abstract class AbstractProp extends Properties {
	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */
	private static final long serialVersionUID = -817554340992510968L;

	/**
	 * 
	 */
	public AbstractProp() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param defaults
	 */
	public AbstractProp(Properties defaults) {
		super(defaults);
		// TODO Auto-generated constructor stub
	}

	public synchronized Object setIntProperty(String key, int value) {

		Object obj = setLogProperty(key, String.valueOf(value));

		return obj;
	}

	public synchronized ArrayList<?> getArrayList(String key) {
		ArrayList<?> list = null;
		try {

			list = (ArrayList<?>) getLogObject(key);

		} catch (ClassCastException | NullPointerException e) {

			logger.log(Level.FINEST, "Error retrieving data", e);

		}

		return list;
	}

	public synchronized void setArrayList(String key, ArrayList<?> value) {

		setLogObject(key, value);
	}

	public synchronized LinkedHashSet<?> getLinkedHashSett(String key) {
		LinkedHashSet<?> list = null;
		try {

			list = (LinkedHashSet<?>) getLogObject(key);

		} catch (ClassCastException | NullPointerException e) {

			logger.log(Level.FINEST, "Error retrieving data", e);
		}

		return list;
	}

	public synchronized void setLinkedHashSet(String key, LinkedHashSet<?> value) {

		setLogObject(key, value);
	}

	public synchronized int getIntProperty(String key) {
		int value = 0;
		try {

			value = Integer.valueOf(getLogProperty(key).trim());

		} catch (NumberFormatException | NullPointerException e) {

			logger.log(Level.FINEST, "Error retrieving data", e);
		}

		return value;
	}

	public synchronized double getDoubleProperty(String key) {
		double value = 0.0;
		try {

			value = Double.valueOf(getLogProperty(key).trim());

		} catch (NumberFormatException | NullPointerException e) {

			logger.log(Level.FINEST, "Error retrieving data", e);
		}

		return value;
	}

	public synchronized Object setDoubleProperty(String key, double value) {

		Object obj = setLogProperty(key, String.valueOf(value));

		return obj;

	}

	public synchronized long getLongProperty(String key) {
		long value = 0L;
		try {

			value = Long.valueOf(getLogProperty(key));

		} catch (NumberFormatException | NullPointerException e) {

			transLog(Level.FINEST, "Error retrieving data", e);
		}

		return value;
	}

	public synchronized Object setLongProperty(String key, long value) {

		Object obj = setLogProperty(key, String.valueOf(value));

		return obj;
	}

	public synchronized Object setBoolProperty(String key, boolean value) {

		Object obj = setLogProperty(key, String.valueOf(value));

		return obj;

	}

	public synchronized boolean getBoolProperty(String key) {
		return Boolean.valueOf(getLogProperty(key));
	}

	public synchronized boolean getBoolProperty(String key, boolean defaultValue) {

		String strDefaultValue = String.valueOf(defaultValue);
		boolean value = Boolean.valueOf(getLogProperty(key, strDefaultValue));

		return value;

	}

	public synchronized LocalDateTime getDateProperty(String key) {

		LocalDateTime dateTime = null;

		try {
			dateTime = TimeUtil.getLocalDateTime(getLogProperty(key));
		} catch (java.lang.NullPointerException e) {

			transLog(Level.FINEST, key + " is null");

		}

		return dateTime;
	}

	public synchronized Object setDateProperty(String key, LocalDateTime value) {

		Object obj = setLogProperty(key, value.toString());
		return obj;
	}

	public synchronized Object setLogProperty(String key, String value) {

		transLog(Level.FINEST, "Setting key: " + key + " with value: " + value);
		return setProperty(key, value);

	}

	public synchronized String getLogProperty(String key, String defaultValue) {

		String value = getProperty(key, defaultValue);
		transLog(Level.FINEST, "Getting  key: " + key + " with value: " + value + " (default: " + defaultValue + ")");
		return value;

	}

	public synchronized String getLogProperty(String key) {

		String value = getProperty(key);
		transLog(Level.FINEST, "Getting  key: " + key + " with value: " + value);
		return value;

	}

	public synchronized Object getLogObject(String key) {

		Object value = get(key);
		transLog(Level.FINEST, "Getting  key: " + key + " with value: " + value);
		return value;

	}

	public synchronized Object setLogObject(String key, Object value) {

		transLog(Level.FINEST, "Setting key: " + key + " with value: " + value);
		return put(key, value);

	}

	public synchronized Object putLog(Object key, Object value) {

		transLog(Level.FINEST, "Setting key: " + key + " with value: " + value);
		return put(key, value);

	}

	public synchronized Object getLog(Object key) {

		Object value = get(key);
		transLog(Level.FINEST, "Getting  key: " + key + " with value: " + value);
		return value;

	}

	public synchronized void update(Properties prop) {

		Set<String> propertyNames = prop.stringPropertyNames();
		for (String name : propertyNames) {
			String propertyValue = prop.getProperty(name);
			defaults.setProperty(name, propertyValue);
		}
	}

	public synchronized void update(Hashtable<String, String> hashTable) {
		Enumeration<String> e = hashTable.keys();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			String element = hashTable.get(key);

			defaults.put(key, element);
		}
	}

	public synchronized String toString() {
		StringBuffer sb = new StringBuffer();

		Enumeration<?> e = propertyNames();
		while (e.hasMoreElements()) {

			String key = (String) e.nextElement();
			String value = (String) getLogProperty(key);
			sb.append("key: " + key + " value: " + value + "| ");

		}

		return sb.toString();
	}

	private void transLog(Level level, String msg, Exception e) {
		if (logger != null) {
			logger.log(level, msg, e);
		}
	}

	private void transLog(Level level, String msg) {

		if (logger != null) {
			logger.log(level, msg);
		}

	}

}
