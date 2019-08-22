/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import javax.swing.JPanel;

import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;

/**
 * @author rmartine
 *
 */
public class DetailedInfoFactory {

	public final static String ClusterDetailedInfoPanel = "CLUSTER_DETAILED_INFO_PANEL";
	public final static String DetailedInfoPanel = "DETAILED_INFO_PANEL";

	/**
	 * 
	 */
	private DetailedInfoFactory() {
		// TODO Auto-generated constructor stub
	}

	public static JPanel createDetailedInfoPanel(ClusterNodeAbstract node) {

		DetailedInfoFactory factory = new DetailedInfoFactory();

		switch (node.getDetailedInfoPanel()) {

		case ClusterDetailedInfoPanel:
			return factory.createClusterDetailPanel(node);

		default:

			return factory.createDetailedPanel(node);
		}
	}

	public JPanel createClusterDetailPanel(ClusterNodeAbstract node) {

		DetailedInfoProp diProp = node.getDetailedInfoProp();
		ClusterDetailedInfoPanel detailInfoPanel = new ClusterDetailedInfoPanel(diProp);
		detailInfoPanel.createPanel();
		return detailInfoPanel;

	}

	public JPanel createDetailedPanel(ClusterNodeAbstract node) {

		DetailedInfoProp diProp = node.getDetailedInfoProp();
		DetailedInfoPanel detailInfo = new DetailedInfoPanel(diProp);
		detailInfo.createPanel();

		return detailInfo;
	}
}
