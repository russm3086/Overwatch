/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data.factory;

import java.util.HashMap;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.net.Payload;
import com.ansys.cluster.monitor.net.SGE_ConnectConst;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * 
 * @author rmartine
 * @since
 */
public class HostFactory {
	private static String sourceClass = HostFactory.class.getName();
	private static Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */
	private HostFactory() {
		
	}

	public static HashMap<String, Host> createHostMap(Payload payload, SGE_MonitorProp mainProps) {
		logger.entering(sourceClass, "createHostMap");
		HashMap<String, Host> map = new HashMap<String, Host>();
		switch (payload.getPayloadType()) {
		case SGE_ConnectConst.xmlType:
			logger.finer("Creating XML Parser");
			XMLParser xmlParser = new XMLParser(payload, mainProps);
			map = xmlParser.createHostsMap();
			break;

		case SGE_ConnectConst.jsonType:
			logger.finer("Creating JSON Parser");
			JSONParser jsonParser = new JSONParser(payload, mainProps);
			map = jsonParser.createHostsMap();
			break;
		}
		logger.exiting(sourceClass, "createHostMap");

		return map;
	}

}
