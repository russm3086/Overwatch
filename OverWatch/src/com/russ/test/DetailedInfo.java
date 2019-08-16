package com.russ.test;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Rectangle;

import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import com.ansys.cluster.monitor.gui.TableBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class DetailedInfo extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5143757669060695530L;

	/**
	 * Create the panel.
	 */
	public DetailedInfo(DetailedInfoProp masterDiProp) {
		setMinimumSize(new Dimension(210, 160));
		// setPreferredSize(new Dimension(420, 1000));
		setSize(420, 320);

		createDetailInfoPage(masterDiProp);

		setBackground(Color.WHITE);

	}

	private void createDetailInfoPage(DetailedInfoProp masterDiProp) {

		ArrayList<DetailedInfoProp> list = masterDiProp.getDetailedInfoPropList();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createTitle(masterDiProp.getTitleMetric(), masterDiProp.getTitleValue());

		for (int i = 0; i < list.size(); i++) {

			DetailedInfoProp diProp = list.get(i);
			add(createPanel(diProp));
		}
	}

	private void createTitle(String metric, String value) {

		JLabel label = new JLabel();
		StringBuilder sb = new StringBuilder(metric);
		sb.append(value);
		label.setText(sb.toString());
		label.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(label);

	}

	private JPanel getPanel(String strTitle) {

		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(600, 100));
		panel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
		LineBorder roundedLineBorder = new LineBorder(Color.blue, 1, true);
		TitledBorder title = BorderFactory.createTitledBorder(roundedLineBorder, strTitle);
		panel.setBorder(title);
		panel.setBackground(Color.WHITE);
		return panel;

	}

	private JPanel createPanel(DetailedInfoProp diProp) {

		JPanel panel = getPanel(diProp.getPanelName());
		LinkedHashMap<String, Object> store = diProp.getMetricStore();

		for (Entry<String, Object> entry : store.entrySet()) {

			if (diProp.isDataTypeTable()) {

				JScrollPane tableContainer = createTable(entry.getKey(), entry.getValue());
				panel.add(tableContainer);
				panel.setMinimumSize(tableContainer.getMaximumSize());
			} else {

				JLabel label = new JLabel(createDisplay(entry.getKey(), (String) entry.getValue()));
				label.setFont(label.getFont().deriveFont(10));
				panel.add(label);
			}

		}
		return panel;
	}

	private JScrollPane createTable(String tableModleName, Object tableModel) {
		AbstractTableModel abstractTableModel = (AbstractTableModel) tableModel;
		JTable table = TableBuilder.buildTable(tableModleName, abstractTableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// table.setPreferredSize(new Dimension(550, 100));
		Rectangle cellRect = table.getCellRect(5, 7, true);
		table.scrollRectToVisible(cellRect);

		table.setBackground(Color.WHITE);
		int visible_rows = table.getRowCount();
		table.setPreferredScrollableViewportSize(
				new Dimension(table.getPreferredSize().width, table.getRowHeight() * visible_rows));

		JScrollPane tableContainer = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableContainer.setBackground(Color.WHITE);
		tableContainer.getViewport().add(table);
		return tableContainer;
	}

	private String createDisplay(String field, String value) {
		StringBuilder sb = new StringBuilder(field);
		sb.append(value);

		return sb.toString();
	}

}
