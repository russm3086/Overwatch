/**
 * 
 */
package com.russ.test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.russ.util.TimeUtil;

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

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		
		
		
		LocalDateTime time = TimeUtil.getLocalDateTime("1586536333731");
		
		ZonedDateTime zTime = TimeUtil.getZonedDateTime("1586536333731");

		System.out.println("localtime: " + time);
		
		
		System.out.println("Zonedtime: " + zTime);
		
		
		
		System.out.println("local time: " +zTime.withZoneSameInstant(ZoneId.of("Europe/Zurich")));
		System.out.println("local time: " +zTime.withZoneSameInstant(ZoneId.of("America/New_York")));
		
		




	}

}
