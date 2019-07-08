/**
 * 
 */
package com.ansys.cluster.monitor.alert;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.russ.util.nio.ResourceLoader;

/**
 * @author rmartine
 *
 */
public class Alerter {
	private AlertMap alertMap = new AlertMap();
	private ConcurrentHashMap<String, Map<String, Alert>> mapResult = new ConcurrentHashMap<String, Map<String, Alert>>();
	/**
	 * The name of current class. To be used with the logging subsystem.
	 */
	private final String sourceClass = this.getClass().getName();

	/**
	 * The Logger instance. All log messages from this class are handle here.
	 */
	private Logger logger = Logger.getLogger(sourceClass);

	public Alerter() {

	}

	public void addAlert(Alert alert) {

		alertMap.put(alert.getIdentifier(), alert);

	}

	public void compare(Alerter alerter) {
		compare(alerter.getAlertMap());
	}

	public void compare(AlertMap alerts) {

		if (alerts.isEmpty()) {
			alertMap = alerts;
		} else {
			mapResult.put("NEW_ALERTS", alertMap.newAlerts(alerts));
			mapResult.put("MISSING_ALERTS", alertMap.missingAlerts(alerts));
			mapResult.put("MATCHED_ALERTS", alertMap.matchedAlerts(alerts));
		}
	}

	public void processMatchedAlerts() {
		ConcurrentHashMap<String, Alert> changedResult = new ConcurrentHashMap<String, Alert>();
		Iterator<Entry<String, Alert>> mapIter = mapResult.get("MATCHED_ALERTS").entrySet().iterator();
		while (mapIter.hasNext()) {

			Entry<String, Alert> matchedEntry = mapIter.next();
			
			Alert origAlert = alertMap.get(matchedEntry.getKey());
			Alert newAlert = matchedEntry.getValue();

			for (Entry<Integer, StateAbstract> entry : newAlert.getStore().entrySet()) {
				if(!origAlert.getStore().containsKey(entry.getKey())) {
					
					changedResult.put(matchedEntry.getKey(), matchedEntry.getValue());
					mapIter.remove();
					
					break;
					
				}
			}
		}
		mapResult.put("CHANGED_ALERTS", changedResult);
	}

	/**
	 * 
	 */
	public Alerter(AlertMap alerts) {
		// TODO Auto-generated constructor stub
		setAlertMap(alerts);
	}

	public void loadAlerts(String filePath) throws ClassNotFoundException, IOException {
		logger.entering(sourceClass, "loadAlerts", filePath);
		AlertMap alerts = (AlertMap) ResourceLoader.loadSerial(filePath);
		setAlertMap(alerts);
		logger.exiting(filePath, "loadAlerts");
	}

	public void saveAlerts(String filePath) throws IOException {
		saveAlerts(alertMap, filePath);
	}

	public void saveAlerts(AlertMap alerts, String filePath) throws IOException {
		logger.entering(sourceClass, "saveAlerts");
		ResourceLoader.saveSerial(alerts, filePath);
		logger.exiting(sourceClass, "saveAlerts");
	}

	public AlertMap getAlertMap() {
		return alertMap;
	}

	public void setAlertMap(AlertMap alertMap) {
		this.alertMap = alertMap;
	}
}
