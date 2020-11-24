/**
 * 
 */
package com.russ.util.gui;

import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import com.ansys.cluster.monitor.main.Main;

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
	private static String sourceClass = Main.class.getName();
	private static Logger logger = Logger.getLogger(sourceClass);

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

	public static void fontScaling(double scaleAdj, float upperLimit, float lowerLimit) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

		if (scaleAdj < lowerLimit) {

			scaleAdj = lowerLimit;
		} else if (scaleAdj > upperLimit) {

			scaleAdj = upperLimit;
		}

		fontScaling(scaleAdj);
	}

	public static void fontScaling(double scaleAdj) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		float adjustment = (float) (.001f * scaleAdj);
		float scale = 1.f + adjustment;

		logger.finer("The font scaling set to " + scale);

		UIManager.LookAndFeelInfo looks[] = UIManager.getInstalledLookAndFeels();

		for (UIManager.LookAndFeelInfo info : looks) {

			UIManager.setLookAndFeel(info.getClassName());

			UIDefaults defaults = UIManager.getDefaults();
			Enumeration<?> newKeys = defaults.keys();

			while (newKeys.hasMoreElements()) {
				Object obj = newKeys.nextElement();
				Object current = UIManager.get(obj);
				if (current instanceof FontUIResource) {
					FontUIResource resource = (FontUIResource) current;
					defaults.put(obj, new FontUIResource(resource.deriveFont(resource.getSize2D() * scale)));
					logger.finest(String.format("%50s : %s\n", obj, UIManager.get(obj)));

				} else if (current instanceof Font) {
					Font resource = (Font) current;
					defaults.put(obj, resource.deriveFont(resource.getSize2D() * scale));
					logger.finest(String.format("%50s : %s\n", obj, UIManager.get(obj)));
				}
			}
		}

	}

}
