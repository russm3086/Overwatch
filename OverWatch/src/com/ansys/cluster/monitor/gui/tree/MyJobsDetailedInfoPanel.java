/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTree;

/**
 * @author rmartine
 *
 */
public class MyJobsDetailedInfoPanel extends DetailedInfoPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -752799894349436331L;

	/**
	 * @param masterDiProp
	 * @param tree
	 */
	public MyJobsDetailedInfoPanel(DetailedInfoProp masterDiProp, JTree tree) {
		super(masterDiProp, tree);
		// TODO Auto-generated constructor stub
	}

	protected void createDetailInfoPage(DetailedInfoProp masterDiProp) {

		ArrayList<DetailedInfoProp> list = masterDiProp.getDetailedInfoPropList();

		// setLayout(new BorderLayout());
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createTitle(masterDiProp.getTitleMetric(), masterDiProp.getTitleValue());

		JPanel graphPanel = new JPanel();
		graphPanel.setBackground(Color.WHITE);
		graphPanel.setLayout(new GridLayout(1, 2, 10, 10));

		DetailedInfoProp diProp = list.get(list.size() - 1);

		JPanel progressBar = createPanel(diProp);
		progressBar.setPreferredSize(new Dimension(250, 250));

		graphPanel.add(progressBar);

		JPanel quotaPanel = createPanel(list.get(list.size() - 2));
		quotaPanel.setPreferredSize(new Dimension(400, 250));
		graphPanel.add(quotaPanel);

		graphPanel.setPreferredSize(new Dimension(700, 300));

		add(graphPanel);

		for (int i = 0; i < list.size() - 2; i++) {

			diProp = list.get(i);
			add(createPanel(diProp));
		}

	}

}
