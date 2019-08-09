package com.ansys.cluster.monitor.gui;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class ClusterDetails extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5884964694695122587L;

	/**
	 * Create the panel.
	 */
	public ClusterDetails() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{205, 17, 17, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{146, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JProgressBar visualProgressBar = new JProgressBar();
		visualProgressBar.setOrientation(SwingConstants.VERTICAL);
		visualProgressBar.setMaximumSize(new Dimension(100, 14));
		GridBagConstraints gbc_visualProgressBar = new GridBagConstraints();
		gbc_visualProgressBar.anchor = GridBagConstraints.NORTHWEST;
		gbc_visualProgressBar.insets = new Insets(0, 0, 5, 5);
		gbc_visualProgressBar.gridx = 0;
		gbc_visualProgressBar.gridy = 0;
		add(visualProgressBar, gbc_visualProgressBar);
		
		JProgressBar coreProgressBar = new JProgressBar();
		coreProgressBar.setOrientation(SwingConstants.VERTICAL);
		coreProgressBar.setMaximumSize(new Dimension(100, 14));
		GridBagConstraints gbc_coreProgressBar = new GridBagConstraints();
		gbc_coreProgressBar.anchor = GridBagConstraints.NORTHWEST;
		gbc_coreProgressBar.gridx = 6;
		gbc_coreProgressBar.gridy = 1;
		add(coreProgressBar, gbc_coreProgressBar);

	}

}
