/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.net;

/**
 * 
 * @author rmartine
 * @since
 */
public final class SGE_ConnectConst {

	/**
	 * 
	 */
	public SGE_ConnectConst() {
		// TODO Auto-generated constructor stub
	}

	public static final String queues = "http://ottsimportal2.ansys.com:5000/queues";
	public static final String hosts = "http://ottsimportal2.ansys.com:5000/hosts";
	public static final String jobs = "http://ottsimportal2.ansys.com:5000/jobs";
	public static final String detailedJobs ="http://ottsimportal2.ansys.com:5000/jobs/all";
	public static final String liveSuffix = "/live";
	public static final String xmlType = "XML";
	public static final String jsonType = "JSON";
	public static final String clusterType ="CLUSTER";
	public static final String unknownType = "UNKNOWN";
	
	
}
