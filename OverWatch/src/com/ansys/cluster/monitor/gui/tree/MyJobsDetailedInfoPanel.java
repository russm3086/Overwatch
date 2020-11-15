/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

		DetailedInfoProp diProp = list.get(list.size() - 1);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createTitle(masterDiProp.getTitleMetric(), masterDiProp.getTitleValue());


		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 2;
		//c.gridwidth = 2;
		//c.gridheight = 5;
		
		
		JPanel summaryPanel = new JPanel();
		summaryPanel.setBackground(this.getBackground());
		summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
		
		
		
		JPanel progressBarPanel = createPanel(list.get(list.size() - 1));
		//progressBarPanel.setMaximumSize(new Dimension(400,225));
		
		summaryPanel.add(progressBarPanel);
		summaryPanel.add(createPanel(list.get(0)));

		mainPanel.add(summaryPanel, c);

		c.gridx = 2;
		c.weightx = 4;
		//c.gridwidth = 3;
		
		mainPanel.add(createPanel(list.get(list.size() - 2)), c);

		JPanel tablePanel = new JPanel();

		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));

		for (int i = 1; i < list.size() - 2; i++) {

			diProp = list.get(i);
			tablePanel.add(createPanel(diProp));
		}

		c.gridy = 5;
		c.gridx = 0;
		c.gridwidth = 5;
		c.weightx = 5;
		c.weighty = 5;

		
		//c.gridwidth = 5;
		//c.gridheight = GridBagConstraints.REMAINDER;


		mainPanel.add(tablePanel, c);

		add(mainPanel);

	}

	protected void createDetailInfoPage1(DetailedInfoProp masterDiProp) {

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
