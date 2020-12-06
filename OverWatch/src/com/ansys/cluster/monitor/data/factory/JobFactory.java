package com.ansys.cluster.monitor.data.factory;

import java.util.HashMap;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.Job;
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
public class JobFactory {
	private static String sourceClass = JobFactory.class.getName();
	private static Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */
	private JobFactory() {

	}

	public static HashMap<Integer, Job> createJobsMap(Payload payloadJobs, Payload payloadDetailedJobs,
			Payload payloadFullDetailedJobs, SGE_MonitorProp mainProps) {

		if (payloadDetailedJobs == null) {

			payloadDetailedJobs = payloadFullDetailedJobs;
		}

		return createJobsMap(payloadJobs, payloadDetailedJobs, mainProps);
	}

	public static HashMap<Integer, Job> createJobsMap(Payload payloadJobs, Payload payloadDetailedJobs,
			SGE_MonitorProp mainProps) {
		logger.entering(sourceClass, "createDetailedJobMap");

		HashMap<Integer, Job> map = null;
		switch (payloadJobs.getPayloadType()) {
		case SGE_ConnectConst.xmlType:
			XMLParser xmlParser = new XMLParser(payloadJobs, payloadDetailedJobs, mainProps);
			map = xmlParser.createJobsMap();
			break;

		case SGE_ConnectConst.jsonType:
			JSONParser jsonParser = new JSONParser(payloadJobs, payloadDetailedJobs, mainProps);
			map = jsonParser.createJobsMap();
			break;
		}
		logger.exiting(sourceClass, "createHostMap");

		return map;
	}

}
