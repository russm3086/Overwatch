/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import javax.swing.JPanel;
import javax.swing.JTree;

import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;

/**
 * @author rmartine
 *
 */
public class DetailedInfoFactory {

	public final static String ClusterDetailedInfoPanel = "CLUSTER_DETAILED_INFO_PANEL";
	public final static String DetailedInfoPanel = "DETAILED_INFO_PANEL";
	public final static String MyJobsDetailedInfoPanel = "MY_JOBS_DETAILED_INFO_PANEL";

	/**
	 * 
	 */
	private DetailedInfoFactory() {
		// TODO Auto-generated constructor stub
	}

	public static JPanel createDetailedInfoPanel(ClusterNodeAbstract node, JTree tree) {

		DetailedInfoFactory factory = new DetailedInfoFactory();

		switch (node.getDetailedInfoPanel()) {

		case ClusterDetailedInfoPanel:
			return factory.createClusterDetailPanel(node, tree);

		case MyJobsDetailedInfoPanel:
			return factory.createMyJobsDetailedInfoPanel(node, tree);

		default:

			return factory.createDetailedPanel(node, tree);
		}
	}

	public JPanel createMyJobsDetailedInfoPanel(ClusterNodeAbstract node, JTree tree) {

		DetailedInfoProp diProp = node.getDetailedInfoProp();
		MyJobsDetailedInfoPanel detailInfoPanel = new MyJobsDetailedInfoPanel(diProp, tree);
		detailInfoPanel.createPanel();
		return detailInfoPanel;

	}

	public JPanel createClusterDetailPanel(ClusterNodeAbstract node, JTree tree) {

		DetailedInfoProp diProp = node.getDetailedInfoProp();
		ClusterDetailedInfoPanel detailInfoPanel = new ClusterDetailedInfoPanel(diProp, tree);
		detailInfoPanel.createPanel();
		return detailInfoPanel;

	}

	public JPanel createDetailedPanel(ClusterNodeAbstract node, JTree tree) {

		DetailedInfoProp diProp = node.getDetailedInfoProp();
		DetailedInfoPanel detailInfo = new DetailedInfoPanel(diProp, tree);
		detailInfo.createPanel();

		return detailInfo;
	}
}
