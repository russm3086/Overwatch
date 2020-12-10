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
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws ConfigurationException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws IOException, URISyntaxException, ConfigurationException {
		LoggingUtil.setLevel(Level.FINE);

		String token = "\\.";
		systemSettings = new SystemSettings(new SGE_MonitorProp(), token);

		propComments = SGE_DataConst.app_name + " v. " + SGE_DataConst.app_version;

		propsFilePath = SystemSettings.getUserHome() + "/" + SGE_DataConst.DefaultPropertiesPath;

		try {
			logger.info("****Started****");
			logger.info("****" + propComments + "****");

			MonitorArgsSettings argsSetting = new MonitorArgsSettings(args);

			SGE_MonitorProp mainProps = systemSettings.loadSettings(propsFilePath, SGE_DataConst.app_version,
					propComments);

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

	public static void saveSettings() throws IOException, URISyntaxException, ConfigurationException {

		systemSettings.savePropertyFile(propsFilePath);
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
