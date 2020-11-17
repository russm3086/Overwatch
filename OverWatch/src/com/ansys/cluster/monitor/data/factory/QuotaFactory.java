/**
 * 
 */
package com.ansys.cluster.monitor.data.factory;

import java.util.LinkedList;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.Quota;
import com.ansys.cluster.monitor.net.Payload;
import com.ansys.cluster.monitor.net.SGE_ConnectConst;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
 * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
*/

/**
 * 
 * @author rmartine
 * @since
 */
public class QuotaFactory {
	private static String sourceClass = QuotaFactory.class.getName();
	private static Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */

	private QuotaFactory() {
	}

	public static LinkedList<Quota> createQuotaList(Payload payload, SGE_MonitorProp mainProps, String userName) {
		logger.entering(sourceClass, "createQuotaMap");

		
		LinkedList<Quota> list = new LinkedList<Quota>();
		switch (payload.getPayloadType()) {
		case SGE_ConnectConst.xmlType:
			XMLParser xmlParser = new XMLParser(payload, mainProps);
			list = xmlParser.createQuotaMap(userName);
			break;

		// case SGE_ConnectConst.jsonType:
		// JSONParser jsonParser = new JSONParser(payloadJobs, payloadDetailedJobs,
		// mainProps);
		// map = jsonParser.createQuotaMap();
		// break;
		}
		logger.exiting(sourceClass, "createHostMap");

		return list;
	}
}
