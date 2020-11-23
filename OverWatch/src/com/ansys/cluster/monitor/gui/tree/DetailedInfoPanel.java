package com.ansys.cluster.monitor.gui.tree;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
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
import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DFactory;
import com.orsoncharts.Chart3DPanel;
import com.orsoncharts.data.PieDataset3D;
import com.orsoncharts.data.StandardPieDataset3D;
import com.orsoncharts.data.xyz.XYZSeries;
import com.orsoncharts.data.xyz.XYZSeriesCollection;
import com.orsoncharts.graphics3d.Dimension3D;
import com.orsoncharts.graphics3d.ViewPoint3D;
import com.orsoncharts.graphics3d.swing.DisplayPanel3D;
import com.orsoncharts.label.StandardPieLabelGenerator;
import com.orsoncharts.label.StandardXYZLabelGenerator;
import com.orsoncharts.plot.StandardColorSource;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.renderer.xyz.ScatterXYZRenderer;
import com.orsoncharts.style.ChartStyles;
import com.orsoncharts.util.Anchor2D;
import com.russ.util.OvalBorder;
import com.russ.util.WrapLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

public class DetailedInfoPanel extends JPanel {

	protected DetailedInfoProp masterDiProp;
	protected JTree tree;
	protected Font titleBorderFont;
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
		titleBorderFont = UIManager.getDefaults().getFont("TitledBorder.font");

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

		StringBuilder sb = new StringBuilder(metric);
		sb.append(value);

		JTextField field = new JTextField();
		field.setEditable(false); // as before
		field.setBackground(null);
		field.setBorder(null); // remove the border
		field.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		field.setAlignmentX(Component.CENTER_ALIGNMENT);
		field.setMaximumSize(new Dimension(300, 20));
		field.setText(sb.toString());

		add(field);

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

				JTextField label = new JTextField(createDisplay(entry.getKey(), entry.getValue()));
				label.setFont(label.getFont().deriveFont(10));
				Color lightBlue = new Color(0, 0, 182, 155);
				Border ovalBorder = new OvalBorder(10, 10, lightBlue, Color.BLUE);
				label.setEditable(false);
				label.setBackground(UIManager.getColor("TextField.activeBackground"));
				label.setBorder(ovalBorder);
				panel.add(label);
				break;

			case DetailedInfoProp.const_DataTypeTextArea:

				panel.setLayout(new BorderLayout());
				JScrollPane textAreaContainer = createTextArea((String) entry.getValue());
				panel.add(BorderLayout.CENTER, textAreaContainer);

				break;

			case "OldChart":

				panel.setLayout(new BorderLayout());
				ChartPanel chartPanelContainer = createPieChartPanel(diProp);
				panel.add(BorderLayout.CENTER, chartPanelContainer);

				break;

			case DetailedInfoProp.const_DataTypePieChart:

				panel.setLayout(new BorderLayout());
				Chart3DPanel chart3dPanel = createPie3dChartPanel(diProp);
				panel.add(BorderLayout.CENTER, chart3dPanel);

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

			case DetailedInfoProp.const_DataTypeScatterPlotChart:

				panel.setLayout(new BorderLayout());
				Chart3DPanel scatterPlotPanel = createScatterPlotPanel(diProp);
				DisplayPanel3D panel3D = new DisplayPanel3D(scatterPlotPanel);
				panel.add(BorderLayout.CENTER, panel3D);

				break;

			case DetailedInfoProp.const_DataTypeProgressBarChart:

				panel.setLayout(new BorderLayout());
				JPanel progressBar = createProgressBarChart(diProp);
				panel.add(BorderLayout.CENTER, progressBar);
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
		chartPanel.setPreferredSize(new java.awt.Dimension(320, 180));
		chartPanel.setBackground(Color.WHITE);

