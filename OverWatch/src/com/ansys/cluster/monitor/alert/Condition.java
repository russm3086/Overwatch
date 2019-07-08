/**
 * 
 */
package com.ansys.cluster.monitor.alert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rmartine
 *
 */
public enum Condition {
	EQUAL, LESS_THAN, GREATER_THAN, LESS_THAN_EQUAL, GREATER_THAN_EQUAL, NOT_EQUAL;

	public static Condition parse(String condition) {

		switch (condition.toLowerCase()) {

		case "equal":
			return EQUAL;

		case "less_than":
			return LESS_THAN;

		case "greater_than":
			return GREATER_THAN;

		case "less_than_equal":
			return LESS_THAN_EQUAL;

		case "greater_than_equal":
			return GREATER_THAN_EQUAL;

		case "not_equal":
			return NOT_EQUAL;
		}
		return null;

	}

	
	public static boolean evalute(String value, Pattern pattern) {
		
		Matcher matcher = pattern.matcher(value); 
		return matcher.matches();
	}
	
	public static boolean evaluate(String first, String second, Condition condition) {
		
		return evaluate(Long.parseLong(first), Long.parseLong(second), condition);
	}
	
	public static boolean evaluate(long first, long second, Condition condition) {

		switch (condition) {

		case EQUAL:
			return (first == second);

		case LESS_THAN:
			return (first < second);

		case GREATER_THAN:
			return (first > second);

		case LESS_THAN_EQUAL:
			return (first <= second);

		case GREATER_THAN_EQUAL:
			return (first >= second);
			
		case NOT_EQUAL:
			return (first == second);

		default:
			return false;

		}

	}

}
