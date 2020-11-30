/**
 * 
 */
package com.ansys.cluster.monitor.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ansys.cluster.monitor.main.SGE_DataConst;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.OvalBorder;

/**
 * @author rmartine
 *
 */
public class FontScalingGui extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7766598289194698167L;

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

	// private Console console;
	private Frame console;
	private JTextField selectedField;
	private JLabel testLabel;
	private float defaultFontSize;
	private JSlider slider;

	public FontScalingGui(Frame owner, String title, boolean modal, SGE_MonitorProp mainProps) {
		super(owner, title, modal);
		this.mainProps = mainProps;
		this.console = (Console) owner;

		setTitle(title + "        (RESTART OF APPLICATION REQUIRED)");

		// Sets layout manager
		getContentPane().setLayout(gridBag);

		// Sets the operation that will happen when the user initiates a "close"
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Turn off the ability to resize the window
		this.setResizable(true);

		make();

		// this.setSize(622, 350);
		this.setSize(530, 298);

		// this.setLocation(xLocation, yLocation);

	}

	public void make() {
		int scaling = (int) mainProps.getGuiFontScaling();
		int scalingMaxValue = (int) SGE_DataConst.app_font_max_scaling;
		int scalingMinValue = -1 * (int) SGE_DataConst.app_font_max_scaling;
		defaultFontSize = UIManager.getFont("TextField.font").getSize2D();

		JLabel refreshLabel = new JLabel("Scaling range " + scalingMinValue + " to " + scalingMaxValue);
		refreshLabel.setDisplayedMnemonic('S');

		slider = new JSlider(JSlider.HORIZONTAL, scalingMinValue, scalingMaxValue, scaling);
		slider.addChangeListener(new SliderChange());
		slider.setMinorTickSpacing(5);
		slider.setMajorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setValue(scaling);
		Dimension d = slider.getPreferredSize();
		slider.setPreferredSize(new Dimension(300, d.height));
		refreshLabel.setLabelFor(slider);

		testLabel = new JLabel("Supercalifragilisticexpialidocious");
		testLabel.setFont(testLabel.getFont().deriveFont(defaultFontSize));
		Color lightBlue = new Color(0, 0, 182, 155);
		Border ovalBorder = new OvalBorder(10, 10, lightBlue, Color.BLUE);
		testLabel.setBorder(ovalBorder);

		JLabel selectedLabel = new JLabel("Selected Value:");
		selectedField = new JTextField(3);
		selectedField.setText(String.valueOf(scaling));
		selectedField.addKeyListener(new TextFieldKeyListener());
		changeFont(slider);

		// Remote Buttons
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");

		okButton.setMnemonic(KeyEvent.VK_O);
		cancelButton.setMnemonic(KeyEvent.VK_C);

		okButton.setToolTipText("Saves the settings and closes the dialog");
		cancelButton.setToolTipText("Closes the dialog without saving any" + " settings");

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = .5;
		c.weighty = .5;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.CENTER;

		gridBag.setConstraints(refreshLabel, c);
		this.getContentPane().add(refreshLabel);

		c.gridy = 1;
		c.weightx = 1.5;
		c.gridwidth = 5;
		gridBag.setConstraints(slider, c);
		this.getContentPane().add(slider);

		c.gridy = 3;
		c.weightx = .5;
		c.insets = new Insets(10, 10, 10, 10);
		gridBag.setConstraints(testLabel, c);
		this.getContentPane().add(testLabel);

		c.insets = new Insets(0, 0, 0, 0);
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridy = 6;
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(selectedLabel, c);
		this.getContentPane().add(selectedLabel);

		c.gridx = 1;
		c.anchor = GridBagConstraints.WEST;
		gridBag.setConstraints(selectedField, c);
		this.getContentPane().add(selectedField);

		c.gridx = 0;
		c.gridy = 7;
		c.insets = new Insets(10, 0, 0, 0);
		c.anchor = GridBagConstraints.CENTER;
		gridBag.setConstraints(okButton, c);
		this.getContentPane().add(okButton);

		c.gridx = 1;
		c.insets = new Insets(10, 0, 0, 20);
		c.anchor = GridBagConstraints.CENTER;
		gridBag.setConstraints(cancelButton, c);
		this.getContentPane().add(cancelButton);

		/**
		 * The ActionListrner for the OK Button It saves the data and exits the gui
		 */
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				saveData();
				exit();
			}
		});

		/**
		 * The ActionListener for the Cancel Button It saves the data and refreshes the
		 * gui
		 */
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				exit();
			}
		});

		pack();

	}

	private void exit() {
		this.dispose();
	}

	private void saveData() {

		mainProps.setGuiFontScaling(slider.getValue());
		console.repaint();
	}

	private void changeFont(JSlider slider) {

		selectedField.setText(String.valueOf(slider.getValue()));

		float scaleValue = (float) (1 + (slider.getValue() * .01));

		float newSize = (float) (defaultFontSize * scaleValue);

		Font font = testLabel.getFont().deriveFont(newSize);
		testLabel.setFont(font);

		logger.finest("Slider value: " + slider.getValue());
		logger.finest("Scale value: " + scaleValue);
		logger.finest("Font soze value: " + font.getSize2D());

	}

	private class SliderChange implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {

			JSlider slider = (JSlider) e.getSource();
			changeFont(slider);
		}
	}

	private class TextFieldKeyListener extends KeyAdapter {

		public void keyReleased(KeyEvent ke) {

			JTextField textField = (JTextField) ke.getSource();
			String typed = textField.getText();

			if (!typed.matches("\\d+")) {
				return;
			}

			int value = Integer.parseInt(typed);
			slider.setValue(value);

		}

	}

}
