/**
 * 
 */
package com.ansys.cluster.monitor.settings;

import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfigurationLayout;
import org.apache.commons.configuration2.ex.ConfigurationException;

import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.russ.util.settings.SystemSettings;

/**
 * @author rmartine
 *
 */
public class SGE_MonitorProp extends PropertiesConfiguration {

	public SGE_MonitorProp(final Reader in) throws ConfigurationException, IOException {
		super.read(in);

	}

	public SGE_MonitorProp(final Configuration c) {
		super.append(c);
	}

	/**
	 * 
	 */
	public SGE_MonitorProp() {
		// TODO Auto-generated constructor stub

		SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z");
		String strDate = formatter.format(new Date());

		PropertiesConfigurationLayout layout = getLayout();
		setHeader("OverWatch " + strDate);

		setMonitorVersion(SGE_DataConst.app_version);
		layout.setComment(SGE_MonitorPropConst.ansysVersion, "Designates the version of the settings");

		layout.setBlancLinesBefore(SGE_MonitorPropConst.logHandler, 2);

		// Specify the handlers to create in the root logger
		setLogHandlers("java.util.logging.ConsoleHandler, java.util.logging.FileHandler");
		layout.setComment(SGE_MonitorPropConst.logHandler,
				"Logging Settings\nSpecify the handlers to create in the root logger");

		// Set the default logging level for the root logger
		setLogDefaultLevel("INFO");
		layout.setComment(SGE_MonitorPropConst.defaultLevel, "\nSet the default logging level for the root logger");

		// default file output
		String tmpFilePath = SystemSettings.getTempDir();
		setLogFileHandlerPattern(tmpFilePath + "/Ansys/OverWatch/logs/log%g.log");
		setLogFileHandlerLimit("104857600");
		layout.setComment(SGE_MonitorPropConst.logFileHandlerLimit,
				"\nThe maximum size of the file, in bytes. If this is 0, there is no limit.");
		setLogFileHandlerCount("10");
		setLogFileHandlerFormatter("java.util.logging.SimpleFormatter");
		setLogFileHandlerAppend(true);
		setLogFileHandlerLevel("FINEST");
		setLogConsoleHandlerLevel("FINEST");

		setLogAnsysLevel("INFO");
		layout.setComment(SGE_MonitorPropConst.logAnsysLevel, "\nSet the default logging level for the ansys logger");

		// GUI
		setOS_LookAndFeel(false);
		layout.setBlancLinesBefore(SGE_MonitorPropConst.useOS_LookAndFeel, 2);
		layout.setComment(SGE_MonitorPropConst.useOS_LookAndFeel,
				"GUI Settings\nSpecifies whether to use the OS Look and Feel.");

		setFrameScreenRatio(2);
		setFrameHeight(0);
		setFrameWidth(0);
		setGuiTimer(true);
		setGuiTimerDelay(1);
		setGuiTimerDelayTimeUnit("minutes");
		setGuiTreeExpansionLevel(3);

		// Paths
		String homeFilePath = SystemSettings.getUserHome();
		setDirEtc(homeFilePath + "/Ansys/OverWatch/etc");
		layout.setBlancLinesBefore(SGE_MonitorPropConst.sgeMonitor_dir_etc, 2);
		layout.setComment(SGE_MonitorPropConst.sgeMonitor_dir_etc, "Path Settings\nSpecifies the setting dir.");

		// Data
		setClusterQueueOmissions("dcv e09n48");
		setClusterQueueVisualRegex(".*vnc.*|.*dcv.*");
		layout.setBlancLinesBefore(SGE_MonitorPropConst.clusterQueueOmissions, 2);
		layout.setComment(SGE_MonitorPropConst.clusterQueueOmissions,
				"Data Filteration\nSpecifies data manipulation settings.");
		setClusterDataJobList(
				"JAT_scaled_usage_list JB_env_list " + "JB_submission_command_line JAT_granted_resources_list "
						+ "JB_job_args JAT_granted_destin_identifier_list");

		setJobDetailOmissions("JB_execution_time JB_deadline JB_notify JB_type "
				+ "JB_reserve JB_priority JB_jobshare JB_shell_list JB_verify JB_checkpoint_attr "
				+ "JB_checkpoint_interval JB_restart JB_merge_stderr JB_hard_resource_list "
				+ "JB_mail_options JB_mail_list JB_pe JB_pe_range JB_ja_structure "
				+ "JB_verify_suitable_queues JB_soft_wallclock_gmt JB_hard_wallclock_gmt "
				+ "JB_override_tickets JB_ar JB_ja_task_concurrency JB_ja_task_concurrency_all "
				+ "JB_binding JB_is_binary JB_no_shell JB_is_array JB_is_immediate "
				+ "JB_mbind JB_preemption JB_supplementary_group_list JB_env_list");

		// Connections
		// Retries
		setClusterConnectionRetries(3);
		layout.setBlancLinesBefore(SGE_MonitorPropConst.connectionRetries, 2);
		layout.setComment(SGE_MonitorPropConst.connectionRetries, "Cluster Connection settings\nRetries");
		setClusterConnectionRetriesDelay(5);
		setClusterConnectionRetriesDelayTimeUnit("Seconds");

		setClusterConnectionRequestReadBuffer(131072);
		layout.setBlancLinesBefore(SGE_MonitorPropConst.connectionReadBuffer, 2);
		layout.setComment(SGE_MonitorPropConst.connectionReadBuffer, "connection settings");

		setClusterConnectionRequestTimeOut(5000);
		setClusterConnectionRequestReadTimeOut(5000);
		setClusterConnectionRequestMethod("HTTP");
		layout.setComment(SGE_MonitorPropConst.connectionRequestMethod, "\nConnection request method=HTTP, FILE, CMD");
		setClusterConnectionRequestContentType("application/xml");
		layout.setComment(SGE_MonitorPropConst.connectionContentType,
				"\napplication/xml application/json application/overwatch");

		setClusterIndex(0);
		layout.setComment(SGE_MonitorPropConst.clusterIndex,
				"\nCluster Server information\nLast cluster configuration");

		setClusterName(0, "Otterfing");
		setClusterConnectionDetailedJobsUrl(0, "http://ottsimportal3.ansys.com:7878/alljobdetails");
		setClusterConnectionSummaryJobsUrl(0, "http://ottsimportal3.ansys.com:7878/alljobs");
		setClusterConnectionHostUrl(0, "http://ottsimportal3.ansys.com:7878/allnodes");

		setClusterName(1, "CDC");
		setClusterConnectionDetailedJobsUrl(1, "http://cdcsimportal1.ansys.com:5080/alljobs/details/xml");
		setClusterConnectionSummaryJobsUrl(1, "http://cdcsimportal1.ansys.com:5080/alljobs/xml");
		setClusterConnectionHostUrl(1, "http://cdcsimportal1.ansys.com:5080/allhosts/xml");

		setClusterName(2, "Pune");
		setClusterConnectionDetailedJobsUrl(2, "http://punsimportal2.ansys.com:5080/alljobs/details/xml");
		setClusterConnectionSummaryJobsUrl(2, "http://punsimportal2.ansys.com:5080/alljobs/xml");
		setClusterConnectionHostUrl(2, "http://punsimportal2.ansys.com:5080/allhosts/xml");

		setClusterConnectionShellCmd(0, "/bin/sh");
		layout.setBlancLinesBefore(
				SGE_MonitorPropConst.clusterPrefix + 0 + SGE_MonitorPropConst.connectionShellCmdSuffix, 2);
		layout.setComment(SGE_MonitorPropConst.clusterPrefix + 0 + SGE_MonitorPropConst.connectionShellCmdSuffix,
				"Cluster commandline settings");

		setClusterConnectionShellArgsCmd(0, "-c");
		setClusterConnectionHostCmd(0, "qhost -q -xml");
		setClusterConnectionSummaryJobsCmd(0, "qstat -u '*' -xml");
		setClusterConnectionDetailedJobsCmd(0, "qstat -j '*' -xml");

		// Job
		setJobIdleThreshold(.05);
		layout.setComment(SGE_MonitorPropConst.jobIdleThreshold, "\nThe settings that determines when a job is idle");
	}

