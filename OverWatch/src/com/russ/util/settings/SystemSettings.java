/**
  * Copyright (c) 2018 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.russ.util.settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfigurationLayout;
import org.apache.commons.configuration2.ex.ConfigurationException;

import com.ansys.cluster.monitor.settings.PropUtil;
import com.ansys.cluster.monitor.settings.PropUtil.Compare;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.ansys.cluster.monitor.settings.SGE_MonitorPropConst;
import com.russ.util.nio.ResourceLoader;

/**
 * The {@code MainSettings} class is responsible for locating the system and log
 * {@link java.util.Properties} files.
 * 
 * @author Russ Martinez
 * @since 1.0
 *
 */
public class SystemSettings {
	private String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	private SGE_MonitorProp mainProps = null;
	private Properties logProps = null;
	private String logPropsFilePath = null;
	private boolean mainPropertiesFileExist = false;
	private String version = "0";
	private String versionToken;
	private PropUtil propUtil = new PropUtil();

	public SystemSettings() {
	}

	public SystemSettings(SGE_MonitorProp mainProps, String versionToken) {

		this.mainProps = mainProps;
		this.versionToken = versionToken;

	}

	public boolean getMainPropertiesFileExist() {
		return mainPropertiesFileExist;
	}

	public static String getUserHome() {
		String userHome = null;

		if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			userHome = System.getenv("APPDATA");
		} else {
			userHome = System.getProperty("user.home");
		}

