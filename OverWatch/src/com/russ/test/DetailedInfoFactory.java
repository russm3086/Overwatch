/**
 * 
 */
package com.russ.test;

import javax.swing.JPanel;

import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;

/**
 * @author rmartine
 *
 */
public class DetailedInfoFactory {

	/**
	 * 
	 */
	private DetailedInfoFactory() {
		// TODO Auto-generated constructor stub
	}

	public static JPanel createDetailedInfoPanel(ClusterNodeAbstract node) {

		DetailedInfoFactory factory = new DetailedInfoFactory();
		return factory.createDetailedPanel(node);

	}

	public JPanel createDetailedPanel(ClusterNodeAbstract node) {

		DetailedInfoProp diProp = node.getDetailedInfoProp();
		DetailedInfo detailInfo = new DetailedInfo(diProp);
		
		return detailInfo;
	}
}
