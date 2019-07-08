/**
 * 
 */
package com.ansys.cluster.monitor.settings;

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
		// TODO Auto-generated constructor stub
	}

	/**
	 * Detects if certain arguments have been parsed and signals if the main program
	 * should be skipped.
	 * 
	 * @return true - if the main program should be skipped.
	 */
	public boolean skipMainProgram() {
		boolean result = false;

		if (hasXMLFilePath() || hasHelp()) {

			result = true;
		}

		return result;
	}

	public boolean hasXMLFilePath() {
		return exist(SGE_MonitorPropConst.args_prop_key_xml_file_path);
	}
	
	public String getXMLFilePath() {
		
		return getParams().get(SGE_MonitorPropConst.args_prop_key_xml_file_path).get(0);
		
	}
	
	/**
	 * Returns the help menu
	 * 
	 * @return {@code String} Help menu
	 */
	public String getHelpMessage() {

		String msg = "Help Message\n This utility will query the designated cluster\n";

		msg += "\n-xmlfile the file path of the xml output. (The console will not display)\n";

		msg += "\n-help Prints out this message.";

		return msg;

	}

	public boolean hasHelp() {
		return hasHelp(SGE_MonitorPropConst.args_prop_key_help);
	}

}
