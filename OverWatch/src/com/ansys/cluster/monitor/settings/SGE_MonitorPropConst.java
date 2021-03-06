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
		// TODO Auto-generated constructor stub
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

	public static final String sgeMonitor_dir_etc = "com.ansys.dir.etc";

	public static final String useOS_LookAndFeel = "com.ansys.cluster.monitor.GUI.UseOS_LookAndFeel";

	public static final String screenRatio = "com.ansys.cluster.monitor.GUI.frame.screenRatio";

	public static final String guiTimer = "com.ansys.cluster.monitor.GUI.timer";

	public static final String guiTimerDelay = "com.ansys.cluster.monitor.GUI.timer.delay";

	public static final String guiTimerDelayTimeUnit = "com.ansys.cluster.monitor.GUI.timer.delay.timeUnit";

	public static final String guiTreeExpansionLevel = "com.ansys.cluster.monitor.GUI.tree.autoexpansion";

	public static final String clusterPrefix = "com.ansys.cluster.monitor.cluster.";

	public static final String clusterDataPrefix = clusterPrefix + "data.";

	public static final String clusterQueuePrefix = clusterDataPrefix + "queue.";

	public static final String clusterQueueOmissions = clusterQueuePrefix + "omissions";

	public static final String clusterJobPrefix = clusterDataPrefix + "job.";

	public static final String clusterJobListPrefix = clusterDataPrefix + "list.";

	public static final String clusterJobListXtag = clusterDataPrefix + "xtags";

	public static final String clusterConnectionPrefix = clusterPrefix + "connection";

	public static final String clusterConnectionRequestPrefix = clusterConnectionPrefix + ".request";

	public static final String connectionRequestMethod = clusterConnectionRequestPrefix + ".method";

	public static final String connectionConnectTimeout = clusterConnectionRequestPrefix + ".connect.timeout";

	public static final String connectionReadTimeout = clusterConnectionRequestPrefix + ".read.timeout";

	public static final String connectionReadBuffer = clusterConnectionRequestPrefix + ".read.buffer";

	public static final String connectionContentType = clusterConnectionRequestPrefix + ".content-type";

	public static final String connectionRetries = clusterConnectionPrefix + ".retries";

	public static final String connectionRetriesDelay = connectionRetries + ".delay";

	public static final String connectionRetriesDelayTimeUnit = connectionRetries + ".delay.timeUnit";

	public static final String clusterIndex = clusterPrefix + "index";

	public static final String nameSuffix = ".name";

	public static final String connectionTypeSuffix = ".connection.type";

	public static final String connectionHostUrlSuffix = ".connection.hosts.URL";

	public static final String connectionSummaryJobsURLSuffix = ".connection.summaryJobs.URL";

	public static final String connectionDetailedJobsURLSuffix = ".connection.detailedJobs.URL";

	public static final String jobIdleThreshold = "com.ansys.monitor.job.idle.threshold";

	public static final String args_prop_key_xml_file_path = "xmlpath";
	
	/**
	 * The arguments properties help key
	 */
	public static String args_prop_key_help = "help";

	
}
