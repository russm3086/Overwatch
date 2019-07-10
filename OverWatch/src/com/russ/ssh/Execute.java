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
		
		String[] arrCommand = command.split(" ");
		return execute(arrCommand);
		
	}

	public String execute(String... command) throws IOException, InterruptedException {
		logger.fine("Executing: " + command);

		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectErrorStream(true);
		builder.command(command);
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
			
			throw new IOException("Error executing " + sbCmd +  sb);
		}
		
		return sb.toString();
	}

}
