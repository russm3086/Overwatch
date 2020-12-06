/**
 * 
 */
package com.russ.test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.russ.util.TimeUtil;
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

	}

	/**
	 * @param args
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IllegalAccessException, IOException {
		LoggingUtil.setLevel(Level.FINER);
		logger.fine("Test");


		String hardTime = TimeUtil.formatDuration(24, TimeUnit.HOURS, "dd 'Day(s)' HH'h' mm'm'");
		
		
		System.out.println(hardTime);

	}


}
