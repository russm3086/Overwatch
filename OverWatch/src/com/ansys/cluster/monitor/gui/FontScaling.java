/**
 * 
 */
package com.ansys.cluster.monitor.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;

import com.ansys.cluster.monitor.main.SGE_DataConst;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.test.Test;

/**
 * @author rmartine
 *
 */
public class FontScaling extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3547864335601879189L;

	/**
	 * The name of current class. To be used with the logging subsystem.
	 */
	private final String className = this.getClass().getName();

	/**
	 * The Logger instance. All log messages from this class are handle here.
	 */
	private Logger logger = Logger.getLogger(className);

	/**
	 * The layout manager for the dialog
	 */
	private GridBagLayout gridBag = new GridBagLayout();

	/**
	 * The constraints for the dialog
	 */
	private GridBagConstraints c = new GridBagConstraints();

	/**
	 * The properties object for settings pertaining to the console
	 */
	private SGE_MonitorProp mainProps = null;

	private Test console;

	/**
	 * 
	 */
	public FontScaling(Frame owner, String title, boolean modal, SGE_MonitorProp mainProps) {
		super(owner, title, modal);
		this.mainProps = mainProps;

		this.console = (Test) owner;

		// Sets layout manager
		getContentPane().setLayout(gridBag);

		// Sets the operation that will happen when the user initiates a "close"
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Turn off the ability to resize the window
		this.setResizable(true);

		make();

		this.setSize(420, 320);
	}

	public void make() {

		JLabel connLabel = new JLabel("Font Scaling: ");

		int scaling = (int) mainProps.getGuiFontScaling();

		int scalingMaxValue = (int) SGE_DataConst.app_font_max_scaling;

		int scalingMinValue = -1 * (int) SGE_DataConst.app_font_max_scaling;

		JLabel refreshLabel = new JLabel("Scaling range " + scalingMinValue + " to " + scalingMaxValue);

		JSlider slider = new JSlider(JSlider.HORIZONTAL, scalingMinValue, scalingMaxValue, scaling);

		//refreshLabel.setLabelFor(slider);


		c.gridy = 1;
		c.gridx = 0;
		c.ipadx = 0;
		this.getContentPane().add(refreshLabel);

		c.gridy = 1;
		c.gridx = 1;
		c.ipadx = 0;
		this.getContentPane().add(slider);

		slider.setMinorTickSpacing(5);
		slider.setMajorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

	}

}
