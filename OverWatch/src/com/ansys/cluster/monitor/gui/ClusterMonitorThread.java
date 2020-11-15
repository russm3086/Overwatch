/**
 * 
 */
package com.ansys.cluster.monitor.gui;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.data.MyJobs;
import com.ansys.cluster.monitor.data.factory.ClusterFactory;
import com.ansys.cluster.monitor.net.Connector;
import com.ansys.cluster.monitor.net.DataCollector;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * @author rmartine
 *
 */
public class ClusterMonitorThread {
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	private SGE_MonitorProp mainProps = null;
	private ConcurrentLinkedQueue<Cluster> linkedQueue = new ConcurrentLinkedQueue<>();

	/**
	 * 
	 */

	/**
	 * @param nodeProp
	 */
	public ClusterMonitorThread(SGE_MonitorProp mainProps, ConcurrentLinkedQueue<Cluster> linkedQueue) {
		this.mainProps = mainProps;
		this.linkedQueue = linkedQueue;
		// TODO Auto-generated constructor stub

	}

	public void retrieveData() {

		// int clusterCount = mainProps.getClusterCount();
		int clusterCount = 1;
		ExecutorService executor = Executors.newFixedThreadPool(1);
		int index = mainProps.getClusterIndex();

		for (int i = 0; i < clusterCount; i++) {
			executor.execute(new ClusterMonitorWorker(mainProps, index));
		}

		logger.fine("Setting ExecutorService to shutdown");
		executor.shutdown();

	}

	class ClusterMonitorWorker implements Runnable {
		protected SGE_MonitorProp mainProps;
		protected int index;

		protected ClusterMonitorWorker(SGE_MonitorProp mainProps, int index) {
			this.mainProps = mainProps;
			this.index = index;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// TODO Display to GUI
			try {

				Connector conn = new Connector(mainProps);
				DataCollector dc = new DataCollector(mainProps, conn);
				logger.info("Connecting to cluster " + mainProps.getClusterName(index));
				Console.setStatusLabel("Connecting to cluster " + mainProps.getClusterName(index));

				Cluster cluster = ClusterFactory.createCluster(dc, mainProps.getClusterName(index), index, mainProps,
						false);

				String userName = System.getProperty("user.name");
				MyJobs myJobs = new MyJobs(cluster, userName);				
				cluster.setMyJobMasterQueue(myJobs);

				Console.setStatusLabel("Closed connection to cluster " + mainProps.getClusterName(index));
				logger.info("Closed connection to cluster " + mainProps.getClusterName(index));
				logger.finer("Added data to queue. Cluster: " + mainProps.getClusterName(index));
				linkedQueue.offer(cluster);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.log(Level.SEVERE, "Problems retrieving cluster data", e);
				Console.setAlertStatusLabel("Problems retrieving cluster data");

			}
		}
	}
}
