/**
 * 
 */
package com.ansys.cluster.monitor.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * @author rmartine
 *
 */
public class AliasGUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7766598289194698167L;

	/**
	 * The name of current class. To be used with the logging subsystem.
	 */
	private final String sourceClass = this.getClass().getName();

	/**
	 * The Logger instance. All log messages from this class are handle here.
	 */
	private Logger logger = Logger.getLogger(sourceClass);

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

	private Console console;
	private JTextField selectedField;

	public AliasGUI(Frame owner, String title, boolean modal, SGE_MonitorProp mainProps) {
		super(owner, title, modal);
		this.mainProps = mainProps;
		this.console = (Console) owner;

		// Sets layout manager
		getContentPane().setLayout(gridBag);

		// Sets the operation that will happen when the user initiates a "close"
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Turn off the ability to resize the window
		this.setResizable(true);

		make();

		// selectedField.setText(mainProps.getUsernameAlias());

		// this.setSize(622, 350);
		this.setPreferredSize(new Dimension(300, 169));
		// this.setSize(300, 169);

	}

	public void make() {
		JLabel selectedLabel = new JLabel("Alias Username:");
		selectedField = new JTextField(8);

		// Remote Buttons
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		JButton clearButton = new JButton("Clear");

		okButton.setMnemonic(KeyEvent.VK_O);
		cancelButton.setMnemonic(KeyEvent.VK_C);
		clearButton.setMnemonic(KeyEvent.VK_L);

		okButton.setToolTipText("Saves the settings and closes the dialog");
		cancelButton.setToolTipText("Closes the dialog without saving any" + " settings");
		clearButton.setToolTipText("Clears the text field");

		c.weightx = .5;
		c.weighty = .5;

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(10, 10, 10, 10);
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(selectedLabel, c);
		this.getContentPane().add(selectedLabel);

		c.gridx = 1;
		c.anchor = GridBagConstraints.WEST;
		gridBag.setConstraints(selectedField, c);
		this.getContentPane().add(selectedField);

		c.gridy = 3;
		c.gridx = 0;
		c.anchor = GridBagConstraints.WEST;
		gridBag.setConstraints(okButton, c);
		this.getContentPane().add(okButton);

		c.gridx = 1;
		c.anchor = GridBagConstraints.CENTER;
		gridBag.setConstraints(clearButton, c);
		this.getContentPane().add(clearButton);

		c.gridx = 2;
		c.anchor = GridBagConstraints.EAST;
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

		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				selectedField.setText("");
			}
		});

		pack();

	}

	private void exit() {
		this.dispose();
	}

	private void saveData() {
		logger.entering(sourceClass, "saveData");

		logger.finer("Setting user alias");
		mainProps.setUsernameAlias(selectedField.getText());

		logger.finer("Refreshing Tree");
		console.populateTree();
		logger.exiting(sourceClass, "saveData");
	}

}
