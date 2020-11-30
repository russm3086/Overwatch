/**
 * 
 */
package com.ansys.cluster.monitor.gui;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.data.factory.ClusterFactory;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * @author rmartine
 *
 */
public class ClusterDataCollector {
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	private SGE_MonitorProp mainProps = null;

	/**
	 * @param manager
	 */
	public ClusterDataCollector(SGE_MonitorProp mainProps) {
		this.mainProps = mainProps;
	}

	public Cluster retrieveClusterData() {
		Cluster cluster = null;
		try {

			String clusterName = mainProps.getClusterName(mainProps.getClusterIndex());

			logger.info("Calling ClusterFactory for cluster " + clusterName);
			Console.setStatusLabel("Retirieving data from cluster " + clusterName);

			String userName = System.getProperty("user.name");

			cluster = ClusterFactory.createCluster(mainProps, false, userName);

			Console.setStatusLabel("Retirieved data from cluster  " + clusterName);
			logger.info("New Cluster Object created " + clusterName);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Problems retrieving cluster data", e);

		}

		return cluster;
	}

}
