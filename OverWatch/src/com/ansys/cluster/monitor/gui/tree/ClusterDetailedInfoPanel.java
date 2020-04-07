/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

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
		// setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		createTitle(masterDiProp.getTitleMetric(), masterDiProp.getTitleValue());

		JTabbedPane tabbedPane = new JTabbedPane();

		JPanel clusterGraphsPanel = new JPanel(new BorderLayout());
		tabbedPane.addTab("Cluster Graphs", clusterGraphsPanel);

		JPanel clusterTablesPanel = new JPanel(new BorderLayout());
		tabbedPane.addTab("Cluster Tables", clusterTablesPanel);

		JPanel clusterBubblePanel = new JPanel(new BorderLayout());
		tabbedPane.addTab("Job Bubble", clusterBubblePanel);

		JPanel graphPanel = new JPanel();
		JPanel tablePanel = new JPanel(new GridLayout(0, 1));
		JPanel bubblePanel = new JPanel(new BorderLayout());

		graphPanel.setBackground(Color.WHITE);
		graphPanel.setLayout(new GridLayout(2, 3, 10, 10));

		for (int i = 0; i < list.size(); i++) {

			DetailedInfoProp diProp = list.get(i);

			if (diProp.getDataType().equalsIgnoreCase(DetailedInfoProp.const_DataTypePieChart)
					|| diProp.getDataType().equalsIgnoreCase(DetailedInfoProp.const_DataTypeBarChart)) {
				graphPanel.add(createPanel(diProp));

				if (clusterGraphsPanel.getComponents().length == 0)
					clusterGraphsPanel.add(graphPanel);

			} else if (diProp.getDataType().equalsIgnoreCase(DetailedInfoProp.const_DataTypeBubbleChart)) {

				bubblePanel.add(createPanel(diProp));

				if (clusterBubblePanel.getComponents().length == 0)
					clusterBubblePanel.add(bubblePanel);

			} else {

				tablePanel.add(createPanel(diProp));

				if (clusterTablesPanel.getComponents().length == 0)
					clusterTablesPanel.add(tablePanel);
			}
		}

		// tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		add(tabbedPane, BorderLayout.CENTER);

	}

}
