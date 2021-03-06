/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.ansys.cluster.monitor.data.factory.Exporter;
import com.ansys.cluster.monitor.gui.Console;
import com.ansys.cluster.monitor.settings.MonitorArgsSettings;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.test.Test;
import com.russ.util.FileStructure;
import com.russ.util.settings.SystemSettings;

/**
 * 
 * @author rmartine
 * @since
 */
public class Main {
	private static String sourceClass = Test.class.getName();
	private static Logger logger = Logger.getLogger(sourceClass);
	private static SystemSettings systemSettings = null;
	private static String propComments;
	private static String propsFilePath;

	/**
	 * 
	 */
	public Main() {
		// TODO Create mechanism to check properties file version
		// TODO Create mechanism to update existing Properties file
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Logger logger = Logger.getLogger(sourceClass);
		logger.setLevel(Level.FINE);

		SGE_MonitorProp mainProps = new SGE_MonitorProp();

		propComments = SGE_DataConst.app_name + " v. " + SGE_DataConst.app_version;

		propsFilePath = SystemSettings.getUserHome() + "/" + ClusterMonitorConst.DefaultPropertiesPath;

		String token = "\\.";

		// propsFilePath =
		// "C:\\Users\\rmartine\\git\\localMonitor\\monitor\\res\\etc\\settings.properties";

		try {
			logger.info("****Started****");

			MonitorArgsSettings argsSetting = new MonitorArgsSettings(args);

			if (!argsSetting.skipMainProgram()) {

				if (argsSetting.hasPropFiles()) {

					logger.info("Changing properties path from " + propsFilePath + " to " + argsSetting.getPropFiles());
					propsFilePath = argsSetting.getPropFiles();
				}

				logger.info("**Checking application file structure**");

				FileStructure fileStruc = new FileStructure(mainProps);
				fileStruc.FileStructureCheck();

				logger.info("**Loading log and program settings**");

				mainProps = getSystemSettings(mainProps, token);

				if (mainProps.getOS_LookAndFeel()) {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}

				new Console(SGE_DataConst.app_name + " v. " + SGE_DataConst.app_version, mainProps);

				/**
				 * SwingUtilities.invokeLater(new Runnable() { public void run() { try { new
				 * Console(SGE_DataConst.app_name + " v. " + SGE_DataConst.app_version,
				 * mainProps); } catch (Exception e) {
				 * Main.displayError(e.getLocalizedMessage(), Level.SEVERE); } } });
				 */
			} else {

				if (!argsSetting.hasHelp()) {

					if (argsSetting.hasXMLFilePath()) {

						Exporter exporter = new Exporter(mainProps, argsSetting.getXMLFilePath());
						exporter.Export();
					}

				} else {

					String msg = argsSetting.getHelpMessage();

					System.out.println(msg);
				}

			}

		} catch (Exception e) {

			logger.log(Level.SEVERE, "Severe Error", e);
		}

	}

	public static SGE_MonitorProp getSystemSettings(SGE_MonitorProp mainProps, String token) throws URISyntaxException {

		try {

			systemSettings = new SystemSettings();
			mainProps = loadDefaultProps(propsFilePath, SGE_DataConst.app_version, propComments, token, systemSettings);

			logger.info("**Loading log manager**");

			systemSettings.loadManager(propsFilePath);
			
		} catch (IOException e) {

			logger.log(Level.INFO, "Could not load properties file " + propsFilePath, e);

		}

		return mainProps;		
	}

	public static SGE_MonitorProp loadDefaultProps(String propsFilePath, String minimalVersion, String propComments,
			String token, SystemSettings systemSettings) throws IOException, URISyntaxException {
		logger.entering(sourceClass, "loadDefaultProps");

		logger.fine("Loading system settings");
		SGE_MonitorProp mainProps = systemSettings.loadDefaultProps(new SGE_MonitorProp(), propsFilePath);

		if (!systemSettings.getMainPropertiesFileExist()) {

			logger.fine("Cannot load settings, using default settings.");
			logger.fine("Saving default settings to " + propsFilePath);
			systemSettings.savePropertyFile(mainProps, propsFilePath, propComments);

		} else {

			logger.fine("Checking version");
			String comparsionResult = systemSettings.compareVersions(minimalVersion, mainProps.getMonitorVersion(),
					token);

			if (SystemSettings.GREATER_THAN.equalsIgnoreCase(comparsionResult)) {

				logger.fine("Version: " + mainProps.getMonitorVersion() + " is not compatible, will upgrade.");
				mainProps = new SGE_MonitorProp();
				systemSettings.savePropertyFile(mainProps, propsFilePath, propComments);
			}
		}

		logger.exiting(sourceClass, "loadDefaultProps");

		return mainProps;
	}

	public static void saveSettings() throws IOException, URISyntaxException {

		systemSettings.savePropertyFile(propsFilePath, propComments);

	}

	public static void saveSettings(SGE_MonitorProp mainProps) throws IOException, URISyntaxException {

		systemSettings.savePropertyFile(mainProps, propsFilePath, propComments);

	}

	/**
	 * Displays error messages in the GUI
	 *
	 * @param errMsg The text message to be displayed
	 * @param level  The error level of the message
	 */
	public static void displayError(String errMsg, Level level) {

		if (Level.SEVERE.equals(level)) {
			JOptionPane.showMessageDialog(null, errMsg, Level.SEVERE.getLocalizedName(), JOptionPane.ERROR_MESSAGE);
		}

		if (Level.WARNING.equals(level)) {
			JOptionPane.showMessageDialog(null, errMsg, Level.WARNING.getLocalizedName(), JOptionPane.ERROR_MESSAGE);
		}

	}

}
