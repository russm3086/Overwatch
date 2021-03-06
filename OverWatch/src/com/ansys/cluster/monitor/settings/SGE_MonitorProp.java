/**
 * 
 */
package com.ansys.cluster.monitor.settings;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.russ.util.AbstractProp;
import com.russ.util.settings.SystemSettings;

/**
 * @author rmartine
 *
 */
public class SGE_MonitorProp extends AbstractProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2489755592055764704L;

	/**
	 * 
	 */
	public SGE_MonitorProp() {
		// TODO Auto-generated constructor stub

		setMonitorVersion(SGE_DataConst.app_version);

		// Specify the handlers to create in the root logger
		setLogHandlers("java.util.logging.ConsoleHandler, java.util.logging.FileHandler");

		// Set the default logging level for the root logger
		setLogDefaultLevel("INFO");

		// default file output
		String tmpFilePath = SystemSettings.getTempDir();
		setLogFileHandlerPattern(tmpFilePath + "/Ansys/OverWatch/logs/log%g.log");
		setLogFileHandlerLimit("104857600");
		setLogFileHandlerCount("10");
		setLogFileHandlerFormatter("java.util.logging.SimpleFormatter");
		setLogFileHandlerAppend(true);
		setLogFileHandlerLevel("FINEST");
		
		setLogConsoleHandlerLevel("FINEST");

		setLogAnsysLevel("INFO");

		// GUI
		setOS_LookAndFeel(false);
		setFrameScreenRatio(2);
		setGuiTimer(true);
		setGuiTimerDelay(5);
		setGuiTimerDelayTimeUnit("minutes");
		setGuiTreeExpansionLevel(3);

		// Paths
		String homeFilePath = SystemSettings.getUserHome();
		setDirEtc(homeFilePath + "/Ansys/OverWatch/etc");

		// Data
		setClusterQueueOmissions("dcv e09n48");
		setClusterDataJobList(
				"JAT_scaled_usage_list JB_env_list " + "JB_submission_command_line JAT_granted_resources_list "
						+ "JB_job_args JAT_granted_destin_identifier_list");

		// Connections
		setClusterConnectionRequestMethod("GET");
		setClusterConnectionRequestReadBuffer(131072);
		setClusterConnectionRequestTimeOut(5000);
		setClusterConnectionRequestReadTimeOut(5000);
		setClusterConnectionRequestContentType("application/xml");
		setClusterConnectionRetries(3);
		setClusterConnectionRetriesDelay(5);
		setClusterConnectionRetriesDelayTimeUnit("Seconds");
		setClusterIndex(0);

		setClusterName(0, "Otterfing");
		setClusterConnectionDetailedJobsUrl(0, "http://ottsimportal2.ansys.com:5080/alljobs/details/xml");
		setClusterConnectionSummaryJobsUrl(0, "http://ottsimportal2.ansys.com:5080/alljobs/xml");
		setClusterConnectionHostUrl(0, "http://ottsimportal2.ansys.com:5080/allhosts/xml");

		setClusterName(1, "CDC");
		setClusterConnectionDetailedJobsUrl(1, "http://cdcsimportal1.ansys.com:5080/alljobs/details/xml");
		setClusterConnectionSummaryJobsUrl(1, "http://cdcsimportal1.ansys.com:5080/alljobs/xml");
		setClusterConnectionHostUrl(1, "http://cdcsimportal1.ansys.com:5080/allhosts/xml");

		setClusterName(2, "Pune");
		setClusterConnectionDetailedJobsUrl(2, "http://punsimportal2.ansys.com:5080/allhosts/xml");
		setClusterConnectionSummaryJobsUrl(2, "http://punsimportal2.ansys.com:5080/alljobs/xml");
		setClusterConnectionHostUrl(2, "http://punsimportal2.ansys.com:5080/alljobs/details/xml");

		// Job
		setJobIdleThreshold(.1);
	}

	/**
	 * @param defaults
	 */
	public SGE_MonitorProp(Properties defaults) {
		super(defaults);
		// TODO Auto-generated constructor stub
	}

	public void setMonitorVersion(String version) {
		setLogProperty(SGE_MonitorPropConst.ansysVersion, version);
	}

	public String getMonitorVersion() {
		return getLogProperty(SGE_MonitorPropConst.ansysVersion);
	}

	public String getClusterConnectionRequestMethod() {
		return getLogProperty(SGE_MonitorPropConst.connectionRequestMethod);
	}

	public void setClusterConnectionRequestMethod(String requestMethod) {
		setLogProperty(SGE_MonitorPropConst.connectionRequestMethod, requestMethod);
	}

	public int getClusterConnectionRequestTimeOut() {
		return getIntProperty(SGE_MonitorPropConst.connectionConnectTimeout);
	}

	public void setClusterConnectionRequestTimeOut(int timeOut) {
		setIntProperty(SGE_MonitorPropConst.connectionConnectTimeout, timeOut);
	}

	public int getClusterConnectionRequestReadTimeOut() {
		return getIntProperty(SGE_MonitorPropConst.connectionReadTimeout);
	}

	public void setClusterConnectionRequestReadTimeOut(int timeOut) {
		setIntProperty(SGE_MonitorPropConst.connectionReadTimeout, timeOut);
	}

	public int getClusterConnectionRequestReadBuffer() {
		return getIntProperty(SGE_MonitorPropConst.connectionReadBuffer);
	}

	public void setClusterConnectionRequestReadBuffer(int buffer) {
		setIntProperty(SGE_MonitorPropConst.connectionReadBuffer, buffer);
	}

	public String getClusterConnectionRequestContentType() {
		return getLogProperty(SGE_MonitorPropConst.connectionContentType);
	}

	public void setClusterConnectionRequestContentType(String contentType) {
		setLogProperty(SGE_MonitorPropConst.connectionContentType, contentType);
	}

	public String getClusterConnectionDetailedJobsUrl(int item) {
		return getLogProperty(
				SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionDetailedJobsURLSuffix);
	}

	public void setClusterConnectionDetailedJobsUrl(int item, String detailedJobsUrl) {
		setLogProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionDetailedJobsURLSuffix,
				detailedJobsUrl);
	}

	public String getClusterConnectionSummaryJobsUrl(int item) {
		return getLogProperty(
				SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionSummaryJobsURLSuffix);
	}

	public void setClusterConnectionSummaryJobsUrl(int item, String sumJobsUrl) {
		setLogProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionSummaryJobsURLSuffix,
				sumJobsUrl);
	}

	public String getClusterConnectionHostUrl(int item) {
		return getLogProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionHostUrlSuffix);
	}

	public void setClusterConnectionHostUrl(int item, String hostUrl) {
		setLogProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionHostUrlSuffix,
				hostUrl);
	}

	public String getClusterType(int item) {
		return getLogProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionTypeSuffix);
	}

	public void setClusterType(int item, String type) {
		setLogProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionTypeSuffix, type);
	}

	public String getClusterName(int item) {
		return getLogProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.nameSuffix);
	}

	public void setClusterName(int item, String name) {
		setLogProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.nameSuffix, name);
	}

	public int getClusterIndex() {
		return getIntProperty(SGE_MonitorPropConst.clusterIndex);
	}

	public void setClusterIndex(int count) {
		setIntProperty(SGE_MonitorPropConst.clusterIndex, count);
	}

	public int getGuiTreeExpansionLevel() {
		return getIntProperty(SGE_MonitorPropConst.guiTreeExpansionLevel);
	}

	public void setGuiTreeExpansionLevel(int level) {
		setIntProperty(SGE_MonitorPropConst.guiTreeExpansionLevel, level);
	}

	public TimeUnit getGuiTimerDelayTimeUnitTU() {

		String str = getLogProperty(SGE_MonitorPropConst.guiTimerDelayTimeUnit);
		str = str.toUpperCase().trim();

		return TimeUnit.valueOf(str);
	}

	public void setGuiTimerDelayTimeUnit(TimeUnit unit) {
		setGuiTimerDelayTimeUnit(unit);
	}

	public String getGuiTimerDelayTimeUnit() {
		return getLogProperty(SGE_MonitorPropConst.guiTimerDelayTimeUnit);
	}

	public void setGuiTimerDelayTimeUnit(String timeUnit) {
		setLogProperty(SGE_MonitorPropConst.guiTimerDelayTimeUnit, timeUnit);
	}

	public int getGuiTimerDelay() {
		return getIntProperty(SGE_MonitorPropConst.guiTimerDelay);
	}

	public void setGuiTimerDelay(int delay) {
		setIntProperty(SGE_MonitorPropConst.guiTimerDelay, delay);
	}

	public boolean getGuiTimer() {
		return getBoolProperty(SGE_MonitorPropConst.guiTimer);
	}

	public void setGuiTimer(boolean timerOn) {
		setBoolProperty(SGE_MonitorPropConst.guiTimer, timerOn);
	}

	public void setFrameScreenRatio(int ratio) {
		setIntProperty(SGE_MonitorPropConst.screenRatio, ratio);
	}

	public int getFrameScreenRatio() {
		return getIntProperty(SGE_MonitorPropConst.screenRatio);
	}

	public void setOS_LookAndFeel(boolean lookAndFeel) {
		setBoolProperty(SGE_MonitorPropConst.useOS_LookAndFeel, lookAndFeel);
	}

	public boolean getOS_LookAndFeel() {
		return getBoolProperty(SGE_MonitorPropConst.useOS_LookAndFeel, false);
	}

	public String getLogHandlers() {
		return getLogProperty(SGE_MonitorPropConst.logHandler);
	}

	public void setLogHandlers(String handlers) {
		setLogProperty(SGE_MonitorPropConst.logHandler, handlers);
	}

	public String getLogDefaultLevel() {
		return getLogProperty(SGE_MonitorPropConst.defaultLevel);
	}

	public void setLogDefaultLevel(String defaultLevel) {
		setLogProperty(SGE_MonitorPropConst.defaultLevel, defaultLevel);
	}

	public String getLogFileHandlerPattern() {
		return getLogProperty(SGE_MonitorPropConst.logFileHandlerPattern);
	}

	public void setLogFileHandlerPattern(String filePattern) {
		setLogProperty(SGE_MonitorPropConst.logFileHandlerPattern, filePattern);
	}

	public String getLogFileHandlerLimit() {
		return getLogProperty(SGE_MonitorPropConst.logFileHandlerLimit);
	}

	public void setLogFileHandlerLimit(String limit) {
		setLogProperty(SGE_MonitorPropConst.logFileHandlerLimit, limit);
	}

	public String getLogFileHandlerCount() {
		return getLogProperty(SGE_MonitorPropConst.logFileHandlerCount);
	}

	public void setLogFileHandlerCount(String count) {
		setLogProperty(SGE_MonitorPropConst.logFileHandlerCount, count);
	}

	public String getLogFileHandlerFormatter() {
		return getLogProperty(SGE_MonitorPropConst.logFileHandlerFormatter);
	}

	public void setLogFileHandlerFormatter(String formatter) {
		setLogProperty(SGE_MonitorPropConst.logFileHandlerFormatter, formatter);
	}

	public String getLogFileHandlerLevel() {
		return getLogProperty(SGE_MonitorPropConst.logFileHandlerLevel);
	}

	public void setLogFileHandlerLevel(String level) {
		setLogProperty(SGE_MonitorPropConst.logFileHandlerLevel, level);
	}

	public boolean getLogFileHandlerAppend() {
		return getBoolProperty(SGE_MonitorPropConst.logFileHandlerAppend);
	}

	public void setLogFileHandlerAppend(boolean append) {
		setBoolProperty(SGE_MonitorPropConst.logFileHandlerLevel, append);
	}

	public String getLogConsoleHandlerLevel() {
		return getLogProperty(SGE_MonitorPropConst.logConsoleHandlerLevel);
	}

	public void setLogConsoleHandlerLevel(String level) {
		setLogProperty(SGE_MonitorPropConst.logConsoleHandlerLevel, level);
	}

	public String getLogConsoleHandlerFormatter() {
		return getLogProperty(SGE_MonitorPropConst.logFileHandlerFormatter);
	}

	public void setLogConsoleHandlerFormatter(String formatter) {
		setLogProperty(SGE_MonitorPropConst.logConsoleHandlerLevel, formatter);
	}

	public String getLogAnsysLevel() {
		return getLogProperty(SGE_MonitorPropConst.logAnsysLevel);
	}

	public void setLogAnsysLevel(String level) {
		setLogProperty(SGE_MonitorPropConst.logAnsysLevel, level);
	}

	public String getDirEtc() {
		return getLogProperty(SGE_MonitorPropConst.sgeMonitor_dir_etc);
	}

	public void setDirEtc(String etc) {
		setLogProperty(SGE_MonitorPropConst.sgeMonitor_dir_etc, etc);
	}

	public double getJobIdleThreshold() {
		return getDoubleProperty(SGE_MonitorPropConst.jobIdleThreshold);
	}

	public void setJobIdleThreshold(double value) {

		setDoubleProperty(SGE_MonitorPropConst.jobIdleThreshold, value);
	}

	public void setClusterConnectionRetries(int value) {

		setIntProperty(SGE_MonitorPropConst.connectionRetries, value);
	}

	public int getClusterConnectionRetries() {

		return getIntProperty(SGE_MonitorPropConst.connectionRetries);
	}

	public void setClusterConnectionRetriesDelay(int value) {
		setIntProperty(SGE_MonitorPropConst.connectionRetriesDelay, value);
	}

	public int getClusterConnectionRetriesDelay() {
		return getIntProperty(SGE_MonitorPropConst.connectionRetriesDelay);
	}

	public void setClusterConnectionRetriesDelayTimeUnit(String value) {

		setLogProperty(SGE_MonitorPropConst.connectionRetriesDelayTimeUnit, value);
	}

	public String getClusterConnectionRetriesDelayTimeUnit() {
		return getLogProperty(SGE_MonitorPropConst.connectionRetriesDelayTimeUnit);
	}

	public TimeUnit getClusterConnectionRetriesDelayTimeUnitTU() {

		String str = getLogProperty(SGE_MonitorPropConst.connectionRetriesDelayTimeUnit);
		str = str.toUpperCase().trim();

		return TimeUnit.valueOf(str);
	}

	public String getClusterQueueOmissions() {
		return getLogProperty(SGE_MonitorPropConst.clusterQueueOmissions);
	}

	public void setClusterQueueOmissions(String queues) {
		setLogProperty(SGE_MonitorPropConst.clusterQueueOmissions, queues);
	}

	public String getClusterDataJobList() {
		return getLogProperty(SGE_MonitorPropConst.clusterJobListXtag);
	}

	public void setClusterDataJobList(String tags) {
		setLogProperty(SGE_MonitorPropConst.clusterJobListXtag, tags);
	}

}
