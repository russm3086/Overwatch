/**
 * 
 */
package com.ansys.cluster.monitor.alert;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author rmartine
 *
 */
public class AlertMap extends ConcurrentHashMap<String, Alert> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5768085909209020413L;

	public ConcurrentHashMap<String, Alert> newAlerts(ConcurrentHashMap<String, Alert> alertMap) {

		ConcurrentHashMap<String, Alert> newAlerts = new ConcurrentHashMap<String, Alert>();
		for (Entry<String, Alert> alertEntry : alertMap.entrySet()) {

			if (!containsKey(alertEntry.getKey())) {
				newAlerts.put(alertEntry.getKey(), alertEntry.getValue());
			}
		}
		return newAlerts;
	}

	public ConcurrentHashMap<String, Alert> missingAlerts(ConcurrentHashMap<String, Alert> alertMap) {

		ConcurrentHashMap<String, Alert> missingAlerts = new ConcurrentHashMap<String, Alert>();
		for (Entry<String, Alert> alertEntry : entrySet()) {

			if (!alertMap.containsKey(alertEntry.getKey())) {
				missingAlerts.put(alertEntry.getKey(), alertEntry.getValue());
			}
		}
		return missingAlerts;
	}

	public ConcurrentHashMap<String, Alert> matchedAlerts(ConcurrentHashMap<String, Alert> alertMap) {

		ConcurrentHashMap<String, Alert> matchedAlerts = new ConcurrentHashMap<String, Alert>();
		for (Entry<String, Alert> alertEntry : entrySet()) {

			if (alertMap.containsKey(alertEntry.getKey())) {
				matchedAlerts.put(alertEntry.getKey(), alertEntry.getValue());
			}
		}
		
		return matchedAlerts;
	}

	
}