	public void setMonitorVersion(String version) {
		setProperty(SGE_MonitorPropConst.ansysVersion, version);
	}

	public String getMonitorVersion() {
		return getString(SGE_MonitorPropConst.ansysVersion);
	}

	public String getClusterConnectionRequestMethod() {
		return getString(SGE_MonitorPropConst.connectionRequestMethod);
	}

	public void setClusterConnectionRequestMethod(String requestMethod) {
		setProperty(SGE_MonitorPropConst.connectionRequestMethod, requestMethod);
	}

	public int getClusterConnectionRequestTimeOut() {
		return getInt(SGE_MonitorPropConst.connectionConnectTimeout);
	}

	public void setClusterConnectionRequestTimeOut(int timeOut) {
		setProperty(SGE_MonitorPropConst.connectionConnectTimeout, timeOut);
	}

	public int getClusterConnectionRequestReadTimeOut() {
		return getInt(SGE_MonitorPropConst.connectionReadTimeout);
	}

	public void setClusterConnectionRequestReadTimeOut(int timeOut) {
		setProperty(SGE_MonitorPropConst.connectionReadTimeout, timeOut);
	}

	public int getClusterConnectionRequestReadBuffer() {
		return getInt(SGE_MonitorPropConst.connectionReadBuffer);
	}

