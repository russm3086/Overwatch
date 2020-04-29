/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.json.JSONException;

import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.gui.Console;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.gui.tree.TreeStateProps;
import com.russ.util.gui.tree.TreeUtil;

/**
 * @author rmartine
 *
 */
public class TreeBuilderMonitorThread {
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	private SGE_MonitorProp mainProps = null;
	private ConcurrentLinkedQueue<Cluster> linkedQueue = new ConcurrentLinkedQueue<>();
	private JTree tree;
	private TreeStateProps tsProps;

	/**
	 * 
	 */
	public TreeBuilderMonitorThread(SGE_MonitorProp mainProps, ConcurrentLinkedQueue<Cluster> linkedQueue, JTree tree) {
		// TODO Auto-generated constructor stub
		this.mainProps = mainProps;
		this.linkedQueue = linkedQueue;
		this.tree = tree;
	}

	public boolean isNewCluster(Cluster newCluster, JTree current) {

		boolean result = false;
		DefaultTreeModel model = (DefaultTreeModel) current.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		TreeNode firstChild = null;

		if (root.getChildCount() > 0) {

			firstChild = root.getFirstChild();
			String name = firstChild.toString();
			result = !newCluster.getName().equalsIgnoreCase(name);
		}

		return result;
	}

	public void buildTree() {

		int numFoundCluster = 0;
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.execute(new TreeBuilderWorker(mainProps, numFoundCluster));

		logger.fine("Setting ExecutorService to shutdown");
		executor.shutdown();

		logger.fine("Thread pool terminated: " + executor.isTerminated());
	}

	class TreeBuilderWorker implements Runnable {
		protected SGE_MonitorProp mainProps;
		protected int index;
		protected int numFoundCluster;

		protected TreeBuilderWorker(SGE_MonitorProp mainProps, int numFoundCluster) {
			this.mainProps = mainProps;
			this.numFoundCluster = numFoundCluster;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			TreeUtil tu = new TreeUtil();
			boolean newCluster = false;

			try {

				while (numFoundCluster < 1) {

					if (!linkedQueue.isEmpty()) {
						Cluster cluster = linkedQueue.poll();
						Console.setStatusLabel("Creating tree for cluster " + cluster.getName());

						newCluster = isNewCluster(cluster, tree);
						tsProps = tu.saveTreeState(tree);
						TreeBuilder treeBuilder = new TreeBuilder(tree);
						treeBuilder.buildTree(cluster);
						Console.setStatusLabel("Created tree for cluster " + cluster.getName());

						LocalDateTime currentDateTime = LocalDateTime.now();
						DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
						String formattedDateTime = currentDateTime.format(formatter);
						Console.setStatusLabel("Updated " + formattedDateTime);
						numFoundCluster += 1;

					} else {

						logger.finer("Cluster Queue is empty");
					}

					logger.finer("Pausing for 30 sec");
					Thread.sleep(1000);

				}

				if (newCluster != true && tsProps.size() > 0) {

					tu.applyTreeState(tree, tsProps);
				} else {

					tu.expandTreeToLevel(tree, mainProps.getGuiTreeExpansionLevel());
					tree.setSelectionRow(1);
				}

				logger.fine("All clusters pulled, terminating polling");

			} catch (JSONException | IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				logger.log(Level.SEVERE, "Problems retrieving cluster data", e);
				Console.setAlertStatusLabel("Problems retrieving cluster data");

			}

		}

	}

}
