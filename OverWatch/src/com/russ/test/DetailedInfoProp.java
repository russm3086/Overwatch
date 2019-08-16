/**
 * 
 */
package com.russ.test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

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
	public final static String key_TitleMetric = "TITLE_METRIC";
	public final static String key_TitleValue = "TITLE_VALUE";
	public final static String key_PanelName = "PANEL_NAME";
	public final static String key_DataType = "DATA_TYPE";
	
	public final static String const_DataTypeString = "STRING";
	public final static String const_DataTypeTable = "TABLE";

	/**
	 * 
	 */
	public DetailedInfoProp() {
		// TODO Auto-generated constructor stub
		setDataType(const_DataTypeString);
	}

	/**
	 * @param defaults
	 */
	public DetailedInfoProp(Properties defaults) {
		super(defaults);
		// TODO Auto-generated constructor stub
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

	public boolean isDataTypeTable() {
		return getDataType().equalsIgnoreCase(const_DataTypeTable);
	}
	
}
