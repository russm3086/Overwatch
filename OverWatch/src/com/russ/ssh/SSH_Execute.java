/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.russ.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * 
 * @author rmartine
 * @since
 */
public class SSH_Execute {
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	private Session session;

	/**
	 * 
	 */
	public SSH_Execute(Session session) {
		// TODO Auto-generated constructor stub

		this.session = session;

	}
	
	public SSH_Execute() {
		
	}
	
	public void closeSession() {
		
		session.disconnect();
		
	}

	public String executeOnce(String command) throws JSchException, IOException {
		
		String result = execute(command);
		closeSession();
		return result;

	}
	
	public String execute(String command) throws JSchException, IOException {
		//String result;
		//StringBuilder strBuild = new StringBuilder();

		logger.fine("Executing: " + command);

		ChannelExec channel = (ChannelExec)session.openChannel("exec"); 
		channel.setCommand(command);

		StringBuilder outputBuffer = new StringBuilder();
		StringBuilder errorBuffer = new StringBuilder();

		InputStream in = channel.getInputStream();
		InputStream err = channel.getExtInputStream();

		channel.connect();

		byte[] tmp = new byte[1024];
		while (true) {
		    while (in.available() > 0) {
		        int i = in.read(tmp, 0, 1024);
		        if (i < 0) break;
		        outputBuffer.append(new String(tmp, 0, i));
		        logger.finest("SSH Stream output: " + outputBuffer);
		    }
		    while (err.available() > 0) {
		        int i = err.read(tmp, 0, 1024);
		        if (i < 0) break;
		        errorBuffer.append(new String(tmp, 0, i));
		        logger.finest("SSH error stream output: " + errorBuffer);
		    }
		    if (channel.isClosed()) {
		        if ((in.available() > 0) || (err.available() > 0)) continue; 
		       logger.finer("exit-status: " + channel.getExitStatus()); 
		        break;
		    }
		    try { 
		      Thread.sleep(1000);
		    } catch (Exception ee) {
		    	
		    	logger.log(Level.FINE, "Error Exec SSH", ee);
		    }
		}

		logger.finer("Dicsonnecting channel");
		channel.disconnect();
		in.close();
		err.close();

		logger.finest("Output: " + outputBuffer);
		logger.finest("Error: " + errorBuffer );

		
		return outputBuffer.toString();
	}

}
