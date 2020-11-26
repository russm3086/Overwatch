/**
 * 
 */
package com.ansys.cluster.monitor.settings;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

/**
 * @author rmartine
 *
 */
public class PropUtil {
	private String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);

	public enum Compare {
		GREATER_THAN, LESS_THAN, EQUALS

	}

	/**
	 * 
	 */
	public PropUtil() {
		// TODO Auto-generated constructor stub
	}

	public SGE_MonitorProp mergeProps(SGE_MonitorProp newProps, SGE_MonitorProp origProp, List<String> lstRegex) {

		LinkedHashMap<String, Object> valueMap = getValues(origProp, lstRegex);
		SGE_MonitorProp props = mergeProps(newProps, origProp);

		for (Entry<String, Object> entry : valueMap.entrySet()) {

			props.setProperty(entry.getKey(), entry.getValue());
		}

		return props;
	}

	public SGE_MonitorProp mergePropsOlderPriority(SGE_MonitorProp newProps, SGE_MonitorProp origProp) {

		String version = newProps.getMonitorVersion();
		newProps.copy(origProp);
		newProps.setMonitorVersion(version);
		newProps.setHeader(newProps.getHeader());
		return newProps;

	}

	public SGE_MonitorProp mergeProps(SGE_MonitorProp newProps, SGE_MonitorProp origProp) {

		origProp.copy(newProps);
		origProp.setHeader(newProps.getHeader());
		return origProp;

	}

	public LinkedHashMap<String, Object> getValues(SGE_MonitorProp prop, List<String> lstRegex) {

		LinkedHashMap<String, Object> matchedValues = new LinkedHashMap<String, Object>();

		for (String regex : lstRegex) {

			matchedValues.putAll(getValue(prop, regex));
		}

		return matchedValues;
	}

	public LinkedHashMap<String, Object> getValue(SGE_MonitorProp prop, String regex) {

		LinkedHashMap<String, Object> matchedValues = new LinkedHashMap<String, Object>();

		Iterator<String> itKey = prop.getKeys();

		while (itKey.hasNext()) {
			String key = itKey.next();
			logger.finest("Validating key " + key + " with regex " + regex);

			if (key.matches(regex)) {

				logger.finer("Found match " + key);
				logger.finer("Saving value " + prop.getProperty(key));
				matchedValues.put(key, prop.getProperty(key));
			}
		}

		return matchedValues;
	}

	/**
	 * 
	 * @param firstVersion
	 * @param secondVersion
	 * @param token
	 * @return
	 */
	public Compare compareVersions(String firstVersion, String secondVersion, String token) {
		logger.entering(sourceClass, "compareVersions");

		firstVersion = nullChecker(firstVersion, "0");

		secondVersion = nullChecker(secondVersion, "0");

		float fltFirstVersion = getVersion(firstVersion, token);

		float fltSecondVerion = getVersion(secondVersion, token);

		Compare result = null;

		if (fltFirstVersion > fltSecondVerion) {

			result = Compare.GREATER_THAN;
		}
		if (fltSecondVerion > fltFirstVersion) {

			result = Compare.LESS_THAN;
		}
		if (fltSecondVerion == fltFirstVersion) {

			result = Compare.EQUALS;
		}

		logger.exiting(sourceClass, "compareVersions", result);
		return result;
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */

	public String nullChecker(String value, String defaultValue) {

		if (value == null || value.equals("")) {
			value = defaultValue;
		}

		return value;
	}

	public float getVersion(String version, String token) {
		logger.entering(sourceClass, "getVersion");
		float result = 0;

		if (version == null || version.equals("")) {
			version = new String("0");
		}

		String[] versionSplit = version.split(token);

		if (versionSplit.length > 0) {
			String value = new String(versionSplit[0] + ".");

			for (int i = 1; i < versionSplit.length; i++) {
				value += versionSplit[i];
			}

			result = Float.parseFloat(value);
		}

		logger.exiting(sourceClass, "getVersion", result);
		return result;
	}

}
