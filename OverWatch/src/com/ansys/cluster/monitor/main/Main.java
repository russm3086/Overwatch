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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.ansys.cluster.monitor.data.factory.Exporter;
import com.ansys.cluster.monitor.gui.AdminMenuConfig;
import com.ansys.cluster.monitor.gui.ConsoleThread;
import com.ansys.cluster.monitor.settings.MonitorArgsSettings;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.FileStructure;
import com.russ.util.gui.DisplayTool;
import com.russ.util.settings.LoggingUtil;
import com.russ.util.settings.SystemSettings;

/**
 * 
 * @author rmartine
 * @since
 */
public class Main {
	private static String sourceClass = Main.class.getName();
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
	 * @throws ConfigurationException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws IOException, URISyntaxException, ConfigurationException {

		Logger logger = Logger.getLogger(sourceClass);
		LoggingUtil.setLevel(Level.FINE);

		SGE_MonitorProp mainProps = new SGE_MonitorProp();

		propComments = SGE_DataConst.app_name + " v. " + SGE_DataConst.app_version;

		propsFilePath = SystemSettings.getUserHome() + "/" + SGE_DataConst.DefaultPropertiesPath;

		String token = "\\.";

		// propsFilePath = "res/etc/settings.orig.properties";

		try {
			logger.info("****Started****");
			logger.info("****" + propComments + "****");

			MonitorArgsSettings argsSetting = new MonitorArgsSettings(args);

			mainProps = getSystemSettings(mainProps, token);

			if (!argsSetting.skipMainProgram()) {

				if (argsSetting.hasPropFiles()) {

					logger.info("Changing properties path from " + propsFilePath + " to " + argsSetting.getPropFiles());
					propsFilePath = argsSetting.getPropFiles();
				}

				logger.info("**Checking application file structure**");

				FileStructure fileStruc = new FileStructure(mainProps);
				fileStruc.FileStructureCheck();

				logger.info("**Loading log and program settings**");

				fontScaling(mainProps.getGuiFontScaling());

				if (mainProps.getOS_LookAndFeel()) {
					logger.info("**Setting Look and Feel to " + UIManager.getSystemLookAndFeelClassName() + "**");
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}

				SwingUtilities.invokeLater(new ConsoleThread(mainProps));

			} else {

				if (!argsSetting.hasHelp()) {

					if (argsSetting.hasDataRequestMethod())
						mainProps.setClusterConnectionRequestMethod(argsSetting.getDataRequestMethod());

					if (argsSetting.hasXMLFilePath()) {

						Exporter exporter = new Exporter(mainProps, argsSetting.getXMLFilePath());
						exporter.exportXMLFile();
					}

					if (argsSetting.hasSerialPath()) {

						Exporter exporter = new Exporter(mainProps, argsSetting.getSerialPath());
						exporter.exportSerialFile();
					}

					if ((argsSetting.hasXMLOutput())) {

						Exporter exporter = new Exporter(mainProps);
						exporter.exportXmlOut();
					}

					if ((argsSetting.hasSerialOutput())) {

						Exporter exporter = new Exporter(mainProps);
						exporter.exportSerialOut();
					}

					if ((argsSetting.hasAdminPass())) {

						String adminSettings = AdminMenuConfig.createKeyPassword(argsSetting.getAdminPass());
						System.out.println(adminSettings);
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

	public static SGE_MonitorProp getSystemSettings(SGE_MonitorProp mainProps, String token)
			throws URISyntaxException, ConfigurationException {

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
			String token, SystemSettings systemSettings)
			throws IOException, URISyntaxException, ConfigurationException {
		logger.entering(sourceClass, "loadDefaultProps");

		logger.fine("Loading system settings");
		SGE_MonitorProp mainProps = (SGE_MonitorProp) systemSettings.loadPropertiesFile(new SGE_MonitorProp(),
				propsFilePath);

		if (!systemSettings.getMainPropertiesFileExist()) {

			logger.fine("Cannot load settings, using default settings.");
			logger.fine("Saving default settings to " + propsFilePath);
			systemSettings.savePropertyFile(mainProps, propsFilePath);

		} else {

			logger.fine("Checking version");
			String comparsionResult = systemSettings.compareVersions(minimalVersion, mainProps.getMonitorVersion(),
					token);

			if (SystemSettings.GREATER_THAN.equalsIgnoreCase(comparsionResult)) {

				logger.fine("Version: " + mainProps.getMonitorVersion() + " is not compatible, will be upgrade.");
				SGE_MonitorProp newMainProps = new SGE_MonitorProp();
				String version = newMainProps.getMonitorVersion();
				newMainProps.copy(mainProps);
				newMainProps.setMonitorVersion(version);
				newMainProps.setHeader(newMainProps.getHeader());
				
				mainProps = newMainProps;
				systemSettings.savePropertyFile(mainProps, propsFilePath);
			}
		}

		logger.exiting(sourceClass, "loadDefaultProps");

		return mainProps;
	}

	public static void saveSettings() throws IOException, URISyntaxException, ConfigurationException {

		systemSettings.savePropertyFile(propsFilePath, propComments);

	}

	public static void saveSettings(SGE_MonitorProp mainProps)
			throws IOException, URISyntaxException, ConfigurationException {

		systemSettings.savePropertyFile(mainProps, propsFilePath);

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

	public static void fontScaling(double scaleAdj) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {

		DisplayTool.fontScaling(scaleAdj, SGE_DataConst.app_font_max_scaling,
				(-1 * SGE_DataConst.app_font_max_scaling));
	}

}
