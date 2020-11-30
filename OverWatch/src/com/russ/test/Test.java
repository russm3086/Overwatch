/**
 * 
 */
package com.russ.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.russ.util.settings.LoggingUtil;

/**
 * @author rmartine
 *
 */
public class Test {
	private static String sourceClass = Test.class.getName();
	private static Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IllegalAccessException
	 */
	public static void main(String[] args) throws IllegalAccessException {
		// TODO Auto-generated method stub
		LoggingUtil.setLevel(Level.FINER);
		logger.fine("Test");

	}


}
