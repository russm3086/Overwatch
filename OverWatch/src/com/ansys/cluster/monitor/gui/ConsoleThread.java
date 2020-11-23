/**
 * 
 */
package com.ansys.cluster.monitor.gui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.main.SGE_DataConst;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * @author rmartine
 *
 */
public class ConsoleThread implements Runnable {
	private String sourceClass = this.getClass().getName();
	private Logger logger = Logger.getLogger(sourceClass);
	SGE_MonitorProp mainProps;

	/**
	 * 
	 */
	public ConsoleThread(SGE_MonitorProp mainProps) {
		this.mainProps = mainProps;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			new Console(SGE_DataConst.app_name + " v. " + SGE_DataConst.app_version + " Beta", mainProps);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Error", e);
		}

	}

}
