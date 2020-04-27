/**
 * 
 */
package com.russ.test;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * @author rmartine
 *
 */
public class Test {

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		long elapseTime = (1000 * 60 * 60 * 9);
		
		
		System.out.println(formatDurationHHmmss( elapseTime, "milliseconds"));

	}

	public static String formatDurationHHmmss(long elapseTime, String timeUnit) {

		TimeUnit tu = TimeUnit.valueOf(timeUnit.toUpperCase());
		return fromatDurationHHmmss(elapseTime, tu);

	}

	public static String fromatDurationHHmmss(long elapseTime, TimeUnit tu) {
		LinkedHashMap<TimeUnit, Long> time = formatDuration(elapseTime, TimeUnit.MILLISECONDS);

		StringBuilder sb = new StringBuilder();
		for (Entry<TimeUnit, Long> entrySet : time.entrySet()) {

			String format;
			switch (entrySet.getKey()) {

			case DAYS:
				format = "Days: %02d ";
				break;

			case SECONDS:
				format = "%02d.";
				break;

			case MILLISECONDS:
				format = "%02d";
				break;

			default:

				format = "%02d:";
				break;
			}

			sb.append(String.format(format, entrySet.getValue()));
		}

		return sb.toString();
	}

	public static LinkedHashMap<TimeUnit, Long> formatDuration(long elapseTime, TimeUnit tu) {

		LinkedHashMap<TimeUnit, Long> result = new LinkedHashMap<TimeUnit, Long>();

		long duration = TimeUnit.MILLISECONDS.convert(elapseTime, tu);

		long days = TimeUnit.MILLISECONDS.toDays(duration);
		duration -= TimeUnit.DAYS.toMillis(days);
		result.put(TimeUnit.DAYS, days);

		long hours = TimeUnit.MILLISECONDS.toHours(duration);
		duration -= TimeUnit.HOURS.toMillis(hours);
		result.put(TimeUnit.HOURS, hours);

		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		duration -= TimeUnit.MINUTES.toMillis(minutes);
		result.put(TimeUnit.MINUTES, minutes);

		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		duration -= TimeUnit.SECONDS.toMillis(seconds);
		result.put(TimeUnit.SECONDS, seconds);

		long milliseconds = TimeUnit.MILLISECONDS.toMillis(duration);
		result.put(TimeUnit.MILLISECONDS, milliseconds);

		return result;
	}

}
