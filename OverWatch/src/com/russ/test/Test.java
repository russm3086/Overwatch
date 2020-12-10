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

		String app_version = "1.5.2.10";

		String[] versionArray = app_version.split("\\.");

		int len = versionArray.length;

		int version = 0;
		int arrayPos = 0;

		for (int i = len; i > 0; i--) {

			double mutltiple = Math.pow(10, i);
			double dblVersion = Double.parseDouble(versionArray[arrayPos]);
			arrayPos += 1;

			version += (dblVersion * mutltiple);

		}

		System.out.println(version);

	}

}
