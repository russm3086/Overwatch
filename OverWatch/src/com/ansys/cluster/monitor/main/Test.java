/**
 * 
 */
package com.ansys.cluster.monitor.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import com.russ.util.TimeUtil;

/**
 * @author rmartine
 *
 */
public class Test {
	private static String sourceClass = Test.class.getName();
	private static Logger logger = Logger.getLogger(sourceClass);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		logger.setLevel(Level.FINEST);
		try {

			Logger rootLogger = Logger.getLogger("com.ansys");
			rootLogger.setLevel(Level.FINEST);
			rootLogger.setUseParentHandlers(false);

			Logger rootRussLogger = Logger.getLogger("com.russ");
			rootRussLogger.setLevel(Level.FINEST);
			rootRussLogger.setUseParentHandlers(false);


			System.out.println(TimeUtil.getLocalDateTime("05/26/2019"));
			
			//1554391994503
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
