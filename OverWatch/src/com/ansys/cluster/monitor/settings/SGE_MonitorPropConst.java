/**
 * 
 */
package com.ansys.cluster.monitor.settings;

/**
 * @author rmartine
 *
 */
public final class SGE_MonitorPropConst {

	/**
	 * 
	 */
	public SGE_MonitorPropConst() {
		
	}

	public static final String logHandler = "handlers";

	public static final String defaultLevel = "java.level";

	public static final String logFileHandlerPattern = "java.util.logging.FileHandler.pattern";

	public static final String logFileHandlerLimit = "java.util.logging.FileHandler.limit";

	public static final String logFileHandlerCount = "java.util.logging.FileHandler.count";

	public static final String logFileHandlerFormatter = "java.util.logging.FileHandler.formatter";

	public static final String logFileHandlerLevel = "java.util.logging.FileHandler.level";

	public static final String logFileHandlerAppend = "java.util.logging.FileHandler.append";

	public static final String logConsoleHandlerLevel = "java.util.logging.ConsoleHandler.level";

	public static final String logConsoleHandlerFormatter = "java.util.logging.ConsoleHandler.formatter";

	public static final String ansysVersion = "com.ansys.version";

	public static final String logAnsysLevel = "com.ansys.level";

	public static final String logRussLevel = "com.russ.level";

	public static final String sgeMonitor_dir_etc = "com.ansys.dir.etc";

	public static final String rootPrefix = "com.ansys.cluster.monitor.";

	public static final String updatePrefix = rootPrefix + "update.";

	public static final String dataRetentionPrefix = updatePrefix + "data.retention.";

	public static final String regexSuffix = ".regex";

	public static final String guiFrameWidth = "com.ansys.cluster.monitor.GUI.frame.width";

	public static final String guiFrameHeight = "com.ansys.cluster.monitor.GUI.frame.height";

	public static final String guiFrameX = "com.ansys.cluster.monitor.GUI.frame.x";

	public static final String guiFrameY = "com.ansys.cluster.monitor.GUI.frame.y";

	public static final String guiDeviceId = "com.ansys.cluster.monitor.GUI.frame.deviceid";

	public static final String useOS_LookAndFeel = "com.ansys.cluster.monitor.GUI.UseOS_LookAndFeel";

	public static final String screenRatio = "com.ansys.cluster.monitor.GUI.frame.screenRatio";

	public static final String guiTimer = "com.ansys.cluster.monitor.GUI.timer";

	public static final String guiTimerDelay = "com.ansys.cluster.monitor.GUI.timer.delay";

	public static final String guiTimerDelayTimeUnit = "com.ansys.cluster.monitor.GUI.timer.delay.timeUnit";

	public static final String guiTreeExpansionLevel = "com.ansys.cluster.monitor.GUI.tree.autoexpansion";

	public static final String guiFontScaling = "com.ansys.cluster.monitor.GUI.font.scaling";

	public static final String clusterPrefix = "com.ansys.cluster.monitor.cluster.";

	public static final String adminPrefix = clusterPrefix + "admin.";

	public static final String adminKey = adminPrefix + "key";

	public static final String adminPassword = adminPrefix + "password";

	public static final String clusterDataPrefix = clusterPrefix + "data.";

	public static final String clusterQueuePrefix = clusterDataPrefix + "queue.";

	public static final String clusterQueueOmissions = clusterQueuePrefix + "omissions";

	public static final String clusterQueueVisualRegex = clusterQueuePrefix + "visual.regex";

	public static final String clusterMyJobQueuePrefix = clusterQueuePrefix + "myjobs.";

	public static final String clusterMyJobQueueUserOverride = clusterMyJobQueuePrefix + "username.override";

	public static final String clusterMyJobQueueUserAlias = clusterMyJobQueuePrefix + "username.alias";

	public static final String clusterMedianQueuePrefix = clusterQueuePrefix + "median.";

	public static final String clusterMedianQueueThrowoutValue = clusterMedianQueuePrefix + "waittime.throwout.value";

	public static final String clusterJobPrefix = clusterDataPrefix + "job.";