		return chartPanel;
	}

	protected ChartPanel createBarChartPanel(DetailedInfoProp diProp) {

		final JFreeChart chart = createBarChart(diProp);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(320, 180));
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
				diProp.getPlotOrientation(), // orientation
				false, // include legend
				true, // tooltips?
				false // URLs?
		);

		chart.getTitle().setFont(titleBorderFont.deriveFont(Font.BOLD));

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customization...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
		BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
		barRenderer.setShadowVisible(true);

		if (diProp.getChartDataPaint() == null) {

			barRenderer.setSeriesPaint(0, new Color(0, 153, 0));
		} else {

			barRenderer.setSeriesPaint(0, diProp.getChartDataPaint());
		}

		barRenderer.setMaximumBarWidth(.2);
		// barRenderer.setDefaultToolTipGenerator(new
		// StandardCategoryToolTipGenerator());

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

		chart.getTitle().setFont(titleBorderFont.deriveFont(Font.BOLD));

		final PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(180);
		plot.setDepthFactor(.10);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(.6f);
		plot.setNoDataMessage("No data to display");
		plot.setMinimumArcAngleToDraw(1);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setCircular(true);
		plot.setDarkerSides(true);

		// plot.setShadowGenerator(new DefaultShadowGenerator(10, Color.BLACK, .25f, 1,
		// -Math.PI /16));

		StringBuilder sb = new StringBuilder("{1} ");
		sb.append(diProp.getChartDataUnit());
		sb.append(" ({2})");

		PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(sb.toString(), new DecimalFormat("0"),
				new DecimalFormat("0%"));

		plot.setLabelGenerator(gen);

		setSectionPaint(plot, diProp);

		return chart;

	}

	protected Chart3DPanel createScatterPlotPanel(DetailedInfoProp diProp) {
		Chart3D chart = createScatterPlot(diProp);
		Chart3DPanel chartPanel = new Chart3DPanel(chart);

		return chartPanel;
	}

	protected com.orsoncharts.data.xyz.XYZDataset<String> createXYZDataset(DetailedInfoProp diProp) {
		XYZSeriesCollection<String> dataset = new XYZSeriesCollection<String>();

		for (DetailedInfoProp dataSet : diProp.getChartDataList()) {

			XYZSeries<String> series = new XYZSeries<String>((String) dataSet.getChartDataKey());

			double[][] data = dataSet.getChartDataSeriesData();

			for (int row = 0; row < data[0].length; row++) {

				double load = data[0][row];
				double duration = data[1][row];
				double cores = data[2][row];
				series.add(duration, load, cores);
			}
			dataset.add(series);
		}

		return dataset;
	}

	protected Chart3D createScatterPlot(DetailedInfoProp diProp) {

		com.orsoncharts.data.xyz.XYZDataset<String> dataset = createXYZDataset(diProp);
		Chart3D chart = Chart3DFactory.createScatterChart("", "", dataset, "Duration", "Load", "Core(s)");
		chart.setViewPoint(ViewPoint3D.createAboveLeftViewPoint(15));

		XYZPlot plot = (XYZPlot) chart.getPlot();
		plot.setDimensions(new Dimension3D(10.0, 4.0, 4.0));
		plot.setLegendLabelGenerator(new StandardXYZLabelGenerator(StandardXYZLabelGenerator.COUNT_TEMPLATE));
		BasicStroke stroke = new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.1F);
		plot.setGridlineStrokeX(stroke);
		plot.getXAxis().setLabelFont(titleBorderFont.deriveFont(titleBorderFont.getSize2D() + 10));
		plot.getYAxis().setLabelFont(titleBorderFont.deriveFont(titleBorderFont.getSize2D() + 10));
		plot.getZAxis().setLabelFont(titleBorderFont.deriveFont(titleBorderFont.getSize2D() + 10));
		plot.setGridlinesVisibleZ(true);

		ScatterXYZRenderer renderer = (ScatterXYZRenderer) plot.getRenderer();
		renderer.setSize(0.5);
		renderer.setColors(getColors(diProp));

		return chart;
	}

	protected PieDataset3D<String> create3dDataset(DetailedInfoProp diProp) {

		StandardPieDataset3D<String> dataSet3d = new StandardPieDataset3D<String>();

		for (DetailedInfoProp dataSet : diProp.getChartDataList()) {
			dataSet3d.add((String) dataSet.getChartDataKey(), dataSet.getChartDataValue());
		}

		return dataSet3d;
	}

	protected Chart3DPanel createPie3dChartPanel(DetailedInfoProp diProp) {

		final Chart3D chart = createPie3dChart(diProp);
		final Chart3DPanel chartPanel = new Chart3DPanel(chart);
		chartPanel.setMargin(0.15);
		chartPanel.setBackground(Color.WHITE);

		return chartPanel;
	}

	protected Chart3D createPie3dChart(DetailedInfoProp diProp) {

		PieDataset3D<String> pieDataset = create3dDataset(diProp);
		Chart3D chart = Chart3DFactory.createPieChart(diProp.getChartDataTitle(), "", pieDataset);

		chart.setStyle(ChartStyles.createPastelStyle());

		chart.setTitle(diProp.getChartDataTitle(), titleBorderFont, Color.BLACK);

		ViewPoint3D vp = ViewPoint3D.createAboveViewPoint(35);
		vp.panLeftRight(-Math.PI / 8);
		chart.setViewPoint(vp);
		chart.setTitleAnchor(Anchor2D.TOP_CENTER);
		chart.setLegendAnchor(Anchor2D.BOTTOM_CENTER);

		com.orsoncharts.plot.PiePlot3D plot = (com.orsoncharts.plot.PiePlot3D) chart.getPlot();
		plot.setLegendLabelGenerator(new StandardPieLabelGenerator("%2$s %s"));
		plot.setDepth(.5);

		plot.setSectionLabelGenerator(new StandardPieLabelGenerator("%3$,.2f%%"));

		plot.setToolTipGenerator(new StandardPieLabelGenerator("%s: %2$s (%3$,.2f%%)"));

		setSectionColor(plot, diProp);

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

	protected JPanel createProgressBarChart(DetailedInfoProp diProp) {

		JPanel panel = new JPanel();
		panel.setBackground(this.getBackground());
		JPanel ProgessBarPanel = createProgressBar(diProp);
		panel.add(ProgessBarPanel);

		return panel;
	}

	protected JPanel createProgressBar(DetailedInfoProp diProp) {

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createLineBorder(Color.black));

		panel.setPreferredSize(new Dimension(320, 180));

		ArrayList<DetailedInfoProp> list = diProp.getDetailedInfoPropList();

		panel.setLayout(new GridLayout(list.size(), 1, 10, 10));

		for (DetailedInfoProp diPropChart : list) {

			int[] data = diPropChart.getProgressBarData();

			ProgressBar progressBar = new ProgressBar(diPropChart.getProgressBarLabel(), data[0], data[1], data[2],
					diPropChart.getProgressBarUnits(), diPropChart.getProgressBarToolTips());
			panel.add(progressBar);
		}

		return panel;
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

	protected Color[] getColors(DetailedInfoProp diProp) {

		ArrayList<Color> colorList = new ArrayList<Color>();

		for (DetailedInfoProp dataSet : diProp.getChartDataList()) {

			colorList.add(dataSet.getChartDataColor());
		}

		return colorList.toArray(new Color[0]);
	}

	protected void setSectionColor(com.orsoncharts.plot.PiePlot3D plot, DetailedInfoProp diProp) {

		if (diProp.usingSectionColor()) {

			StandardColorSource<String> colorSource = new StandardColorSource<String>();

			for (DetailedInfoProp dataSet : diProp.getChartDataList()) {

				colorSource.setColor((String) dataSet.getChartDataKey(), dataSet.getChartDataColor());
			}
			plot.setSectionColorSource(colorSource);
		}

	}

	protected String createDisplay(String field, Object value) {
		StringBuilder sb = new StringBuilder(field);
		sb.append(value);

		return sb.toString();
	}

}
