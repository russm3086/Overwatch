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

	public Connect() {
	};

	/**
	 * @throws JSchException
	 * 
	 */
	public Session createSession(UserInfo userInfo) throws JSchException {
		JSch jsch = new JSch();
		logger.finer("Connecting to " + host);
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
