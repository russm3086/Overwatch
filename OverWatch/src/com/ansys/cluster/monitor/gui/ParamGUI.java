/**
 * <p>Title: URLyBird Database</p>
 * <p>Description: Hotel Room Booking System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: URLyBird</p>
 * @author Russ Martinez
 * @version 1.0
 */

package com.ansys.cluster.monitor.gui;

import javax.swing.*;

import com.ansys.cluster.monitor.main.Main;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

import java.io.*;
import java.net.URISyntaxException;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;

/**
 * The <code>ParamGUI</code> class provides a graphical user interface to set
 * application settings
 */
public class ParamGUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5788291111618875194L;

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

	/**
	 * The combonation box that allows for the setting of local or remote connection
	 * to the database
	 */
	private JComboBox<String> comboConn;

	/**
	 * The textbox that contains the name of the RMI server host name
	 */
	private JTextField hostUrlTxt;

	/**
	 * The textbox that contains the name in which the RMI session is bound to.
	 */
	private JTextField jobsUrlTxt;

	/**
	 * The textbox that contains the port number in which the RMI server is listen
	 * to
	 */
	private JTextField jobsDetailUrlTxt;

	/**
	 * The spinner that contains the value in which the database is refreshed
	 */
	private JSpinner refreshSpin;

	/**
	 * Creates a modal or non-modal dialog with the specified title and the
	 * specified owner frame.
	 */
	public ParamGUI(Frame owner, String title, boolean modal, SGE_MonitorProp mainProps) {
		super(owner, title, modal);
		this.mainProps = mainProps;

		// Sets layout manager
		getContentPane().setLayout(gridBag);

		// Sets the operation that will happen when the user initiates a "close"
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Turn off the ability to resize the window
		this.setResizable(true);

		make();

		this.setSize(420, 320);

	}

	/**
	 * Builds and displays the settings window by building the console, database,
	 * and remote selections.
	 */
	public void make() {
		JLabel connLabel = new JLabel("Cluster: ");
		connLabel.setDisplayedMnemonic('n');

		ConnectionComboModel comboModel = new ConnectionComboModel(mainProps);
		comboConn = new JComboBox<String>(comboModel);
		comboConn.setToolTipText("Select the type of db connection");
		connLabel.setLabelFor(comboConn);

		JLabel refreshLabel = new JLabel("Refresh Rate (5 min. min): ");
		refreshLabel.setDisplayedMnemonic('R');

		SpinnerNumberModel model = new SpinnerNumberModel(5, 5, 900, 5);
		refreshSpin = new JSpinner(model);
		refreshLabel.setLabelFor(refreshSpin);
		refreshSpin.setToolTipText("Interval between db refresh in sec");

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = .5;
		c.weighty = .5;
		c.anchor = GridBagConstraints.WEST;

		c.gridy = 1;
		c.gridx = 0;
		c.ipadx = 0;
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(connLabel, c);
		this.getContentPane().add(connLabel);

		c.gridx = 1;
		c.anchor = GridBagConstraints.WEST;
		gridBag.setConstraints(comboConn, c);
		this.getContentPane().add(comboConn);

		c.gridy = 2;
		c.gridx = 0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(refreshLabel, c);
		this.getContentPane().add(refreshLabel);

		c.gridx = 2;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		gridBag.setConstraints(refreshSpin, c);
		this.getContentPane().add(refreshSpin);

		c.gridy = 3;
		c.gridx = 0;
		c.anchor = GridBagConstraints.WEST;

		c.gridy = 4;
		c.gridx = 0;
		c.ipady = 0;
		c.anchor = GridBagConstraints.EAST;

		c.gridx = 1;
		c.weightx = .5;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;

		c.gridx = 2;
		c.gridy = 5;
		c.weightx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;

		// Remote Section
		JLabel hostUrlLbl = new JLabel("Hosts URL: ");
		hostUrlLbl.setDisplayedMnemonic('H');
		hostUrlTxt = new JTextField(25);
		hostUrlTxt.setEditable(false);
		hostUrlLbl.setLabelFor(hostUrlTxt);
		hostUrlTxt.setToolTipText("The URL for the hosts data.");

		JLabel jobsUrlLbl = new JLabel("Jobs URL: ");
		jobsUrlLbl.setDisplayedMnemonic('J');
		jobsUrlTxt = new JTextField(25);
		jobsUrlTxt.setEditable(false);
		jobsUrlLbl.setLabelFor(jobsUrlTxt);
		jobsUrlTxt.setToolTipText("The URL for the summary jobs data.");

		JLabel jobsDetailUrlLbl = new JLabel("Jobs Detail URL: ");
		jobsDetailUrlLbl.setDisplayedMnemonic('D');
		jobsDetailUrlTxt = new JTextField(25);
		jobsDetailUrlTxt.setEditable(false);
		jobsDetailUrlLbl.setLabelFor(jobsDetailUrlTxt);
		jobsDetailUrlTxt.setToolTipText("The URL for the detail jobs data.");

		c.gridx = 0;
		c.gridy = 6;
		c.anchor = GridBagConstraints.WEST;

		c.gridy = 7;
		c.gridx = 0;
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(hostUrlLbl, c);
		this.getContentPane().add(hostUrlLbl);

		c.gridx = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		gridBag.setConstraints(hostUrlTxt, c);
		this.getContentPane().add(hostUrlTxt);

		c.gridy = 8;
		gridBag.setConstraints(jobsUrlTxt, c);
		this.getContentPane().add(jobsUrlTxt);

		c.gridx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(jobsUrlLbl, c);
		this.getContentPane().add(jobsUrlLbl);

		c.gridx = 0;
		c.gridy = 9;
		gridBag.setConstraints(jobsDetailUrlLbl, c);
		this.getContentPane().add(jobsDetailUrlLbl);

		c.gridx = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		gridBag.setConstraints(jobsDetailUrlTxt, c);
		this.getContentPane().add(jobsDetailUrlTxt);

		// Remote Buttons
		JButton okButton = new JButton("OK");
		JButton applyButton = new JButton("Apply");
		JButton cancelButton = new JButton("Cancel");

		okButton.setMnemonic(KeyEvent.VK_O);
		applyButton.setMnemonic(KeyEvent.VK_A);
		cancelButton.setMnemonic(KeyEvent.VK_C);

		okButton.setToolTipText("Saves the settings and closes the dialog");
		applyButton.setToolTipText("Saves the settings");
		cancelButton.setToolTipText("Closes the dialog without saving any" + " settings");

		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 11;
		c.ipady = 10;
		c.insets = new Insets(10, 0, 0, 0);
		c.anchor = GridBagConstraints.CENTER;
		gridBag.setConstraints(okButton, c);
		this.getContentPane().add(okButton);

		c.gridx = 1;
		c.insets = new Insets(10, 0, 0, 30);
		gridBag.setConstraints(applyButton, c);
		this.getContentPane().add(applyButton);

		c.gridx = 2;
		c.insets = new Insets(10, 0, 0, 20);
		gridBag.setConstraints(cancelButton, c);
		this.getContentPane().add(cancelButton);

		comboConn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				int index = comboConn.getSelectedIndex();
				hostUrlTxt.setText(mainProps.getClusterConnectionHostUrl(index));
				jobsUrlTxt.setCaretPosition(0);

				jobsUrlTxt.setText(mainProps.getClusterConnectionSummaryJobsUrl(index));
				jobsUrlTxt.setCaretPosition(0);

				jobsDetailUrlTxt.setText(mainProps.getClusterConnectionDetailedJobsUrl(index));
				jobsDetailUrlTxt.setCaretPosition(0);

			}
		});

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
		 * The ActionListrner for the Apply Button It saves the data and refreshes the
		 * gui
		 */
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				saveData();
				loadData();
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

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		loadData();
	}

	private void exit() {
		this.dispose();
	}

	/**
	 * Populates all textfield with data from the properties file
	 */
	private void loadData() {

		comboConn.setSelectedIndex(mainProps.getClusterIndex());
		refreshSpin.setValue(mainProps.getGuiTimerDelay());

	}

	/**
	 * Populates the properties file with data from all textfields in the UI.
	 */
	private void saveData() {

		mainProps.setClusterIndex(comboConn.getSelectedIndex());
		mainProps.setGuiTimerDelay((int) refreshSpin.getValue());

		try {
			Main.saveSettings(mainProps);
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.log(Level.WARNING, "Cannot save settings", e);
		}

	}
}
