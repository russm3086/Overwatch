/**
 * 
 */
package com.russ.util.gui;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

/**
 * @author rmartine
 *
 */
public class DisplayTool {

	private int totalWidth = 0;
	private int totalHeight = 0;
	private int numOfDevices = 0;
	private int maxWidth = 0;
	private int maxHeight = 0;
	private ArrayList<String> listDeviceId = new ArrayList<String>();

	/**
	 * 
	 */
	public DisplayTool() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] graphicDevices = ge.getScreenDevices();
		for (GraphicsDevice device : graphicDevices) {

			listDeviceId.add(device.getIDstring());

			DisplayMode mode = device.getDisplayMode();

			totalWidth += mode.getWidth();
			totalHeight += mode.getHeight();

			if (maxWidth < mode.getWidth()) {
				maxWidth = mode.getWidth();
			}

			if (maxHeight < mode.getHeight()) {
				maxHeight = mode.getHeight();
			}
		}
	}

	public boolean deviceExist(String deviceId) {
		
		return listDeviceId.contains(deviceId);
	}
	
	/**
	 * @return the totalWidth
	 */
	public int getTotalWidth() {
		return totalWidth;
	}

	/**
	 * @param totalWidth the totalWidth to set
	 */
	public void setTotalWidth(int totalWidth) {
		this.totalWidth = totalWidth;
	}

	/**
	 * @return the totalHeight
	 */
	public int getTotalHeight() {
		return totalHeight;
	}

	/**
	 * @param totalHeight the totalHeight to set
	 */
	public void setTotalHeight(int totalHeight) {
		this.totalHeight = totalHeight;
	}

	/**
	 * @return the numOfDevices
	 */
	public int getNumOfDevices() {
		return numOfDevices;
	}

	/**
	 * @param numOfDevices the numOfDevices to set
	 */
	public void setNumOfDevices(int numOfDevices) {
		this.numOfDevices = numOfDevices;
	}

	/**
	 * @return the maxWidth
	 */
	public int getMaxWidth() {
		return maxWidth;
	}

	/**
	 * @param maxWidth the maxWidth to set
	 */
	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	/**
	 * @return the maxHeight
	 */
	public int getMaxHeight() {
		return maxHeight;
	}

	/**
	 * @param maxHeight the maxHeight to set
	 */
	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	/**
	 * @return the listDeviceId
	 */
	public ArrayList<String> getListDeviceId() {
		return listDeviceId;
	}

	/**
	 * @param listDeviceId the listDeviceId to set
	 */
	public void setListDeviceId(ArrayList<String> listDeviceId) {
		this.listDeviceId = listDeviceId;
	}

}
