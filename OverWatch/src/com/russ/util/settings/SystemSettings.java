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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfigurationLayout;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.OverrideCombiner;

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

	public PropertiesConfiguration loadDefaultProps(SGE_MonitorProp defaultProps, String filePath)
			throws ConfigurationException {

		SGE_MonitorProp props = loadDefaultProps(filePath, SGE_MonitorPropConst.ansysVersion);

		if (props != null) {

			CombinedConfiguration combined = new CombinedConfiguration();
			combined.setNodeCombiner(new OverrideCombiner());
			combined.addConfiguration(props);
			combined.addConfiguration(defaultProps);

			SGE_MonitorProp finalFile = new SGE_MonitorProp(combined);
	
			mainProps = finalFile;

		} else {
			mainProps = defaultProps;
		}

		return mainProps;
	}

	/**
	 * 
	 * @param defaultProps
	 * @param filePath
	 * @return
	 * @throws ConfigurationException
	 */
	public SGE_MonitorProp loadDefaultProps(String filePath, String version) throws ConfigurationException {
		logger.entering(sourceClass, "loadDefaultProps");

		SGE_MonitorProp props = null;

		try {

			props = loadProps(filePath);
			logger.fine("Loaded " + version + " version");
			setVersion(props.getString(version, "0"));
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

		mainProps = loadPropertyfile(filePath);

		logger.exiting(sourceClass, "loadProps", mainProps);

		return mainProps;
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

	public void savePropertyFile(String filePath, String comments)
			throws IOException, URISyntaxException, ConfigurationException {
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