	public static final String clusterJobDetailPrefix = clusterJobPrefix + "detail.";

	public static final String clusterJobDetailOmissions = clusterJobDetailPrefix + "omissions";

	public static final String clusterJobListPrefix = clusterDataPrefix + "list.";

	public static final String clusterJobListXtag = clusterDataPrefix + "xtags";

	public static final String clusterConnectionPrefix = clusterPrefix + "connection";

	public static final String clusterConnectionRequestPrefix = clusterConnectionPrefix + ".request";

	public static final String connectionRequestMethod = clusterConnectionRequestPrefix + ".method";

	public static final String connectionRequestCdcPrefix = clusterConnectionRequestPrefix + ".cdc";

	public static final String connectionRequestCdcPoolSize = connectionRequestCdcPrefix + ".poolsize";

	public static final String connectionRequestCdcTimeout = connectionRequestCdcPrefix + ".timeout";

	public static final String connectionRequestCdcTimeoutTimeUnit = connectionRequestCdcPrefix + ".timeout.timeunit";

	public static final String connectionConnectTimeout = clusterConnectionRequestPrefix + ".connect.timeout";

	public static final String connectionReadTimeout = clusterConnectionRequestPrefix + ".read.timeout";

	public static final String connectionReadBuffer = clusterConnectionRequestPrefix + ".read.buffer";

	public static final String connectionContentType = clusterConnectionRequestPrefix + ".content-type";

	public static final String connectionRetries = clusterConnectionPrefix + ".retries";

	public static final String connectionRetriesDelay = connectionRetries + ".delay";

	public static final String connectionRetriesDelayTimeUnit = connectionRetries + ".delay.timeUnit";

	public static final String clusterIndex = clusterPrefix + "index";

	public static final String nameSuffix = ".name";

	public static final String timeZoneIdSuffix = ".timezoneid";

	public static final String timeZoneId = ".timezoneid";

	public static final String connectionClusterUrlSuffix = ".connection.cluster.URL";

	public static final String connectionTypeSuffix = ".connection.type";

	public static final String connectionHostUrlSuffix = ".connection.hosts.URL";

	public static final String connectionQuotaUrlSuffix = ".connection.quota.URL";

	public static final String connectionSummaryJobsUrlSuffix = ".connection.summaryJobs.URL";

	public static final String connectionDetailedJobsUrlSuffix = ".connection.detailedJobs.URL";

	public static final String connectionFullDetailedJobsUrlSuffix = ".connection.fullDetailedJobs.URL";

	public static final String connectionQueueUrlSuffix = ".connection.queue.URL";

	public static final String connectionUseFullDetailedJobsSuffix = ".useFullDetailedJobs";

	public static final String connectionShellCmdSuffix = ".connection.shell.CMD";

	public static final String connectionShellArgsCmdSuffix = ".connection.shell.args.CMD";

	public static final String connectionHostCmdSuffix = ".connection.hosts.CMD";

	public static final String connectionQuotaCmdSuffix = ".connection.quota.CMD";

	public static final String connectionSummaryJobsCmdSuffix = ".connection.summaryJobs.CMD";

	public static final String connectionDetailedJobsCmdSuffix = ".connection.detailedJobs.CMD";

	public static final String connectionFullDetailedJobsCmdSuffix = ".connection.fullDetailedJobs.CMD";

	public static final String connectionQueueCmdSuffix = ".connection.queue.CMD";

	public static final String jobIdleThreshold = "com.ansys.monitor.job.idle.threshold";

	public static final String args_prop_key_export_cluster_sum_xml_file_path = "exportClusterSummaryXmlPath";

	public static final String args_prop_key_export_cluster_sum_xml_out = "exportClusterSummaryXmlOut";

	public static final String args_prop_key_data_request_method = "requestmethod";

	public static final String args_prop_key_export_serial_file_path = "exportSerialObjectPath";

	public static final String args_prop_key_export_serial_out = "exportSerialObjectOut";

	public static final String args_prop_key_mode = "mode";

	public static final String args_prop_key_admin_pass = "adminpasswordencrypt";

	/**
	 * The arguments properties help key
	 */
	public static String args_prop_key_help = "help";

}
