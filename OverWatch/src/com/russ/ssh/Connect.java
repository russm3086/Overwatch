/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.russ.ssh;

import java.util.logging.Logger;


import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.russ.sge.gui.MyUserInfo;



/**
 * 
 * @author rmartine
 * @since
 */
public class Connect {
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	private UserInfo ui;
	private String host;
	private String user;
	
	
	public Connect(){};
	
	/**
	 * @throws JSchException 
	 * 
	 */
	public  Session createSession() throws JSchException {
		// TODO Auto-generated constructor stub
		logger.entering(sourceClass, "createSession");
		JSch jsch = new JSch();
/**		String host = JOptionPane.showInputDialog("Enter username@hostname",
				System.getProperty("user.name") + "@localhost");

		user = host.substring(0, host.indexOf('@'));
		this.host = host.substring(host.indexOf('@') + 1);
		*/
		user = "rmartine";
		this.host ="ottsimportal2.ansys.com";

		Session session = jsch.getSession(user, this.host, 22);
		
		ui = new MyUserInfo();
		session.setUserInfo(ui);
		
		logger.finer("Creating initial connection to " + this.host);
		session.connect();		
		
		logger.exiting(sourceClass, "createSession", session);
		return session;

	}

	public Session createSession(UserInfo userInfo) throws JSchException {
		JSch jsch = new JSch();
		Session session = jsch.getSession(user, host, 22);
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(ui.getPassword());
		session.setUserInfo(ui);
		session.connect();		
		
		return session;		
	}
	
	
	/**
	 * @return the ui
	 */
	public UserInfo getUi() {
		return ui;
	}

	/**
	 * @param ui the ui to set
	 */
	public void setUi(UserInfo ui) {
		this.ui = ui;
	}

}
