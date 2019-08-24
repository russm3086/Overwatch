/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;

/**
 * @author rmartine
 *
 */
public class ClusterDetailedInfoPanel extends DetailedInfoPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 257151745964483501L;

	/**
	 * @param masterDiProp
	 */
	public ClusterDetailedInfoPanel(DetailedInfoProp masterDiProp, JTree tree) {
		super(masterDiProp, tree);
		// TODO Auto-generated constructor stub

	}

	protected void createDetailInfoPage(DetailedInfoProp masterDiProp) {

		ArrayList<DetailedInfoProp> list = masterDiProp.getDetailedInfoPropList();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createTitle(masterDiProp.getTitleMetric(), masterDiProp.getTitleValue());

		JTabbedPane tabbedPane = new JTabbedPane();
		
		JPanel clusterGraphs = new JPanel(new BorderLayout());
		tabbedPane.addTab("Cluster Graphs", clusterGraphs);
		
		JPanel clusterTables = new JPanel(new BorderLayout());
		tabbedPane.addTab("Cluster Tables", clusterTables);

		JPanel graphPanel = new JPanel();
		graphPanel.setBackground(Color.WHITE);
		graphPanel.setLayout(new GridLayout(2, 3, 10, 10));

		for (int i = 0; i < list.size(); i++) {

			DetailedInfoProp diProp = list.get(i);

			if (diProp.getDataType().equalsIgnoreCase(DetailedInfoProp.const_DataTypePieChart)) {
				graphPanel.add(createPanel(diProp));

				if (this.getComponents().length >= 1)
					clusterGraphs.add(graphPanel);

			} else if (diProp.getDataType().equalsIgnoreCase(DetailedInfoProp.const_DataTypeBubbleChart)) {

				tabbedPane.addTab("Job Bubble", createPanel(diProp));

			} else {

				clusterTables.add(createPanel(diProp));
			}
		}

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		add(tabbedPane);

	}

}
