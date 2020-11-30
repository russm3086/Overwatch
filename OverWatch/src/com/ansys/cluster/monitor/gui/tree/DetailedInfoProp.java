/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.jfree.chart.plot.PlotOrientation;

import com.russ.util.AbstractProp;

/**
 * @author rmartine
 *
 */
public class DetailedInfoProp extends AbstractProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 422636128246694442L;
	private LinkedHashMap<String, Object> metricStore = new LinkedHashMap<String, Object>();
	private ArrayList<DetailedInfoProp> listDetailedInfoProp = new ArrayList<DetailedInfoProp>();
	private final static String key_TitleMetric = "TITLE_METRIC";
	private final static String key_TitleValue = "TITLE_VALUE";
	private final static String key_PanelName = "PANEL_NAME";
	private final static String key_DataType = "DATA_TYPE";
	private final static String key_ChartDataKey = "CHART_DATA_KEY";
	private final static String key_ChartDataValue = "CHART_DATA_VALUE";
	private final static String key_ChartDataPaint = "CHART_DATA_PAINT";
	private final static String key_ChartDataColor = "CHART_DATA_COLOR";
	private final static String key_ChartDataTitle = "CHART_DATA_TITLE";
	private final static String key_ChartDataUnit = "CHART_DATA_UNIT";
	private final static String key_Chart_yAxisLabel = "CHART_Y_AXIS_LABEL";
	private final static String key_Chart_xAxisLabel = "CHART_X_AXIS_LABEL";
	private final static String key_ChartDataSeriesData = "CHART_DATA_SERIES_DATA";
	private final static String key_ChartDataColumnKey = "CHART_DATA_COLUMN_KEY";
	private final static String key_ChartDataRowKey = "CHART_DATA_ROW_KEY";
	private final static String key_ChartSectionColorsBool = "CHART_SECTION_COLORS_KEY_BOOL";

	private final static String key_PlotOrientation = "PLOT_ORIENTATION";
	private final static String key_ProgressBarLbl = "PROGRESS_BAR_LBL";
	private final static String key_ProgressBarData = "PROGRESS_BAR_DATA";
	private final static String key_ProgressBarUnit = "PROGRESS_BAR_UNIT";
	private final static String key_ProgressBarToolTips = "PROGRESS_BAR_TOOL_TIPS";

	public final static String const_DataTypeString = "STRING";
	public final static String const_DataTypeTable = "TABLE";
	public final static String const_DataTypeTextArea = "TEXT_AREA";
	public final static String const_DataTypePieChart = "PIE_CHART";
	public final static String const_DataTypeBubbleChart = "BUBBLE_CHART";
	public final static String const_DataTypeBarChart = "BAR_CHART";
	public final static String const_DataTypeProgressBarChart = "PROGRESS_BAR_CHART";
	public final static String const_DataTypeScatterPlotChart = "SCATTER_PLOT_CHART";

	/**
	 * 
	 */
	public DetailedInfoProp() {
		setDataType(const_DataTypeString);
		setPlotOrientation(PlotOrientation.VERTICAL);
		setSectionColorTrue();
	}

	/**
	 * @param defaults
	 */
	public DetailedInfoProp(Properties defaults) {
		super(defaults);
	}

	public void get_xAxisLabel(String label) {
		metricStore.put(key_Chart_xAxisLabel, label);
	}

	public String get_xAxisLabel() {
		return (String) metricStore.get(key_Chart_xAxisLabel);
	}

	public void get_yAxisLabel(String label) {
		metricStore.put(key_Chart_yAxisLabel, label);
	}

	public String get_yAxisLabel() {
		return (String) metricStore.get(key_Chart_yAxisLabel);
	}

	public void setChartDataTitle(String title) {
		metricStore.put(key_ChartDataTitle, title);
	}

	public String getChartDataTitle() {
		return (String) metricStore.get(key_ChartDataTitle);
	}

	public void setChartDataUnit(String unit) {
		metricStore.put(key_ChartDataUnit, unit);
	}

	public String getChartDataUnit() {
		return (String) metricStore.get(key_ChartDataUnit);
	}

	public void addChartData(Comparable<?> key, Number value) {

		DetailedInfoProp chartData = new DetailedInfoProp();

		chartData.addChartDataKey(key);
		chartData.addChartDataValue(value);

		addDetailedInfoProp(chartData);
	}

	public void addChartData(Number value, Comparable<?> rowKey, Comparable<?> columnKey) {
		DetailedInfoProp chartData = new DetailedInfoProp();

		chartData.addChartDataValue(value);
		chartData.addChartRowKey(rowKey);
		chartData.addChartColumnKey(columnKey);

		addDetailedInfoProp(chartData);

	}

	public void addChartData(Comparable<?> key, Number value, Paint paint) {

		DetailedInfoProp chartData = new DetailedInfoProp();

		chartData.addChartDataKey(key);
		chartData.addChartDataValue(value);
		chartData.addChartDataPaint(paint);

		addDetailedInfoProp(chartData);
	}

	public void addChartData(Comparable<?> key, Number value, Color color) {

		DetailedInfoProp chartData = new DetailedInfoProp();

		chartData.addChartDataKey(key);
		chartData.addChartDataValue(value);
		chartData.addChartDataColor(color);

		addDetailedInfoProp(chartData);
	}

	public ArrayList<DetailedInfoProp> getChartDataList() {
		return getDetailedInfoPropList();
	}

	public void addSeries(String seriesName, int[] data, String units, String toolTips) {

		DetailedInfoProp chartDataSeries = new DetailedInfoProp();
		chartDataSeries.setProgressBarLabel(seriesName);
		chartDataSeries.setProgressBarData(data);
		chartDataSeries.setProgressBarUnits(units);
		chartDataSeries.setProgressBarToolTips(toolTips);

		addDetailedInfoProp(chartDataSeries);
	}

	public void addSeries(Comparable<?> seriesKey, double[][] data, Color color) {

		DetailedInfoProp chartDataSeries = new DetailedInfoProp();
		chartDataSeries.addChartDataKey(seriesKey);
		chartDataSeries.addChartDataSeriesData(data);
		chartDataSeries.addChartDataColor(color);

		addDetailedInfoProp(chartDataSeries);
	}

	public void addSeries(Comparable<?> seriesKey, double[][] data, Paint paint) {

		DetailedInfoProp chartDataSeries = new DetailedInfoProp();
		chartDataSeries.addChartDataKey(seriesKey);
		chartDataSeries.addChartDataSeriesData(data);
		chartDataSeries.addChartDataPaint(paint);

		addDetailedInfoProp(chartDataSeries);
	}

	public void setSectionColorTrue() {
		setBoolProperty(key_ChartSectionColorsBool, true);
	}

	public void setSectionColorFalse() {
		setBoolProperty(key_ChartSectionColorsBool, false);
	}

	public boolean usingSectionColor() {
		return (boolean) getBoolProperty(key_ChartSectionColorsBool);
	}

	public void setProgressBarToolTips(String toolTips) {
		metricStore.put(key_ProgressBarToolTips, toolTips);
	}

	public String getProgressBarToolTips() {
		return (String) metricStore.get(key_ProgressBarToolTips);
	}

	public void setProgressBarUnits(String units) {
		metricStore.put(key_ProgressBarUnit, units);
	}

	public String getProgressBarUnits() {
		return (String) metricStore.get(key_ProgressBarUnit);
	}

	public void setProgressBarLabel(String label) {
		metricStore.put(key_ProgressBarLbl, label);
	}

	public String getProgressBarLabel() {
		return (String) metricStore.get(key_ProgressBarLbl);
	}

	public void setProgressBarData(int[] data) {
		metricStore.put(key_ProgressBarData, data);
	}

	public int[] getProgressBarData() {
		return (int[]) metricStore.get(key_ProgressBarData);
	}

	public void addChartRowKey(Comparable<?> rowKey) {
		metricStore.put(key_ChartDataRowKey, rowKey);
	}

	public Comparable<?> getChartRowKey() {
		return (Comparable<?>) metricStore.get(key_ChartDataRowKey);
	}

	public void addChartColumnKey(Comparable<?> columnKey) {
		metricStore.put(key_ChartDataColumnKey, columnKey);
	}

	public Comparable<?> getChartColumnKey() {
		return (Comparable<?>) metricStore.get(key_ChartDataColumnKey);
	}

	public void addChartDataSeriesData(double[][] data) {
		metricStore.put(key_ChartDataSeriesData, data);
	}

	public double[][] getChartDataSeriesData() {
		return (double[][]) metricStore.get(key_ChartDataSeriesData);
	}

	public void addChartDataKey(Comparable<?> key) {
		metricStore.put(key_ChartDataKey, key);
	}

	public Comparable<?> getChartDataKey() {
		return (Comparable<?>) metricStore.get(key_ChartDataKey);
	}

	public void addChartDataPaint(Paint paint) {
		metricStore.put(key_ChartDataPaint, paint);
	}

	public Paint getChartDataPaint() {
		return (Paint) metricStore.get(key_ChartDataPaint);
	}

	public void addChartDataColor(Color color) {
		metricStore.put(key_ChartDataColor, color);
	}

	public Color getChartDataColor() {
		return (Color) metricStore.get(key_ChartDataColor);
	}

	public void addChartDataValue(Number value) {
		metricStore.put(key_ChartDataValue, value);
	}

	public Number getChartDataValue() {
		return (Number) metricStore.get(key_ChartDataValue);
	}

	public String getTitleMetric() {
		return getLogProperty(key_TitleMetric);
	}

	public void setTitleMetric(String metric) {
		putLog(key_TitleMetric, metric);
	}

	public String getTitleValue() {
		return getLogProperty(key_TitleValue);
	}

	public void setTitleValue(String value) {
		putLog(key_TitleValue, value);
	}

	public String getPanelName() {
		return getLogProperty(key_PanelName);
	}

	public void setPanelName(String panelName) {
		putLog(key_PanelName, panelName);
	}

	public void addMetric(String metric1, String metric2, Object value) {
		StringBuilder sb = new StringBuilder(metric1);
		sb.append(metric2);
		metricStore.put(sb.toString(), value);
	}

	public void addMetric(String metric, Object value) {
		metricStore.put(metric, value);
	}

	public void addMetric(String metric, String value) {
		metricStore.put(metric, value);
	}

	public void addMetric(String metric, int value) {

		metricStore.put(metric, String.valueOf(value));
	}

	public Object getMetric(String metric) {
		return metricStore.get(metric);
	}

	/**
	 * @return the store
	 */
	public LinkedHashMap<String, Object> getMetricStore() {
		return metricStore;
	}

	/**
	 * @param store the store to set
	 */
	public void setMetricStore(LinkedHashMap<String, Object> metricStore) {
		this.metricStore = metricStore;
	}

	public void addDetailedInfoProp(DetailedInfoProp diProp) {
		listDetailedInfoProp.add(diProp);
	}

	public ArrayList<DetailedInfoProp> getDetailedInfoPropList() {
		return listDetailedInfoProp;
	}

	public String getDataType() {
		return getLogProperty(key_DataType);
	}

	public void setDataType(String type) {
		putLog(key_DataType, type);
	}

	public void setDataTypeTable() {
		setDataType(const_DataTypeTable);
	}

	public void setDataTypeString() {
		setDataType(const_DataTypeString);
	}

	public void setDataTypeTextArea() {
		setDataType(const_DataTypeTextArea);
	}

	public void setDataTypePieChart() {
		setDataType(const_DataTypePieChart);
	}

	public void setDataTypeBarChart() {
		setDataType(const_DataTypeBarChart);
	}

	public void setDataTypeScatterPlotChart() {
		setDataType(const_DataTypeScatterPlotChart);
	}

	public void setDataTypeBubbleChart() {
		setDataType(const_DataTypeBubbleChart);
	}

	public void setDataTypeProgressBarChart() {
		setDataType(const_DataTypeProgressBarChart);
	}

	public void setPlotOrientation(PlotOrientation po) {
		putLog(key_PlotOrientation, po);
	}

	public PlotOrientation getPlotOrientation() {
		return (PlotOrientation) get(key_PlotOrientation);
	}

	public boolean isDataTypeTable() {
		return getDataType().equalsIgnoreCase(const_DataTypeTable);
	}

}
