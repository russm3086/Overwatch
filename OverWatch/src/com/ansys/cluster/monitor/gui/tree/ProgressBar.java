/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

/**
 * @author rmartine
 *
 */
public class ProgressBar extends JPanel {
	private Color backGroundColor = Color.WHITE;
	private String labelText;
	private Dimension pnlPrefferedSize = new Dimension(285, 50);
	private Dimension lblPrefferedSize = new Dimension(85, 30);
	private Dimension prgPrefferedSize = new Dimension(180, 40);
	private int availableUnits = 0;
	private int unavailableUnits = 0;
	private int totalUnits = 0;
	private String nameUnit = new String();
	private Color barBackGround = Color.GREEN;
	private Color barForeGround = Color.RED;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2920680479135410636L;

	/**
	 * @param backGroundColor
	 * @param labelText
	 * @param prefferedSize
	 * @param availableUnits
	 * @param unavailableUnits
	 * @param totalUnits
	 * @param nameUnit
	 * @param toolTip
	 * @param barBackGround
	 * @param barForeGround
	 * @param border
	 */
	public ProgressBar(Color backGroundColor, String labelText, Dimension pnlPrefferedSize, Dimension lblPrefferedSize,
			Dimension prgPrefferedSize, int availableUnits, int unavailableUnits, int totalUnits, String nameUnit,
			String toolTip, Color barBackGround, Color barForeGround, Border border) {
		super();
		setBackGroundColor(backGroundColor);
		this.labelText = labelText;
		setPnlPrefferedSize(pnlPrefferedSize);
		setLblPrefferedSize(lblPrefferedSize);
		setPrgPrefferedSize(prgPrefferedSize);

		this.availableUnits = availableUnits;
		this.unavailableUnits = unavailableUnits;
		this.totalUnits = totalUnits;
		this.nameUnit = nameUnit;
		setToolTipText(toolTip);
		setBarBackGround(barBackGround);
		setBarForeGround(barForeGround);
		setBorder(border);
	}

	public ProgressBar(String labelText, int availableUnits, int unavailableUnits, int totalUnits, String nameUnit,
			String toolTip) {

		this(null, labelText, null, null, null, availableUnits, unavailableUnits, totalUnits, nameUnit, toolTip, null,
				null, null);

		build();
	}

	public void build() {

		setBackground(getBackGroundColor());

		setPreferredSize(getPnlPrefferedSize());

		JLabel lblNewLabel = new JLabel(getLabelText());
		lblNewLabel.setPreferredSize(getLblPrefferedSize());

		add(lblNewLabel);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setPreferredSize(getPrgPrefferedSize());

		progressBar.setBackground(getBarBackGround());
		progressBar.setForeground(getBarForeGround());
		progressBar.setMaximum(getTotalUnits());
		progressBar.setValue(getUnavailableUnits());

		progressBar.setString(getAvailableUnits() + " of " + getTotalUnits() + " " + getNameUnit());
		progressBar.setStringPainted(true);
		add(progressBar);

	}

	/**
	 * @return the backGroundColor
	 */
	public Color getBackGroundColor() {
		return backGroundColor;
	}

	/**
	 * @param backGroundColor the backGroundColor to set
	 */
	public void setBackGroundColor(Color backGroundColor) {
		if (backGroundColor != null)
			this.backGroundColor = backGroundColor;
	}

	/**
	 * @return the labelText
	 */
	public String getLabelText() {
		return labelText;
	}

	/**
	 * @param labelText the labelText to set
	 */
	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	/**
	 * @return the prefferedSize
	 */
	public Dimension getPnlPrefferedSize() {
		return pnlPrefferedSize;
	}

	/**
	 * @param prefferedSize the prefferedSize to set
	 */
	public void setPnlPrefferedSize(Dimension pnlPrefferedSize) {
		if (pnlPrefferedSize != null)
			this.pnlPrefferedSize = pnlPrefferedSize;
	}

	/**
	 * @return the lblPrefferedSize
	 */
	public Dimension getLblPrefferedSize() {
		return lblPrefferedSize;
	}

	/**
	 * @param lblPrefferedSize the lblPrefferedSize to set
	 */
	public void setLblPrefferedSize(Dimension lblPrefferedSize) {
		if (lblPrefferedSize != null)
			this.lblPrefferedSize = lblPrefferedSize;
	}

	/**
	 * @return the prgPrefferedSize
	 */
	public Dimension getPrgPrefferedSize() {
		return prgPrefferedSize;
	}

	/**
	 * @param prgPrefferedSize the prgPrefferedSize to set
	 */
	public void setPrgPrefferedSize(Dimension prgPrefferedSize) {
		if (prgPrefferedSize != null)
			this.prgPrefferedSize = prgPrefferedSize;
	}

	/**
	 * @return the availableUnits
	 */
	public int getAvailableUnits() {
		return availableUnits;
	}

	/**
	 * @param availableUnits the availableUnits to set
	 */
	public void setAvailableUnits(int availableUnits) {
		this.availableUnits = availableUnits;
	}

	/**
	 * @return the unavailableUnits
	 */
	public int getUnavailableUnits() {
		return unavailableUnits;
	}

	/**
	 * @param unavailableUnits the unavailableUnits to set
	 */
	public void setUnavailableUnits(int unavailableUnits) {
		this.unavailableUnits = unavailableUnits;
	}

	/**
	 * @return the totalUnits
	 */
	public int getTotalUnits() {
		return totalUnits;
	}

	/**
	 * @param totalUnits the totalUnits to set
	 */
	public void setTotalUnits(int totalUnits) {
		this.totalUnits = totalUnits;
	}

	/**
	 * @return the nameUnit
	 */
	public String getNameUnit() {
		return nameUnit;
	}

	/**
	 * @param nameUnit the nameUnit to set
	 */
	public void setNameUnit(String nameUnit) {
		this.nameUnit = nameUnit;
	}

	/**
	 * @return the barBackGround
	 */
	public Color getBarBackGround() {
		return barBackGround;
	}

	/**
	 * @param barBackGround the barBackGround to set
	 */
	public void setBarBackGround(Color barBackGround) {

		if (barBackGround != null)
			this.barBackGround = barBackGround;
	}

	/**
	 * @return the barFoerGround
	 */
	public Color getBarForeGround() {
		return barForeGround;
	}

	/**
	 * @param barForGround the barForeGround to set
	 */
	public void setBarForeGround(Color barForeGround) {
		if (barForeGround != null)
			this.barForeGround = barForeGround;
	}

}
