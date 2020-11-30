/**
 * 
 */
package com.russ.util.settings;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ansys.cluster.monitor.main.Main;

/**
 * @author rmartine
 *
 */
public class LoggingUtil {
	private static String sourceClass = Main.class.getName();
	private static Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */
	private LoggingUtil() {
		// TODO Auto-generated constructor stub
	}

	public static void setLevel(Level targetLevel) {
		Logger root = Logger.getLogger("");
		root.setLevel(targetLevel);
		for (Handler handler : root.getHandlers()) {
			handler.setLevel(targetLevel);
		}
		logger.log(targetLevel, "level set: " + targetLevel.getName());
	}

	public static String getLogLevels() {
		String result = new String();
		try {
			Set<Level> levels = getAllLevels();
			result = levels.stream().map(String::valueOf).collect(Collectors.joining(", "));

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Set<Level> getAllLevels() throws IllegalAccessException {
		Class<Level> levelClass = Level.class;

		Set<Level> allLevels = new TreeSet<>(Comparator.comparingInt(Level::intValue));

		for (Field field : levelClass.getDeclaredFields()) {
			if (field.getType() == Level.class) {
				allLevels.add((Level) field.get(null));
			}
		}
		return allLevels;
	}

}
