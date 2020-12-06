/**
 * 
 */
package com.ansys.cluster.monitor.settings;

import java.util.regex.Pattern;

/**
 * @author rmartine
 *
 */
public class MonitorArgsSettings extends ArgsSettings {

	/**
	 * @param args
	 */
	public MonitorArgsSettings(String[] args) {
		super(args);
		process();
		
	}

	/**
	 * Detects if certain arguments have been parsed and signals if the main program
	 * should be skipped.
	 * 
	 * @return true - if the main program should be skipped.
	 */
	public boolean skipMainProgram() {
		boolean result = false;

		if (hasExports() || hasHelp() || hasAdminPass()) {

			result = true;
		}

		return result;
	}

	public boolean hasAdminPass() {
		return exist(SGE_MonitorPropConst.args_prop_key_admin_pass);
	}

	public String getAdminPass() {
		return getValue(SGE_MonitorPropConst.args_prop_key_admin_pass).get(0);
	}

	public boolean hasMode() {
		return exist(SGE_MonitorPropConst.args_prop_key_mode);
	}

	public String getMode() {
		return getValue(SGE_MonitorPropConst.args_prop_key_mode).get(0);
	}

	public boolean hasXMLFilePath() {
		return exist(SGE_MonitorPropConst.args_prop_key_export_cluster_sum_xml_file_path);
	}

	public String getXMLFilePath() {
		return getValue(SGE_MonitorPropConst.args_prop_key_export_cluster_sum_xml_file_path).get(0);
	}

	public boolean hasXMLOutput() {
		return exist(SGE_MonitorPropConst.args_prop_key_export_cluster_sum_xml_out);
	}

	public String getXMLOutput() {
		return getValue(SGE_MonitorPropConst.args_prop_key_export_cluster_sum_xml_out).get(0);
	}

	public boolean hasSerialPath() {
		return exist(SGE_MonitorPropConst.args_prop_key_export_serial_file_path);
	}

	public String getSerialPath() {
		return getValue(SGE_MonitorPropConst.args_prop_key_export_serial_file_path).get(0);
	}

	public boolean hasSerialOutput() {
		return exist(SGE_MonitorPropConst.args_prop_key_export_serial_out);
	}

	public String getSerialOutput() {
		return getValue(SGE_MonitorPropConst.args_prop_key_export_serial_out).get(0);
	}

	public boolean hasDataRequestMethod() {
		return exist(SGE_MonitorPropConst.args_prop_key_data_request_method);
	}

	public String getDataRequestMethod() {

		return getValue(SGE_MonitorPropConst.args_prop_key_data_request_method).get(0);
	}

	public boolean hasExports() {

		return regex("export.*", Pattern.CASE_INSENSITIVE);

	}

	/**
	 * Returns the help menu
	 * 
	 * @return {@code String} Help menu
	 */
	public String getHelpMessage() {

		StringBuilder msg = new StringBuilder();

		msg.append("Help Message\n This utility will query the designated cluster\n");
		msg.append("\n-").append(SGE_MonitorPropConst.args_prop_key_export_cluster_sum_xml_file_path)
				.append(" the file path of the xml output. (The console will not display)\n");

		msg.append("\n-").append(SGE_MonitorPropConst.args_prop_key_export_serial_file_path)
				.append(" the file path of the serial object. (The console will not display)\n");

		msg.append("\n-").append(SGE_MonitorPropConst.args_prop_key_data_request_method)
				.append(" the method used to request the data. Methods: HTTP, FILE, CMD ")
				.append("(The console will not display)\n");

		msg.append("\n-help Prints out this message.");

		return msg.toString();

	}

	public boolean hasHelp() {
		return hasHelp(SGE_MonitorPropConst.args_prop_key_help);
	}

}
