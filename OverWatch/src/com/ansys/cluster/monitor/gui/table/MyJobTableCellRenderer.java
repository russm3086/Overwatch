/**
 * 
 */
package com.ansys.cluster.monitor.gui.table;

import java.awt.Color;
import java.awt.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.russ.util.gui.ZoneBorder;

/**
 * @author rmartine
 *
 */
public class MyJobTableCellRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1930255881967350965L;

	/**
	 * 
	 */
	public MyJobTableCellRenderer() {
		super();
	}

	public Component getTableCellRendererComponent(JTable table, Double value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		System.out.println(value);
		return this;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value == null)
			return this;
		// TODO Fix color bleed Foreground color

		switch (column) {

		case MyJobDetailTableModel.COLUMN_TTL:
			ttlColorCodes(value, table.getModel().getClass(), MyJobDetailTableModel.class);
			break;

		case MyJobVisualTableModel.COLUMN_TTL:
			ttlColorCodes(value, table.getModel().getClass(), MyJobVisualTableModel.class);
			break;

		case MyJobDetailTableModel.COLUMN_EFFICIENCY:
			effColorCodes(value);
			break;

		case MyJobDetailTableModel.COLUMN_LOAD:
			loadEffColorCodes(value);
			break;

		}

		return this;

	}

	private void effColorCodes(Object value) {
		double dblValue = (double) value;
		loadEffColorCodes(dblValue / 100);

	}

	private void loadEffColorCodes(Object value) {
		Color neBorderColor = null;
		Color swBorderColor = null;
		StringBuffer toolTip = new StringBuffer("The ideal values: <BR>");
		toolTip.append("Effienciency: 80 to 120<BR>");
		toolTip.append("Load: .80 to 1.20</html>");

		double dblValue = (double) value;

		if (dblValue > 1.50 || dblValue < .50) {

			neBorderColor = Color.RED;
			swBorderColor = Color.RED;
			toolTip.insert(0, "<html>The value is either extremely too low or too high<BR>");
		} else if (dblValue > 1.40 || dblValue < .60) {

			neBorderColor = Color.RED;
			swBorderColor = Color.ORANGE;
			toolTip.insert(0, "<html>The value is either excessively too low or too high<BR>");
		} else if (dblValue > 1.30 || dblValue < .70) {

			neBorderColor = Color.ORANGE;
			swBorderColor = Color.YELLOW;
			toolTip.insert(0, "<html>The value is either moderately too low or too high<BR>");
		} else if (dblValue > 1.20 || dblValue < .80) {

			neBorderColor = Color.YELLOW;
			swBorderColor = Color.YELLOW;
			toolTip.insert(0, "<html>The value is either slightly too low or too high<BR>");
		}

		if (toolTip != null && toolTip.toString().contains("<html>"))
			setToolTipText(toolTip.toString());
		setBorder(new ZoneBorder(neBorderColor, neBorderColor, swBorderColor, swBorderColor));

	}

	private void ttlColorCodes(Object value, Class<?> current, Class<?> target) {
		if (current == target) {

			ttlColorCodes(value);
		}
	}

	private void ttlColorCodes(Object value) {
		Color neBorderColor = null;
		Color swBorderColor = null;
		String toolTip = null;
		String toolTipSuffix = " hrs. before termination";

		Pattern pattern = Pattern.compile("(\\d+).*\\s+(\\d+)h\\s+(\\d+)m");
		Matcher matcher = pattern.matcher((String) value);

		if (matcher.find()) {
			int days = Integer.parseInt(matcher.group(1));
			int hours = Integer.parseInt(matcher.group(2));

			int hrs = (days * 24) + hours;

			if (hrs <= 1) {
				neBorderColor = Color.RED;
				swBorderColor = Color.RED;
				// setForeground(Color.RED);
				toolTip = "Less then 1" + toolTipSuffix;
			} else if (hrs <= 6) {
				neBorderColor = Color.RED;
				swBorderColor = Color.ORANGE;
				// setForeground(Color.ORANGE);
				toolTip = "Less then 6" + toolTipSuffix;

			} else if (hrs <= 36) {
				neBorderColor = Color.ORANGE;
				swBorderColor = Color.YELLOW;
				toolTip = "Less then 36" + toolTipSuffix;

			} else if (hrs <= 72) {
				neBorderColor = Color.YELLOW;
				swBorderColor = Color.YELLOW;
				toolTip = "Less then 72" + toolTipSuffix;

			}

			if (toolTip != null)
				setToolTipText(toolTip);
			setBorder(new ZoneBorder(neBorderColor, neBorderColor, swBorderColor, swBorderColor));
		}

	}

}
