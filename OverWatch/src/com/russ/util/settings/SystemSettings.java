/**
  * Copyright (c) 2018 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.russ.util.settings;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

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
	private Properties mainProps = null;
	private Properties logProps = null;
	private String logPropsFilePath = null;
	private boolean mainPropertiesFileExist = false;
	private String version = "0";

	public final static String GREATER_THAN = "GREATER_THAN";
	public final static String LESS_THAN = "LESS_THAN";
	public final static String EQUALS = "EQUALS";

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

	public SystemSettings() {
	}

	/**
	 * Constructs a new {@code MainSettings} with the given {@code String} file path
	 * of the system {@link java.util.Properties} file.
	 * 
	 * @param propsFilePath The {@code String} file path for system
	 *                      {@link java.util.Properties} file.
	 * 
	 * @throws IOException        if the file cannot be located or accessed
	 * @throws URISyntaxException
	 */
	public SystemSettings(String propsFilePath) throws IOException, URISyntaxException {
		logger.entering(sourceClass, "MainSettings", propsFilePath);

		loadProps(propsFilePath);

		logger.exiting(logPropsFilePath, "MainSettings");
	}

	/**
	 * Returns the {@link java.util.Properties}
	 * 
	 * @return {@link java.util.Properties}
	 */
	public Properties getMainProps() {

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
	public void setMainProp(Properties mainProps) {

		this.mainProps = mainProps;
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

	public SGE_MonitorProp loadDefaultProps(SGE_MonitorProp defaultProps, String filePath) {

		Properties props = loadDefaultProps(filePath, SGE_MonitorPropConst.ansysVersion);

		defaultProps.putAll(props);

		mainProps = defaultProps;

		return (SGE_MonitorProp) mainProps;
	}

	/**
	 * 
	 * @param defaultProps
	 * @param filePath
	 * @return
	 */
	public Properties loadDefaultProps(String filePath, String version) {
		logger.entering(sourceClass, "loadDefaultProps");

		Properties props = new Properties();

		try {
			
			props = loadProps(filePath);
			logger.fine("Loaded " + version + " version");
			setVersion(props.getProperty(version, "0"));
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
	 * @throws IOException        if the file cannot be located or accessed
	 * @throws URISyntaxException
	 */
	public Properties loadProps(String filePath) throws IOException, URISyntaxException {
		logger.entering(sourceClass, "loadProps");

		mainProps = loadPropertyfile(filePath);

		logger.exiting(sourceClass, "loadProps", mainProps);

		return mainProps;
	}

	/**
	 * Loads the log {@code Properties} with the given {@code String} file path.
	 * 
	 * @param filePath {@code String} file path
	 * 
	 * @return {@link java.util.Properties}
	 * 
	 * @throws IOException        if the file cannot be located or accessed
	 * @throws URISyntaxException
	 */
	public Properties loadLogProperty(String filePath) throws IOException, URISyntaxException {
		logger.entering(filePath, "retrieveLogProperty");

		logProps = loadPropertyfile(filePath);

		logger.exiting(sourceClass, "retrieveLogProperty", logProps);

		return logProps;
	}

	/**
	 * Loads the {@code Properties} with the given {@code String} file path.
	 * 
	 * @param filePath {@code String} file path
	 * 
	 * @return {@link java.util.Properties}
	 * 
	 * @throws IOException        if the file cannot be located or accessed
	 * @throws URISyntaxException
	 */
	public Properties loadPropertyfile(String filePath) throws IOException, URISyntaxException {
		logger.entering(filePath.toString(), "retrievePropertyfile", filePath);

		Properties props = new Properties();

		InputStream inStream = retrieveFileStream(filePath);

		logger.fine("Loading properties file " + filePath.toString());
		props.load(inStream);
		inStream.close();

		logger.exiting(filePath.toString(), "retrievePropertyfile", props);
		return props;
	}

	public void savePropertyFile(String filePath, String comments) throws IOException, URISyntaxException {
		logger.entering(sourceClass, "savePropertyFile");
		if (getMainProps() != null) {

			savePropertyFile(getMainProps(), filePath, comments);

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
	 */
	public void savePropertyFile(Properties props, String filePath, String comments)
			throws IOException, URISyntaxException {
		logger.entering(sourceClass, "savePropertyFile");

		FileWriter writer = retrieveFileWriter(filePath);
		props.store(writer, comments);
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
	 * 
	 * @param strFilePath
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public FileWriter retrieveFileWriter(String strFilePath) throws IOException, URISyntaxException {
		Path filePath = Paths.get(strFilePath);
		return retrieveFileWriter(filePath);
	}

	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public FileWriter retrieveFileWriter(Path path) throws IOException, URISyntaxException {
		path = ResourceLoader.writeFile(path, true);
		FileWriter writer = new FileWriter(path.toString());
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

		filePath = ResourceLoader.getFile(filePath, true);
		inStream = Files.newInputStream(filePath);

		return inStream;

	}

	/**
	 * 
	 * @param firstVersion
	 * @param secondVersion
	 * @param token
	 * @return
	 */
	public String compareVersions(String firstVersion, String secondVersion, String token) {
		logger.entering(sourceClass, "compareVersions");

		firstVersion = nullChecker(firstVersion, "0");

		secondVersion = nullChecker(secondVersion, "0");

		int intFirstVersion = getVersionInt(firstVersion, token);

		int intSecondVerion = getVersionInt(secondVersion, token);

		int intFirstVersionSize = (firstVersion.split(token)).length;

		int intSecondVersionSize = (secondVersion.split(token)).length;

		int sizeDifference = Math.abs(intFirstVersionSize - intSecondVersionSize);

		int multiple = (int) Math.pow(10, sizeDifference);

		if (intFirstVersionSize < intSecondVersionSize) {

			intFirstVersion = intFirstVersion * multiple;
		} else {

			intSecondVerion = intSecondVerion * multiple;
		}

		String result = "";

		if (intFirstVersion > intSecondVerion) {

			result = GREATER_THAN;
		}
		if (intSecondVerion > intFirstVersion) {

			result = LESS_THAN;
		}
		if (intSecondVerion == intFirstVersion) {

			result = EQUALS;
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

	/**
	 * 
	 * @param version
	 * @param token
	 * @return
	 */
	public int getVersionInt(String version, String token) {

		if (version == null || version.equals("")) {
			version = new String("0");
		}

		String[] versionSplit = version.split(token);

		int sum = 0;
		int e = 0;
		for (int i = versionSplit.length - 1; i >= 0; i--) {

			sum += Integer.valueOf(versionSplit[i]) * Math.pow(10, e);
			e += 1;
		}
		return sum;
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
