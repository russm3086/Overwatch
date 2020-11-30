/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.net;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import org.jdom2.JDOMException;
import org.json.JSONException;

import com.ansys.cluster.monitor.main.SGE_DataConst;
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

	private Payload connectors(String strUrl) throws JSONException, IOException, URISyntaxException, JDOMException,
			InterruptedException, TransformerException, ClassNotFoundException {
		logger.finer("Connecting to " + strUrl);
		
		return connector.getPayload(strUrl);
	}

	public Payload getHostsData(int item) throws JSONException, IOException, URISyntaxException, JDOMException,
			InterruptedException, TransformerException, ClassNotFoundException {

		if (mainProps.getClusterConnectionRequestMethod().equalsIgnoreCase(SGE_DataConst.connTypeCMD)) {

			return connectors(mainProps.getClusterConnectionHostCmd(item));
		}
		return connectors(mainProps.getClusterConnectionHostUrl(item));
	}

	public Payload getQuotaData(int item) throws JSONException, IOException, URISyntaxException, JDOMException,
			InterruptedException, TransformerException, ClassNotFoundException {

		if (mainProps.getClusterConnectionRequestMethod().equalsIgnoreCase(SGE_DataConst.connTypeCMD)) {

			return connectors(mainProps.getClusterConnectionQuotaCmd(item));
		}
		return connectors(mainProps.getClusterConnectionQuotaUrl(item));
	}

	public Payload getJobsData(int item) throws JSONException, IOException, URISyntaxException, JDOMException,
			InterruptedException, TransformerException, ClassNotFoundException {
		if (mainProps.getClusterConnectionRequestMethod().equalsIgnoreCase(SGE_DataConst.connTypeCMD)) {

			return connectors(mainProps.getClusterConnectionSummaryJobsCmd(item));
		}
		return connectors(mainProps.getClusterConnectionSummaryJobsUrl(item));
	}

	public Payload getDetailedJobsData(int item) throws JSONException, IOException, URISyntaxException, JDOMException,
			InterruptedException, TransformerException, ClassNotFoundException {
		if (mainProps.getClusterConnectionRequestMethod().equalsIgnoreCase(SGE_DataConst.connTypeCMD)) {

			return connectors(mainProps.getClusterConnectionDetailedJobsCmd(item));
		}
		return connectors(mainProps.getClusterConnectionDetailedJobsUrl(item));
	}

	public Payload getCluster(int item) throws JSONException, ClassNotFoundException, IOException, URISyntaxException,
			JDOMException, InterruptedException, TransformerException {
		return connectors(mainProps.getClusterConnectionClusterUrl(item));
	}

	public SGE_MonitorProp getMainProps() {
		return mainProps;
	}

	public void setMainProps(SGE_MonitorProp mainProps) {
		this.mainProps = mainProps;
	}

}
