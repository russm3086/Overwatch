/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.net;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.jdom2.JDOMException;
import org.json.JSONException;

import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * 
 * @author rmartine
 * @since
 */
public class DataCollector {
	private String sourceClass = this.getClass().getName();
	private Logger logger = Logger.getLogger(sourceClass);
	private SGE_MonitorProp mainProps;
	private Connector connector;

	/**
	 * 
	 */
	public DataCollector(SGE_MonitorProp mainProps, Connector connector) {
		// TODO Auto-generated constructor stub
		logger.entering(sourceClass, "Constructor", connector);
		setConnector(connector);
		setMainProps(mainProps);
	}

	/**
	 * @return the connector
	 */
	public Connector getConnector() {
		return connector;
	}

	/**
	 * @param connector the connector to set
	 */
	public void setConnector(Connector connector) {
		this.connector = connector;
	}

	private Payload connectors(String strUrl) throws JSONException, IOException, URISyntaxException, JDOMException {
		logger.finer("Connecting to " + strUrl);
		if ((strUrl.startsWith("http://")) || (strUrl.startsWith("https://"))) {
			return connector.connect(strUrl, mainProps);

		} else {

			return connector.getFile(strUrl);
		}
	}

	public Payload getHostsData(int item) throws JSONException, IOException, URISyntaxException, JDOMException {
		return connectors(mainProps.getClusterConnectionHostUrl(item));
	}

	public Payload getJobsData(int item) throws JSONException, IOException, URISyntaxException, JDOMException {

		return connectors(mainProps.getClusterConnectionSummaryJobsUrl(item));
	}

	public Payload getDetailedJobsData(int item) throws JSONException, IOException, URISyntaxException, JDOMException {

		return connectors(mainProps.getClusterConnectionDetailedJobsUrl(item));
	}

	public Payload getJobsData(String jobNumber) throws JSONException, IOException, URISyntaxException, JDOMException {

		return connectors(SGE_ConnectConst.jobs + "/" + jobNumber);
	}

	public Payload getQueuesData(String queueName)
			throws JSONException, IOException, URISyntaxException, JDOMException {

		return connectors(SGE_ConnectConst.queues + "/" + queueName);
	}

	public SGE_MonitorProp getMainProps() {
		return mainProps;
	}

	public void setMainProps(SGE_MonitorProp mainProps) {
		this.mainProps = mainProps;
	}

}
