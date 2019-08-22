/**
 * 
 */
package com.russ.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DecimalFormat;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.ansys.cluster.monitor.gui.tree.DetailedInfoProp;

/**
 * @author rmartine
 *
 */
public class Test {

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JFrame frame = new JFrame();

		ChartPanel panel = createAvailableChartPanel("Cores", "core(s)",1440, 720);

		frame.add(panel, BorderLayout.CENTER);

		frame.setSize(640, 480);
		//frame.setSize(320, 240);
		frame.setVisible(true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private static ChartPanel createAvailableChartPanel(String title, String unit, Number numAvail, Number numUnavail) {

		DetailedInfoProp diProp = new DetailedInfoProp();
		diProp.setChartDataTitle(title);
		diProp.setChartDataUnit(unit);

		diProp.addChartData("Available", numAvail, new Color(0, 153, 0));
		diProp.addChartData("Unavailable", numUnavail, new Color(204, 0, 0));

		final ChartPanel chartPanel = createChartPanel(diProp);
		return chartPanel;

	}

	private static ChartPanel createChartPanel(DetailedInfoProp diProp) {

		final JFreeChart chart = createPieChart(diProp);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(320, 240));
		chartPanel.setBackground(Color.WHITE);

		return chartPanel;
	}

	private static DefaultPieDataset createPieDataset(DetailedInfoProp diProp) {

		final DefaultPieDataset result = new DefaultPieDataset();

		for (DetailedInfoProp dataSet : diProp.getChartDataList()) {
			result.setValue(dataSet.getChartDataKey(), dataSet.getChartDataValue());
		}

		return result;
	}

	private static JFreeChart createPieChart(DetailedInfoProp diProp) {

		PieDataset pieDataset = createPieDataset(diProp);

		final JFreeChart chart = ChartFactory.createPieChart3D(diProp.getChartDataTitle(), pieDataset, true, true,
				false);
		final PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(180);
		plot.setDepthFactor(.10);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(.6f);
		plot.setNoDataMessage("No data to display");
		plot.setMinimumArcAngleToDraw(.3);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setCircular(true);
		plot.setShadowPaint(Color.BLACK);

		StringBuilder sb = new StringBuilder("{1} ");
		sb.append(diProp.getChartDataUnit());
		sb.append(" ({2})");

		PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(sb.toString(), new DecimalFormat("0"),
				new DecimalFormat("0%"));

		plot.setLabelGenerator(gen);
		plot.setDarkerSides(true);

		setSectionPaint(plot, diProp);

		return chart;

	}

	private static void setSectionPaint(PiePlot3D plot, DetailedInfoProp diProp) {

		for (DetailedInfoProp dataSet : diProp.getChartDataList()) {
			plot.setSectionPaint(dataSet.getChartDataKey(), dataSet.getChartDataPaint());
		}

	}

}
