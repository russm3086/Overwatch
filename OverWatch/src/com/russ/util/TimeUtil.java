/**
 * 
 */
package com.russ.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rmartine
 *
 */
public class TimeUtil {

	private static Logger logger = Logger.getLogger(Class.class.getName());

	public static LocalDate subtractDate(LocalDate startDate, TemporalUnit unit, int numOfUnit) {

		LocalDate endDate = startDate.minus(numOfUnit, unit);

		return endDate;

	}

	public static LocalDate addDate(LocalDate startDate, TemporalUnit unit, int numOfUnit) {

		LocalDate endDate = startDate.plus(numOfUnit, unit);

		return endDate;

	}

	public static LocalDate daysInTheFuture(int days) {

		LocalDate today = LocalDate.now();

		LocalDate endDate = addDate(today, ChronoUnit.DAYS, days);

		return endDate;

	}

	public static LocalDate daysInPast(int days) {

		LocalDate today = LocalDate.now();

		LocalDate endDate = subtractDate(today, ChronoUnit.DAYS, days);

		return endDate;

	}

	public static LocalDate weeksInTheFuture(int weeks) {

		LocalDate today = LocalDate.now();

		LocalDate endDate = addDate(today, ChronoUnit.WEEKS, weeks);

		return endDate;

	}

	public static LocalDate weeksInPast(int weeks) {

		LocalDate today = LocalDate.now();

		LocalDate endDate = subtractDate(today, ChronoUnit.WEEKS, weeks);

		return endDate;

	}

	public static LocalDate monthsInTheFuture(int months) {

		LocalDate today = LocalDate.now();

		LocalDate endDate = addDate(today, ChronoUnit.MONTHS, months);

		return endDate;

	}

	public static LocalDate monthsInPast(int months) {

		LocalDate today = LocalDate.now();

		LocalDate endDate = subtractDate(today, ChronoUnit.MONTHS, months);

		return endDate;

	}

	public static LocalDate yearsInTheFuture(int years) {

		LocalDate today = LocalDate.now();

		LocalDate endDate = addDate(today, ChronoUnit.YEARS, years);

		return endDate;

	}

	public static LocalDate yearsInPast(int years) {

		LocalDate today = LocalDate.now();

		LocalDate endDate = subtractDate(today, ChronoUnit.YEARS, years);

		return endDate;

	}

	public static String dateFile(String file) {
		try {

			Pattern p = Pattern.compile("(.*)\'(.*)\'(.*)");
			Matcher m = p.matcher(file);

			if (m.find()) {
				String dateFormat = m.group(2);
				String suffix = m.group(3);

				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				String preFix = sdf.format(new Date());
				file = preFix + suffix;

			}

		} catch (Exception e) {

			logger.log(Level.INFO, "Unable to format file " + file, e);

		}

		return file;

	}

	public static LocalDateTime getLocalDateTime(String strDate) {
		LocalDateTime result;
		try {

			result = LocalDateTime.parse(strDate);

		} catch (Exception e) {

			try {

				result = EpochToLocalDateTime(strDate);

			} catch (NumberFormatException nfe) {

				result = StringToEpoch(strDate);

			}

		}

		return result;

	}

	public static LocalDateTime EpochToLocalDateTime(String epochTime) {

		Long longValue = Long.valueOf(epochTime);
		LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(longValue), ZoneId.systemDefault());
		return date;
	}

	public static LocalDateTime StringToEpoch(String strDate) {
		logger.finer("Entering");
		final List<String> dateFormats = Arrays.asList("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm", "yyyy-MM-dd",
				"H:mm:ss", "hh:mm:ss", "MM/dd/yyyy HH:mm:ss.SSS", "MM/dd/yyyy HH:mm:ss", "MM/dd/yyyy HH:mm",
				"M/dd/yyyy", "MM/dd/yyyy", "MMM dd yyyy", "dd-MM-yyyy", "dd/MM/yyyy");

		for (String format : dateFormats) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

			try {
				logger.finest("Format " + format);
				return LocalDateTime.parse(strDate, formatter);
			} catch (DateTimeParseException e) {
				logger.finest(strDate + " failed for format " + format);
			} finally {
				logger.finer("Exiting");
			}
		}
		throw new IllegalArgumentException("Invalid input for date. Given " + strDate);

	}

	public static LocalDate DateToLocalDate(Date date) {
		LocalDate current = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return current;
	}

	public static LocalDate DateToCalendar(Calendar cal) {
		LocalDate current = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return current;
	}

	public static Date localDateToDate(LocalDate localDate) {

		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return date;
	}

	public static Calendar localDateToCalendar(LocalDate localDate) {

		Date date = localDateToDate(localDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;

	}

	public static String EpochToString(Calendar cal) {

		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
		String formattedDate = df.format(cal.getTime());
		return formattedDate;
	}

	public static String formatDurationHHmmSSsss(long elapseTime, String timeUnit) {

		TimeUnit tu = TimeUnit.valueOf(timeUnit.toUpperCase());
		return formatDurationHHmmSSsss(elapseTime, tu);
	}

	public static String formatDurationHHmmSSsss(long elapseTime, TimeUnit tu) {
		LinkedHashMap<TimeUnit, Long> time = formatDuration(elapseTime, TimeUnit.MILLISECONDS);
		
		StringBuilder sb = new StringBuilder();
		for (Entry<TimeUnit, Long> entrySet : time.entrySet()) {

			String format;
			switch (entrySet.getKey()) {

			case DAYS:
				format = "Days: %02d ";
				break;

			case HOURS:
				format = "%02d:";
				break;

			case SECONDS:
				format = "%02d.";
				break;

			case MILLISECONDS:
				format = "%03d";
				break;

			default:
				format = "";
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

	public static ChronoUnit findChronoUnit(String timeUnits) {
		final ChronoUnit unit;

		switch (timeUnits.toLowerCase()) {

		case "sec":
		case "secs": {
			unit = ChronoUnit.SECONDS;
			break;
		}
		case "min":
		case "mins": {
			unit = ChronoUnit.MINUTES;
			break;
		}
		case "hour":
		case "hours": {
			unit = ChronoUnit.HOURS;
			break;
		}
		case "day":
		case "days": {
			unit = ChronoUnit.DAYS;
			break;
		}
		case "week":
		case "weeks": {
			unit = ChronoUnit.WEEKS;
			break;
		}
		case "month":
		case "months": {
			unit = ChronoUnit.MONTHS;
			break;
		}
		case "year":
		case "years": {
			unit = ChronoUnit.YEARS;
			break;
		}
		default: {
			unit = ChronoUnit.valueOf(timeUnits);
			if (unit == null) {
				throw new IllegalStateException("Incorrect format: " + timeUnits + " !!");
			}
		}

		}
		return unit;
	}
}