		return userHome;
	}

	public static String getTempDir() {

		return System.getProperty("java.io.tmpdir");
	}

	public SGE_MonitorProp loadSettings(String propsFilePath, String minimalVersion, String propComments) {
		try {

			mainProps = loadDefaultProps(propsFilePath, minimalVersion, propComments, versionToken);

			logger.info("**Loading log manager**");

			loadManager(propsFilePath);

		} catch (IOException | URISyntaxException | ConfigurationException e) {

			logger.log(Level.INFO, "Could not load properties file " + propsFilePath, e);

		}

		return mainProps;
	}

	public SGE_MonitorProp loadDefaultProps(String propsFilePath, String minimalVersion, String propComments,
			String token) throws IOException, URISyntaxException, ConfigurationException {
		logger.entering(sourceClass, "loadDefaultProps");

		logger.fine("Loading system settings");
		SGE_MonitorProp readProps = (SGE_MonitorProp) loadPropertiesFile(propsFilePath);

		if (!getMainPropertiesFileExist()) {

			logger.fine("Cannot load settings, using default settings.");
			savePropertyFile(mainProps, propsFilePath);
		} else {

			logger.fine("Checking version");
			Compare comparsionResult = compareVersions(minimalVersion, readProps.getMonitorVersion(), token);

			if (Compare.GREATER_THAN == comparsionResult) {

				logger.fine("Version: " + readProps.getMonitorVersion() + " is not compatible, will be upgrade.");
				mainProps = mergeProps(readProps);
				savePropertyFile(mainProps, propsFilePath);

			} else {

				mainProps = readProps;
			}
		}

		logger.exiting(sourceClass, "loadDefaultProps");

		return mainProps;
	}

	/**
	 * Constructs a new {@code MainSettings} with the given {@code String} file path
	 * of the system {@link java.util.Properties} file.
	 * 
	 * @param propsFilePath The {@code String} file path for system
	 *                      {@link java.util.Properties} file.
	 * 
	 * @throws IOException            if the file cannot be located or accessed
	 * @throws URISyntaxException
	 * @throws ConfigurationException
	 */
	public SystemSettings(String propsFilePath) throws IOException, URISyntaxException, ConfigurationException {
		logger.entering(sourceClass, "MainSettings", propsFilePath);

		loadProps(propsFilePath);

		logger.exiting(logPropsFilePath, "MainSettings");
	}

	/**
	 * Returns the {@link java.util.Properties}
	 * 
	 * @return {@link java.util.Properties}
	 */
	public SGE_MonitorProp getMainProps() {

		return mainProps;
	}

	/**
	 * Sets the {@link java.util.Properties}
	 * 
	 * @param ekmProps
	 * 
	 *                 {@link java.util.Properties}
	 * 
	 */
	public void setMainProp(SGE_MonitorProp mainProps) {

		this.mainProps = mainProps;
	}

	public Compare compareVersions(String minimalVersion, String currentVersion, String token) {

		return propUtil.compareVersions(minimalVersion, currentVersion, token);
	}

	public SGE_MonitorProp mergeProps(SGE_MonitorProp oldProps) {

		List<String> listRegex = mainProps.getDataRetentionRegexLst();
		PropUtil pUtil = new PropUtil();
		SGE_MonitorProp props = pUtil.mergeProps(mainProps, oldProps, listRegex);

		return props;
	}

	/**
	 * Returns the log {@link java.util.Properties}
	 * 
	 * @return {@link java.util.Properties}
	 */
	public Properties getLogProperties() {

		return logProps;
	}

	/**
	 * Sets the log {@link java.util.Properties}
	 * 
	 * @param logProps {@link java.util.Properties}
	 */
	public void setLogproperties(Properties logProps) {

		this.logProps = logProps;
	}

	public PropertiesConfiguration loadPropertiesFile(String filePath) throws ConfigurationException {

		SGE_MonitorProp props = loadPropertiesFile(filePath, SGE_MonitorPropConst.ansysVersion);
		return props;
	}

	/**
	 * 
	 * @param defaultProps
	 * @param filePath
	 * @return
	 * @throws ConfigurationException
	 */
	public SGE_MonitorProp loadPropertiesFile(String filePath, String versionKey) throws ConfigurationException {
		logger.entering(sourceClass, "loadPropertiesFile");

		SGE_MonitorProp props = null;

		try {

			props = loadProps(filePath);
			setVersion(props.getString(versionKey, "0"));
			logger.fine("Loaded " + version + " version");
			mainPropertiesFileExist = true;

		} catch (IOException | URISyntaxException e) {

			logger.log(Level.WARNING, "Error loading settings file: " + filePath, e);
			logger.log(Level.WARNING, "Using default settings");

			setVersion(version);
		}
		logger.exiting(sourceClass, "loadDefaultProps");
		return props;
	}

	/**
	 * Loads the {@code Properties} with the given {@code String} file path.
	 * 
	 * @param filePath {@code String} file path
	 * 
	 * @return {@link java.util.Properties}
	 * 
	 * @throws IOException            if the file cannot be located or accessed
	 * @throws URISyntaxException
	 * @throws ConfigurationException
	 */
	public SGE_MonitorProp loadProps(String filePath) throws IOException, URISyntaxException, ConfigurationException {
		logger.entering(sourceClass, "loadProps");

		SGE_MonitorProp props = loadPropertyfile(filePath);

		logger.exiting(sourceClass, "loadProps");

		return props;
	}

	/**
	 * Loads the {@code Properties} with the given {@code String} file path.
	 * 
	 * @param filePath {@code String} file path
	 * 
	 * @return {@link java.util.Properties}
	 * 
	 * @throws IOException            if the file cannot be located or accessed
	 * @throws URISyntaxException
	 * @throws ConfigurationException
	 */
	public SGE_MonitorProp loadPropertyfile(String filePath)
			throws IOException, URISyntaxException, ConfigurationException {
		logger.entering(filePath.toString(), "retrievePropertyfile", filePath);

		BufferedReader reader = retrieveReader(filePath);
		SGE_MonitorProp props = new SGE_MonitorProp(reader);

		logger.fine("Loading properties file " + filePath.toString());
		reader.close();

		logger.exiting(filePath.toString(), "retrievePropertyfile", props);
		return props;
	}

	public void savePropertyFile(String filePath) throws IOException, URISyntaxException, ConfigurationException {
		logger.entering(sourceClass, "savePropertyFile");
		if (getMainProps() != null) {

			savePropertyFile(getMainProps(), filePath);

		} else {

			logger.info("Cannot save settings");
		}
		logger.exiting(sourceClass, "savePropertyFile");
	}

	/**
	 * 
	 * @param props
	 * @param filePath
	 * @param comments
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ConfigurationException
	 */
	public void savePropertyFile(SGE_MonitorProp props, String filePath)
			throws IOException, ConfigurationException, URISyntaxException {
		logger.entering(sourceClass, "savePropertyFile");
		logger.fine("Saving default settings to " + filePath);

		BufferedWriter writer = retrieveFileWriter(filePath);
		PropertiesConfigurationLayout layout = props.getLayout();
		layout.save(props, writer);
		logger.fine("Saved " + filePath);
		writer.flush();
		writer.close();

		logger.exiting(sourceClass, "savePropertyFile");
	}

	/**
	 * Loads the {@code Properties} with the given {@code String} file path to the
	 * {@link java.util.logging.LogManager}.
	 * 
	 * @param strFilePath {@code String} file path
	 * 
	 * @return {@link java.util.logging.LogManager}
	 * 
	 * @throws IOException        if the file cannot be located or accessed
	 * @throws URISyntaxException
	 */
	public LogManager loadManager(String strFilePath) throws IOException, URISyntaxException {

		InputStream ins = retrieveFileStream(strFilePath);

		LogManager.getLogManager().readConfiguration(ins);

		ins.close();

		return LogManager.getLogManager();

	}

	/**
	 * Returns the {@code java.io.BufferedReader} with the given
	 * {@link java.lang.String} file path.
	 * 
	 * 
	 * @param filePath
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public BufferedReader retrieveReader(String strFilePath) throws IOException, URISyntaxException {
		Path filePath = Paths.get(strFilePath);
		return retrieveReader(filePath);
	}

	/**
	 * Returns the {@code java.io.BufferedReader} with the given
	 * {@link java.nio.file.Path} file path.
	 * 
	 * 
	 * @param filePath
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public BufferedReader retrieveReader(Path filePath) throws IOException, URISyntaxException {

		Path path = ResourceLoader.getFile(filePath, false, false);
		BufferedReader reader = new BufferedReader(new FileReader(path.toString()));
		return reader;
	}

	/**
	 * 
	 * @param strFilePath
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public BufferedWriter retrieveFileWriter(String strFilePath) throws IOException, URISyntaxException {
		Path filePath = Paths.get(strFilePath);
		Path path = ResourceLoader.getFile(filePath, true, false);
		return retrieveFileWriter(path);
	}

	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public BufferedWriter retrieveFileWriter(Path path) throws IOException, URISyntaxException {
		path = ResourceLoader.getFile(path, true, false);
		BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()));
		return writer;
	}

	/**
	 * Returns the {@code java.io.InputStream} with the given {@code String} file
	 * path.
	 * 
	 * @param strFilePath {@code String} file path
	 * 
	 * @return {@link java.io.InputStream}
	 * 
	 * @throws IOException        if the file cannot be located or accessed
	 * @throws URISyntaxException
	 */
	public InputStream retrieveFileStream(String strFilePath) throws IOException, URISyntaxException {

		Path filePath = Paths.get(strFilePath);
		return retrieveFileStream(filePath);

	}

	/**
	 * Returns the {@code java.io.InputStream} with the given
	 * {@link java.nio.file.Path} file path.
	 * 
	 * @param filePath {@code Path}
	 * 
	 * @return {@link java.io.InputStream}
	 * 
	 * @throws IOException        if the file cannot be located or accessed
	 * @throws URISyntaxException
	 */
	public InputStream retrieveFileStream(Path filePath) throws IOException, URISyntaxException {
		logger.entering(sourceClass, "retrievePropertyfile", filePath);
		InputStream inStream;

		filePath = ResourceLoader.getFile(filePath, true, false);
		inStream = Files.newInputStream(filePath);

		return inStream;

	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}
