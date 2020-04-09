package com.russ.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UnitConversion {

	public static String decimalUnits = "kMGTP";
	public static String binaryUnits = "KMGTP";

	public static String decimalBigUnits = decimalUnits + "EZY";
	public static String binaryBigUnits = binaryUnits + "EZY";

	
	public static double convertUnit(double value, String oldUnit, String newUnit, boolean si) {
		
		
		return 0L;
	}
	
	public static String humanReadableByteCount(long bytes, boolean decimal) {
		int unit = decimal ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (decimal ? decimalUnits : binaryUnits).charAt(exp - 1) + (decimal ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public static long byteCountHumanReadable(String bytes) throws NumberFormatException {
		return byteCountHumanReadable(bytes, false);
	}

	public static long byteCountHumanReadable(String bytes, boolean decimal) throws NumberFormatException {
		char label = 0;
		String units = findUnits(bytes);
		char[] charUnit = units.toCharArray();
		String result = removeNonDigits(bytes);

		if (charUnit.length >= 1) {
			label = charUnit[0];
		}
		return byteCountHumanReadable(result, decimal, label);
	}

	public static long byteCountHumanReadable(String bytes, boolean decimal, char label) throws NumberFormatException {
		
		if(bytes == null)
			return 0;

		double doubleValue = Double.valueOf(bytes).doubleValue();
		String pre = (decimal ? decimalUnits : binaryUnits);
		int exp = pre.toUpperCase().indexOf(Character.toUpperCase(label)) + 1;
		int unit = decimal ? 1000 : 1024;
		long value = (long) (doubleValue * Math.pow(unit, exp));

		return value;
	}

	public static String humanReadableByteCount(BigDecimal bytes, boolean decimal) {
		int unit = decimal ? 1000 : 1024;
		if ((BigDecimal.valueOf(unit).compareTo(bytes) > 0))
			return bytes + " B";

		int exp = (int) (BigMath.logBigDecimal(bytes) / Math.log(unit));
		String pre = (decimal ? decimalBigUnits : binaryBigUnits).charAt(exp - 1) + (decimal ? "" : "i");
		BigDecimal value = bytes.divide(BigDecimal.valueOf((Math.pow(unit, exp))));

		return String.format("%.1f %sB", value, pre);
	}

	public static BigInteger byteCountHumanReadableBI(String bytes, boolean decimal, char label)
			throws NumberFormatException {

		if(bytes == null)
			return BigInteger.ZERO;

		BigDecimal bgValue = new BigDecimal(bytes);
		String pre = (decimal ? decimalBigUnits : binaryBigUnits);
		int exp = pre.toUpperCase().indexOf(Character.toUpperCase(label)) + 1;
		int unit = decimal ? 1000 : 1024;

		BigInteger value = bgValue.multiply(BigDecimal.valueOf(Math.pow(unit, exp))).toBigInteger();

		return value;
	}

	public static double round(double value, int round) {
		double multiple = Math.pow(10, round);
		value = (double) Math.round(value * multiple) / multiple;
		return value;
	}

	
	private static String removeNonDigits(String str) {
		String result = str.replaceAll("[^\\d.]", "");
		return result;
	}

	private static String findUnits(String memory) {
		String memoryUnit = "";

		Pattern pattern = Pattern.compile("([^\\d.])");
		Matcher matcher = pattern.matcher(memory);

		if (matcher.find()) {
			memoryUnit = matcher.group(0);
		}
		return memoryUnit;
	}

}
