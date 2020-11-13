/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;

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
		
		//setLayout(new BorderLayout());
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createTitle(masterDiProp.getTitleMetric(), masterDiProp.getTitleValue());
	
		
		JPanel graphPanel = new JPanel();
		graphPanel.setBackground(Color.WHITE);
		graphPanel.setLayout(new GridLayout(1, 2, 10, 10));
		
		
		JPanel progressBar = createPanel(list.get(list.size()-1));
		progressBar.setPreferredSize(new Dimension(250, 250));
		
		graphPanel.add(progressBar);
		graphPanel.add(createPanel(list.get(list.size()-2)));

		graphPanel.setPreferredSize(new Dimension(700, 300));
		
		add(graphPanel);
		
		
		for (int i = 0; i < list.size()-2; i++) {

			DetailedInfoProp diProp = list.get(i);
			add(createPanel(diProp));
		}

		
		
	}
	
}