	public void setClusterConnectionRequestReadBuffer(int buffer) {
		setProperty(SGE_MonitorPropConst.connectionReadBuffer, buffer);
	}

	public String getClusterConnectionRequestContentType() {
		return getString(SGE_MonitorPropConst.connectionContentType);
	}

	public void setClusterConnectionRequestContentType(String contentType) {
		setProperty(SGE_MonitorPropConst.connectionContentType, contentType);
	}

	public String getClusterConnectionClusterUrl(int item) {
		return getString(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionClusterUrlSuffix);
	}

	public void setClusterConnectionClusterUrl(int item, String clusterUrl) {
		setProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionClusterUrlSuffix,
				clusterUrl);
	}

	public String getClusterConnectionDetailedJobsUrl(int item) {
		return getString(
				SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionDetailedJobsUrlSuffix);
	}

	public void setClusterConnectionDetailedJobsUrl(int item, String detailedJobsUrl) {
		setProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionDetailedJobsUrlSuffix,
				detailedJobsUrl);
	}

	public String getClusterConnectionSummaryJobsUrl(int item) {
		return getString(
				SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionSummaryJobsUrlSuffix);
	}

	public void setClusterConnectionSummaryJobsUrl(int item, String sumJobsUrl) {
		setProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionSummaryJobsUrlSuffix,
				sumJobsUrl);
	}

	public String getClusterConnectionHostUrl(int item) {
		return getString(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionHostUrlSuffix);
	}

	public void setClusterConnectionHostUrl(int item, String hostUrl) {
		setProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionHostUrlSuffix, hostUrl);
	}

	public String getClusterConnectionShellCmd(int item) {
		return getString(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionShellCmdSuffix);
	}

	public void setClusterConnectionShellCmd(int item, String shellCmd) {
		setProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionShellCmdSuffix,
				shellCmd);
	}

	public String getClusterConnectionShellArgCmd(int item) {
		return getString(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionShellArgsCmdSuffix);
	}

	public void setClusterConnectionShellArgsCmd(int item, String shellArgs) {
		setProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionShellArgsCmdSuffix,
				shellArgs);
	}

	public String getClusterConnectionHostCmd(int item) {
		return getString(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionHostCmdSuffix);
	}

	public void setClusterConnectionHostCmd(int item, String hostCmd) {
		setProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionHostCmdSuffix, hostCmd);
	}

	public String getClusterConnectionSummaryJobsCmd(int item) {
		return getString(
				SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionSummaryJobsCmdSuffix);
	}

	public void setClusterConnectionSummaryJobsCmd(int item, String sumJobsCmd) {
		setProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionSummaryJobsCmdSuffix,
				sumJobsCmd);
	}

	public String getClusterConnectionDetailedJobsCmd(int item) {
		return getString(
				SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionDetailedJobsCmdSuffix);
	}

	public void setClusterConnectionDetailedJobsCmd(int item, String detailedJobsCmd) {
		setProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionDetailedJobsCmdSuffix,
				detailedJobsCmd);
	}

	public String getClusterType(int item) {
		return getString(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionTypeSuffix);
	}

	public void setClusterType(int item, String type) {
		setProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.connectionTypeSuffix, type);
	}

	public String getClusterName(int item) {
		return getString(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.nameSuffix);
	}

	public void setClusterName(int item, String name) {
		setProperty(SGE_MonitorPropConst.clusterPrefix + item + SGE_MonitorPropConst.nameSuffix, name);
	}

	public int getClusterIndex() {
		return getInt(SGE_MonitorPropConst.clusterIndex);
	}

	public void setClusterIndex(int count) {
		setProperty(SGE_MonitorPropConst.clusterIndex, count);
	}

	public int getGuiTreeExpansionLevel() {
		return getInt(SGE_MonitorPropConst.guiTreeExpansionLevel);
	}

	public void setGuiTreeExpansionLevel(int level) {
		setProperty(SGE_MonitorPropConst.guiTreeExpansionLevel, level);
	}

	public TimeUnit getGuiTimerDelayTimeUnitTU() {

		String str = getString(SGE_MonitorPropConst.guiTimerDelayTimeUnit);
		str = str.toUpperCase().trim();

		return TimeUnit.valueOf(str);
	}

	public void setGuiTimerDelayTimeUnit(TimeUnit unit) {
		setGuiTimerDelayTimeUnit(unit);
	}

	public String getGuiTimerDelayTimeUnit() {
		return getString(SGE_MonitorPropConst.guiTimerDelayTimeUnit);
	}

	public void setGuiTimerDelayTimeUnit(String timeUnit) {
		setProperty(SGE_MonitorPropConst.guiTimerDelayTimeUnit, timeUnit);
	}

	public int getGuiTimerDelay() {
		return getInt(SGE_MonitorPropConst.guiTimerDelay);
	}

	public void setGuiTimerDelay(int delay) {
		setProperty(SGE_MonitorPropConst.guiTimerDelay, delay);
	}

	public boolean getGuiTimer() {
		return getBoolean(SGE_MonitorPropConst.guiTimer);
	}

	public void setGuiTimer(boolean timerOn) {
		setProperty(SGE_MonitorPropConst.guiTimer, timerOn);
	}

	public void setFrameWidth(double width) {
		setProperty(SGE_MonitorPropConst.guiFrameWidth, width);
	}

	public double getFrameWidth() {
		return getDouble(SGE_MonitorPropConst.guiFrameWidth, 0);
	}

	public void setFrameHeight(double height) {
		setProperty(SGE_MonitorPropConst.guiFrameHeight, height);
	}

	public double getFrameHeight() {
		return getDouble(SGE_MonitorPropConst.guiFrameHeight, 0);
	}

	public void setFrameScreenRatio(int ratio) {
		setProperty(SGE_MonitorPropConst.screenRatio, ratio);
	}

	public int getFrameScreenRatio() {
		return getInt(SGE_MonitorPropConst.screenRatio);
	}

	public void setOS_LookAndFeel(boolean lookAndFeel) {
		setProperty(SGE_MonitorPropConst.useOS_LookAndFeel, lookAndFeel);
	}

	public boolean getOS_LookAndFeel() {
		return getBoolean(SGE_MonitorPropConst.useOS_LookAndFeel, false);
	}

	public String getLogHandlers() {
		return getString(SGE_MonitorPropConst.logHandler);
	}

	public void setLogHandlers(String handlers) {
		setProperty(SGE_MonitorPropConst.logHandler, handlers);
	}

	public String getLogDefaultLevel() {
		return getString(SGE_MonitorPropConst.defaultLevel);
	}

	public void setLogDefaultLevel(String defaultLevel) {
		setProperty(SGE_MonitorPropConst.defaultLevel, defaultLevel);
	}

	public String getLogFileHandlerPattern() {
		return getString(SGE_MonitorPropConst.logFileHandlerPattern);
	}

	public void setLogFileHandlerPattern(String filePattern) {
		setProperty(SGE_MonitorPropConst.logFileHandlerPattern, filePattern);
	}

	public String getLogFileHandlerLimit() {
		return getString(SGE_MonitorPropConst.logFileHandlerLimit);
	}

	public void setLogFileHandlerLimit(String limit) {
		setProperty(SGE_MonitorPropConst.logFileHandlerLimit, limit);
	}

	public String getLogFileHandlerCount() {
		return getString(SGE_MonitorPropConst.logFileHandlerCount);
	}

	public void setLogFileHandlerCount(String count) {
		setProperty(SGE_MonitorPropConst.logFileHandlerCount, count);
	}

	public String getLogFileHandlerFormatter() {
		return getString(SGE_MonitorPropConst.logFileHandlerFormatter);
	}

	public void setLogFileHandlerFormatter(String formatter) {
		setProperty(SGE_MonitorPropConst.logFileHandlerFormatter, formatter);
	}

	public String getLogFileHandlerLevel() {
		return getString(SGE_MonitorPropConst.logFileHandlerLevel);
	}

	public void setLogFileHandlerLevel(String level) {
		setProperty(SGE_MonitorPropConst.logFileHandlerLevel, level);
	}

	public boolean getLogFileHandlerAppend() {
		return getBoolean(SGE_MonitorPropConst.logFileHandlerAppend);
	}

	public void setLogFileHandlerAppend(boolean append) {
		setProperty(SGE_MonitorPropConst.logFileHandlerLevel, append);
	}

	public String getLogConsoleHandlerLevel() {
		return getString(SGE_MonitorPropConst.logConsoleHandlerLevel);
	}

	public void setLogConsoleHandlerLevel(String level) {
		setProperty(SGE_MonitorPropConst.logConsoleHandlerLevel, level);
	}

	public String getLogConsoleHandlerFormatter() {
		return getString(SGE_MonitorPropConst.logFileHandlerFormatter);
	}

	public void setLogConsoleHandlerFormatter(String formatter) {
		setProperty(SGE_MonitorPropConst.logConsoleHandlerLevel, formatter);
	}

	public String getLogAnsysLevel() {
		return getString(SGE_MonitorPropConst.logAnsysLevel);
	}

	public void setLogAnsysLevel(String level) {
		setProperty(SGE_MonitorPropConst.logAnsysLevel, level);
	}

	public String getDirEtc() {
		return getString(SGE_MonitorPropConst.sgeMonitor_dir_etc);
	}

	public void setDirEtc(String etc) {
		setProperty(SGE_MonitorPropConst.sgeMonitor_dir_etc, etc);
	}

	public double getJobIdleThreshold() {
		return getDouble(SGE_MonitorPropConst.jobIdleThreshold);
	}

	public void setJobIdleThreshold(double value) {

		setProperty(SGE_MonitorPropConst.jobIdleThreshold, value);
	}

	public void setClusterConnectionRetries(int value) {

		setProperty(SGE_MonitorPropConst.connectionRetries, value);
	}

	public int getClusterConnectionRetries() {

		return getInt(SGE_MonitorPropConst.connectionRetries);
	}

	public void setClusterConnectionRetriesDelay(int value) {
		setProperty(SGE_MonitorPropConst.connectionRetriesDelay, value);
	}

	public int getClusterConnectionRetriesDelay() {
		return getInt(SGE_MonitorPropConst.connectionRetriesDelay);
	}

	public void setClusterConnectionRetriesDelayTimeUnit(String value) {

		setProperty(SGE_MonitorPropConst.connectionRetriesDelayTimeUnit, value);
	}

	public String getClusterConnectionRetriesDelayTimeUnit() {
		return getString(SGE_MonitorPropConst.connectionRetriesDelayTimeUnit);
	}

	public TimeUnit getClusterConnectionRetriesDelayTimeUnitTU() {

		String str = getString(SGE_MonitorPropConst.connectionRetriesDelayTimeUnit);
		str = str.toUpperCase().trim();

		return TimeUnit.valueOf(str);
	}

	public String getClusterQueueVisualRegex() {
		return getString(SGE_MonitorPropConst.clusterQueueVisualRegex);
	}

	public void setClusterQueueVisualRegex(String regex) {
		setProperty(SGE_MonitorPropConst.clusterQueueVisualRegex, regex);
	}

	public String getClusterQueueOmissions() {
		return getString(SGE_MonitorPropConst.clusterQueueOmissions);
	}

	public void setClusterQueueOmissions(String queues) {
		setProperty(SGE_MonitorPropConst.clusterQueueOmissions, queues);
	}

	public String getClusterDataJobList() {
		return getString(SGE_MonitorPropConst.clusterJobListXtag);
	}

	public void setClusterDataJobList(String tags) {
		setProperty(SGE_MonitorPropConst.clusterJobListXtag, tags);
	}

	public String getJobDetailOmissions() {
		return getString(SGE_MonitorPropConst.clusterJobDetailOmissions);
	}

	public void setJobDetailOmissions(String tags) {
		setProperty(SGE_MonitorPropConst.clusterJobDetailOmissions, tags);
	}

}
