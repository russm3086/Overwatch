package com.ansys.cluster.monitor.gui.tree;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BubbleXYItemLabelGenerator;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardXYZToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.util.Rotation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;

import com.ansys.cluster.monitor.gui.table.TableBuilder;
import com.ansys.cluster.monitor.gui.table.TableMouseListener;
import com.russ.util.OvalBorder;
import com.russ.util.WrapLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class DetailedInfoPanel extends JPanel {

	protected DetailedInfoProp masterDiProp;
	protected JTree tree;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5143757669060695530L;

	/**
	 * Create the panel.
	 */
	public DetailedInfoPanel(DetailedInfoProp masterDiProp, JTree tree) {
		setMinimumSize(new Dimension(210, 160));
		setSize(420, 320);
		this.masterDiProp = masterDiProp;
		this.tree = tree;

		setBackground(Color.WHITE);

	}

	public void createPanel() {

		createDetailInfoPage(masterDiProp);

	}

	protected void createDetailInfoPage(DetailedInfoProp masterDiProp) {

		ArrayList<DetailedInfoProp> list = masterDiProp.getDetailedInfoPropList();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createTitle(masterDiProp.getTitleMetric(), masterDiProp.getTitleValue());

		for (int i = 0; i < list.size(); i++) {

			DetailedInfoProp diProp = list.get(i);
			add(createPanel(diProp));
		}
	}

	protected void createTitle(String metric, String value) {

		JLabel label = new JLabel();
		StringBuilder sb = new StringBuilder(metric);
		sb.append(value);
		label.setText(sb.toString());
		label.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(label);

	}

	protected JPanel getPanel(String strTitle) {

		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(600, 100));
		panel.setLayout(new WrapLayout(FlowLayout.LEADING));

		LineBorder roundedLineBorder = new LineBorder(Color.blue, 1, true);
		TitledBorder title = BorderFactory.createTitledBorder(roundedLineBorder, strTitle);
		panel.setBorder(title);
		panel.setBackground(Color.WHITE);
		return panel;

	}

	protected JPanel createPanel(DetailedInfoProp diProp) {

		JPanel panel = getPanel(diProp.getPanelName());
		LinkedHashMap<String, Object> store = diProp.getMetricStore();

		for (Entry<String, Object> entry : store.entrySet()) {

			switch (diProp.getDataType()) {

			case DetailedInfoProp.const_DataTypeTable:

				panel.setLayout(new BorderLayout());
				JScrollPane tableContainer = createTable(entry.getKey(), entry.getValue());
				panel.add(BorderLayout.CENTER, tableContainer);
				break;

			case DetailedInfoProp.const_DataTypeString:

				JLabel label = new JLabel(createDisplay(entry.getKey(), entry.getValue()));
				label.setFont(label.getFont().deriveFont(10));
				Color lightBlue = new Color(0, 0, 182, 155);
				Border ovalBorder = new OvalBorder(10, 10, lightBlue, Color.BLUE);
				label.setBorder(ovalBorder);
				panel.add(label);
				break;

			case DetailedInfoProp.const_DataTypeTextArea:

				panel.setLayout(new BorderLayout());
				JScrollPane textAreaContainer = createTextArea((String) entry.getValue());
				panel.add(BorderLayout.CENTER, textAreaContainer);

				break;

			case DetailedInfoProp.const_DataTypePieChart:

				panel.setLayout(new BorderLayout());
				ChartPanel chartPanelContainer = createPieChartPanel(diProp);
				panel.add(BorderLayout.CENTER, chartPanelContainer);

				break;

			case DetailedInfoProp.const_DataTypeBubbleChart:

				panel.setLayout(new BorderLayout());
				ChartPanel chartPanel = createBubbleChartPanel(diProp);
				panel.add(BorderLayout.CENTER, chartPanel);

				break;

			case DetailedInfoProp.const_DataTypeBarChart:

				panel.setLayout(new BorderLayout());
				ChartPanel barChartPanel = createBarChartPanel(diProp);
				panel.add(BorderLayout.CENTER, barChartPanel);

				break;
			}

		}
		return panel;
	}

	protected JScrollPane createTextArea(String content) {

		JTextArea textArea = new JTextArea(content);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);

		JScrollPane areaScrollPane = new JScrollPane(textArea);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(250, 250));

		return areaScrollPane;
	}

	protected JScrollPane createTable(String tableModleName, Object tableModel) {

		AbstractTableModel abstractTableModel = (AbstractTableModel) tableModel;
		JTable table = TableBuilder.buildTable(tableModleName, abstractTableModel);

		int column = table.getRowSorter().getSortKeys().get(0).getColumn();
		table.addMouseListener(new TableMouseListener(table, column, tree));

		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setBackground(Color.WHITE);

		int visible_rows = table.getRowCount() < 5 ? table.getRowCount() : 5;

		table.setPreferredScrollableViewportSize(
				new Dimension(table.getPreferredSize().width, table.getRowHeight() * visible_rows));
		table.setFillsViewportHeight(true);

		JScrollPane tableContainer = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableContainer.setBackground(Color.WHITE);
		tableContainer.getViewport().add(table);

		return tableContainer;
	}

	protected ChartPanel createPieChartPanel(DetailedInfoProp diProp) {

		final JFreeChart chart = createPieChart(diProp);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(320, 240));
		chartPanel.setBackground(Color.WHITE);

		return chartPanel;
	}

	protected ChartPanel createBarChartPanel(DetailedInfoProp diProp) {

		final JFreeChart chart = createBarChart(diProp);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(320, 240));
		chartPanel.setBackground(Color.WHITE);

		return chartPanel;
	}

	protected CategoryDataset createChartDataset(DetailedInfoProp diProp) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (DetailedInfoProp chartDataSet : diProp.getChartDataList()) {
			dataset.addValue(chartDataSet.getChartDataValue(), chartDataSet.getChartRowKey(),
					chartDataSet.getChartColumnKey());
		}
		return dataset;
	}

	protected JFreeChart createBarChart(DetailedInfoProp diProp) {

		CategoryDataset dataset = createChartDataset(diProp);

		
		
		final JFreeChart chart = ChartFactory.createBarChart(diProp.getChartDataTitle(), // chart title
				"", // domain axis label
				"", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				false, // include legend
				true, // tooltips?
				false // URLs?
		);

		chart.getTitle().setFont(new Font(chart.getTitle().getFont().getName(), Font.PLAIN, 12));
		
		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customization...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
		BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
		Color color = new Color(0, 153, 0);;
		barRenderer.setSeriesPaint(0, color);
		barRenderer.setMaximumBarWidth(.2);
		
		
		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setUpperMargin(0.15);

		// disable bar outlines...
		final CategoryItemRenderer renderer = plot.getRenderer();
		renderer.setSeriesItemLabelsVisible(0, Boolean.TRUE);

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		
		
		renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());

		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}

	protected DefaultPieDataset createPieDataset(DetailedInfoProp diProp) {

		final DefaultPieDataset result = new DefaultPieDataset();

		for (DetailedInfoProp dataSet : diProp.getChartDataList()) {
			result.setValue(dataSet.getChartDataKey(), dataSet.getChartDataValue());
		}

		return result;
	}

	protected JFreeChart createPieChart(DetailedInfoProp diProp) {

		PieDataset pieDataset = createPieDataset(diProp);

		final JFreeChart chart = ChartFactory.createPieChart3D(diProp.getChartDataTitle(), pieDataset, true, true,
				false);
		chart.getTitle().setFont(new Font(chart.getTitle().getFont().getName(), Font.PLAIN, 12));

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

	protected ChartPanel createBubbleChartPanel(DetailedInfoProp diProp) {

		JFreeChart chart = createBubbleChart(diProp);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(320, 240));
		chartPanel.setBackground(Color.WHITE);

		chartPanel.setDomainZoomable(true);
		chartPanel.setRangeZoomable(true);
		return chartPanel;

	}

	protected JFreeChart createBubbleChart(DetailedInfoProp diProp) {

		XYZDataset dataSet = createXYZ_Dataset(diProp);

		JFreeChart chart = ChartFactory.createBubbleChart(diProp.getChartDataTitle(), diProp.get_xAxisLabel(),
				diProp.get_yAxisLabel(), dataSet, PlotOrientation.HORIZONTAL, true, true, false);

		// Set range for X-Axis
		XYPlot plot = chart.getXYPlot();
		plot.setForegroundAlpha(0.70F);

		XYItemRenderer xyitemrenderer = plot.getRenderer();
		bubbleChartDataPaint(xyitemrenderer, diProp);

		NumberAxis numberYaxis = (NumberAxis) plot.getDomainAxis();
		numberYaxis.setLowerMargin(.2);
		numberYaxis.setUpperMargin(.4);

		// Set range for Y-Axis
		NumberAxis numberXaxis = (NumberAxis) plot.getRangeAxis();
		numberXaxis.setLowerMargin(0.1);
		numberXaxis.setUpperMargin(0.2);
		numberXaxis.setRange(-400, 1600);

		// Format label
		XYBubbleRenderer renderer = (XYBubbleRenderer) plot.getRenderer();
		BubbleXYItemLabelGenerator generator = new BubbleXYItemLabelGenerator("{3}", new DecimalFormat("0"),
				new DecimalFormat("0"), new DecimalFormat("0"));

		StandardXYZToolTipGenerator xyzTooltipGenerator = new StandardXYZToolTipGenerator(
				"{0} Core:{3} Duration:{2}  Host Load:{1}", new DecimalFormat("0.00"), new DecimalFormat("0"),
				new DecimalFormat("0"));

		renderer.setDefaultToolTipGenerator(xyzTooltipGenerator);
		renderer.setDefaultItemLabelGenerator(generator);
		renderer.setDefaultItemLabelsVisible(true);
		renderer.setDefaultOutlinePaint(Color.BLACK);

		return chart;
	}

	protected XYZDataset createXYZ_Dataset(DetailedInfoProp diProp) {

		DefaultXYZDataset xyzDataset = new DefaultXYZDataset();

		for (DetailedInfoProp dataSet : diProp.getChartDataList()) {
			xyzDataset.addSeries(dataSet.getChartDataKey(), dataSet.getChartDataSeriesData());
		}

		return xyzDataset;
	}

	protected void bubbleChartDataPaint(XYItemRenderer xyitemrenderer, DetailedInfoProp diProp) {

		int i = 0;
		for (DetailedInfoProp dataSet : diProp.getChartDataList()) {
			xyitemrenderer.setSeriesPaint(i, dataSet.getChartDataPaint());
			i += 1;
		}

	}

	protected void setSectionPaint(PiePlot3D plot, DetailedInfoProp diProp) {

		for (DetailedInfoProp dataSet : diProp.getChartDataList()) {
			plot.setSectionPaint(dataSet.getChartDataKey(), dataSet.getChartDataPaint());
		}

	}

	protected String createDisplay(String field, Object value) {
		StringBuilder sb = new StringBuilder(field);
		sb.append(value);

		return sb.toString();
	}

}
