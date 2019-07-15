/**
 * 
 */
package com.ansys.cluster.monitor.alert;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTree;

import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.gui.tree.TreeUtil;
import com.russ.util.gui.tree.TreeUtilSearchItem;

/**
 * @author rmartine
 *
 */
public class AlertMonitor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8601362247233963339L;

	/**
	 * The name of current class. To be used with the logging subsystem.
	 */
	private final String sourceClass = this.getClass().getName();

	/**
	 * The Logger instance. All log messages from this class are handle here.
	 */
	private Logger logger = Logger.getLogger(sourceClass);
	private SGE_MonitorProp mainProps;
	Alerter alerter = new Alerter();

	/**
	 * 
	 */
	public AlertMonitor(SGE_MonitorProp mainProps) {
		// TODO Auto-generated constructor stub
		setMainProps(mainProps);
	}

	public void loadAlerts() {
		// TODO mainProps variable for path

		try {
			alerter.loadAlerts("C:\\Users\\rmartine\\git\\localMonitor\\monitor\\alerts\\alerts.alerts");
		} catch (ClassNotFoundException | IOException e) {
			logger.log(Level.FINE, "Cannot load alerts", e);
		}

	}

	public Alerter scanMyJobs(JTree tree) {

		Alerter alerter = new Alerter();
		TreeUtil tu = new TreeUtil();
		ArrayList<TreeUtilSearchItem> list = tu.search(tree, ".*My Job.*");

		for (TreeUtilSearchItem searchItem : list) {

			Alert alert = new Alert(searchItem.getNode().getIdentifier(), searchItem.getNode(), searchItem.getPath(),
					Condition.NOT_EQUAL, searchItem.getNode().getStates());

			alerter.addAlert(alert);

		}
		return alerter;
	}

	public void addAlert(Alert alert) {
		alerter.addAlert(alert);
	}

	public void evaluateAlerts(Alerter alerter) {
		this.alerter.compare(alerter);
	}

	public void saveAlerts() {
		// TODO mainProps variable for path

		try {
			alerter.saveAlerts("C:\\Users\\rmartine\\git\\localMonitor\\monitor\\alerts\\alerts.alerts");
		} catch (IOException e) {
			logger.log(Level.FINE, "Cannot save alerts", e);
		}

	}

	public SGE_MonitorProp getMainProps() {
		return mainProps;
	}

	public void setMainProps(SGE_MonitorProp mainProps) {
		this.mainProps = mainProps;
	}

}
