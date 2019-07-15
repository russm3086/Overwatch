/**
 * @author rmartine
 * Dec 10, 2017
 */
package com.russ.util.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a skeletal foundation of the {@code Arguments}
 * implementation.
 * 
 * <p>
 * To implement an argument, the programmer needs only to extend this class and
 * execute the <tt>process</tt> method, which will parse the {@code String}
 * command line and populate the {@link java.util.Map<String, List<String>>}
 * object.
 * </p>
 * 
 * @author Russ Martinez
 * @since 1.0
 *
 */
public abstract class AbstractArguments {
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	protected Map<String, List<String>> params = new HashMap<>();
	protected String[] args = null;

	/**
	 * The sole constructor and the only point to store the command line arguments.
	 * 
	 * @param args The {@code String[]} arguments
	 */
	public AbstractArguments(String[] args) {

		this.args = args;

	}

	/**
	 * Detects if certain arguments have been parsed and signals if the main program
	 * should be skipped.
	 * 
	 * @return true - if the main program should be skipped.
	 */
	public abstract boolean skipMainProgram();

	/**
	 * Verifies if the properties argument has been given
	 * 
	 * @return {@code true} if the properties argument has been given
	 * 
	 */
	public abstract boolean hasPropFiles();

	// TDO Need to finish
	/**
	 * This methods
	 */
	public void process() {
		logger.entering(sourceClass, "process");
		List<String> options = null;
		for (int i = 0; i < args.length; i++) {
			final String a = args[i];

			if (a.charAt(0) == '-') {
				if (a.length() < 2) {
					logger.severe("Error at argument " + a);
					return;
				}

				options = new ArrayList<>();
				String key = a.substring(1).toLowerCase();

				logger.finest("Key: " + key + " Value: " + options);
				params.put(a.substring(1).toLowerCase(), options);
			} else if (options != null) {
				options.add(a);
			} else {
				logger.severe("Illegal parameter usage");
				return;
			}
		}

		logger.exiting(sourceClass, "process");

	}

	/**
	 * Verifies if the {@code String} key exist
	 * 
	 * @param key {@code String} the key for the {@code Map} argument
	 * 
	 * @return {@code true} if the key exist
	 */
	public boolean exist(String key) {
		logger.entering(sourceClass, "exist", key);
		boolean result = false;

		if (getParams().get(key.toLowerCase()) != null) {
			result = true;
		}

		logger.exiting(sourceClass, "exist", result);
		return result;

	}

	public boolean regex(String regex, int caseSensitive) {
		boolean result = false;
		for (Entry<String, List<String>> entry : params.entrySet()) {

			Pattern mypattern = Pattern.compile(regex, caseSensitive);
			Matcher mymatcher = mypattern.matcher(entry.getKey());
			result = mymatcher.find();

			if (result)
				break;
		}
		return result;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	protected List<String> getValue(String key) {

		return getParams().get(key.toLowerCase());
	}

	/**
	 * @return the params
	 */
	protected Map<String, List<String>> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	protected void setParams(Map<String, List<String>> params) {
		this.params = params;
	}

	/**
	 * Verifies if the properties argument has been given
	 * 
	 * @return {@code true} if the properties argument has been given
	 * 
	 */
	public abstract String getPropFiles();

	/**
	 * Returns the help menu
	 * 
	 * @return {@code String} Help menu
	 */
	protected abstract String getHelpMessage();

	/**
	 * Verify if the help argument has been given.
	 * 
	 * @return {@code true} if the help argument has been given.
	 */
	public abstract boolean hasHelp(String string);

}
