/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.russ.ssh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * 
 * @author rmartine
 * @since
 */
public class Execute {
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */

	public Execute() {

	}
	
	public String execute(String command) throws IOException, InterruptedException {
		ArrayList<String> list = new ArrayList<String>();
		
		list.add("/bin/sh");
		list.add("-c");
		list.add(command);
		
		return execute(list.toArray(new String[0]));
		
	}

	public String execute(String[] command) throws IOException, InterruptedException {

		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectErrorStream(true);
		
		builder.command(command);
		logger.fine("Setting execute directory " + System.getProperty("user.home"));
		builder.directory(new File(System.getProperty("user.home")));

		Process process = builder.start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		StringBuilder sb = new StringBuilder();

		Iterator<String> it = reader.lines().iterator();

		while (it.hasNext()) {
			
			sb.append(it.next());
		}
		
		process.waitFor();
		
		if(process.exitValue()!=0) {
			
			StringBuilder sbCmd = new StringBuilder();
			
			for (String cmd : command) {
				
				sbCmd.append(cmd + " ");
				
			}
			
			throw new IOException("Error executing " + sbCmd + " details: "+ sb);
		}
		
		return sb.toString();
	}

}
