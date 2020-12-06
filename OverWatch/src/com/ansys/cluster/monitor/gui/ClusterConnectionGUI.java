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

import org.apache.commons.configuration2.ex.ConfigurationException;

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
public class ClusterConnectionGUI extends JDialog {
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
	 * The combination box that allows for the setting of local or remote connection
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
	 * The textbox the URL for the full job detail data
	 */
	private JTextField jobsFullDetailUrlTxt;

	/**
	 * The textbox the URL for the queue summary data
	 */
	private JTextField queueUrlTxt;

	/**
	 * The checkbox to enable the use of the full detailed job URL
	 */
	private JCheckBox jobFullDetailUseChkBox;

	/**
	 * The spinner that contains the value in which the database is refreshed
	 */
	private JSpinner refreshSpin;

	private Console console;

	/**
	 * The textbox that contains the name of the quota url feed
	 */
	private JTextField quotaUrlTxt;

	/**
	 * Creates a modal or non-modal dialog with the specified title and the
	 * specified owner frame.
	 */

	private int index;

	public ClusterConnectionGUI(Frame owner, String title, boolean modal, SGE_MonitorProp mainProps) {
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

		// this.setSize(420, 350);
		this.setSize(600, 338);

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

		JLabel jobsUseFullDetailUrlLbl = new JLabel("Use Job Full Detail data: ");
		jobsUseFullDetailUrlLbl.setDisplayedMnemonic('U');
		jobFullDetailUseChkBox = new JCheckBox();
		jobsUseFullDetailUrlLbl.setLabelFor(jobFullDetailUseChkBox);
		jobFullDetailUseChkBox.setToolTipText("To use URL for the full detail jobs data.");

		JLabel refreshLabel = new JLabel("Refresh Rate (1 min. min): ");
		refreshLabel.setDisplayedMnemonic('R');

		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 900, 1);
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

		// check box
		c.gridy = 1;
		c.gridx = 1;
		c.ipadx = 0;
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(jobsUseFullDetailUrlLbl, c);
		this.getContentPane().add(jobsUseFullDetailUrlLbl);

		c.gridx = 2;
		c.anchor = GridBagConstraints.WEST;
		gridBag.setConstraints(jobFullDetailUseChkBox, c);
		this.getContentPane().add(jobFullDetailUseChkBox);

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

		JLabel jobsFullDetailUrlLbl = new JLabel("Jobs Full Detail URL: ");
		jobsFullDetailUrlLbl.setDisplayedMnemonic('F');
		jobsFullDetailUrlTxt = new JTextField(25);
		jobsFullDetailUrlTxt.setEditable(false);
		jobsFullDetailUrlLbl.setLabelFor(jobsFullDetailUrlTxt);
		jobsFullDetailUrlTxt.setToolTipText("The URL for the full detail jobs data.");

		JLabel queueUrlLbl = new JLabel("Queue URL: ");
		queueUrlLbl.setDisplayedMnemonic('Q');
		queueUrlTxt = new JTextField(25);
		queueUrlTxt.setEditable(false);
		queueUrlLbl.setLabelFor(queueUrlTxt);
		queueUrlTxt.setToolTipText("The URL for the queue data.");

		JLabel quotaUrlLbl = new JLabel("Quota URL: ");
		quotaUrlLbl.setDisplayedMnemonic('o');
		quotaUrlTxt = new JTextField(25);
		quotaUrlTxt.setEditable(false);
		quotaUrlLbl.setLabelFor(quotaUrlTxt);
		quotaUrlTxt.setToolTipText("The URL for the quota data.");

		// c.gridx = 0;
		// c.gridy = 6;
		// c.anchor = GridBagConstraints.WEST;

		// Host Url settings
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

		// Jobs Url settings
		c.gridy = 8;
		gridBag.setConstraints(jobsUrlTxt, c);
		this.getContentPane().add(jobsUrlTxt);

		c.gridx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(jobsUrlLbl, c);
		this.getContentPane().add(jobsUrlLbl);

		// JobsDetail Url settings
		c.gridx = 0;
		c.gridy = 9;
		gridBag.setConstraints(jobsDetailUrlLbl, c);
		this.getContentPane().add(jobsDetailUrlLbl);

		c.gridx = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		gridBag.setConstraints(jobsDetailUrlTxt, c);
		this.getContentPane().add(jobsDetailUrlTxt);

		// JobsFullDetail Url settings
		c.gridx = 0;
		c.gridy = 10;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		gridBag.setConstraints(jobsFullDetailUrlLbl, c);
		this.getContentPane().add(jobsFullDetailUrlLbl);

		c.gridx = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		gridBag.setConstraints(jobsFullDetailUrlTxt, c);
		this.getContentPane().add(jobsFullDetailUrlTxt);

		// Queue Url settings
		c.gridx = 0;
		c.gridy = 11;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		gridBag.setConstraints(queueUrlLbl, c);
		this.getContentPane().add(queueUrlLbl);

		c.gridx = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		gridBag.setConstraints(queueUrlTxt, c);
		this.getContentPane().add(queueUrlTxt);

		// Quota Url settings
		c.gridx = 0;
		c.gridy = 12;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		gridBag.setConstraints(quotaUrlLbl, c);
		this.getContentPane().add(quotaUrlLbl);

		c.gridx = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		gridBag.setConstraints(quotaUrlTxt, c);
		this.getContentPane().add(quotaUrlTxt);

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
		c.gridy = 13;
		c.ipady = 11;
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

				index = comboConn.getSelectedIndex();
				hostUrlTxt.setText(mainProps.getClusterConnectionHostUrl(index));
				jobsUrlTxt.setCaretPosition(0);

				jobsUrlTxt.setText(mainProps.getClusterConnectionSummaryJobsUrl(index));
				jobsUrlTxt.setCaretPosition(0);

				jobsDetailUrlTxt.setText(mainProps.getClusterConnectionDetailedJobsUrl(index));
				jobsDetailUrlTxt.setCaretPosition(0);

				jobsFullDetailUrlTxt.setText(mainProps.getClusterConnectionFullDetailedJobsUrl(index));
				jobsFullDetailUrlTxt.setCaretPosition(0);
				
				queueUrlTxt.setText(mainProps.getClusterConnectionQueueUrl(index));
				queueUrlTxt.setCaretPosition(0);

				jobFullDetailUseChkBox.setSelected(mainProps.getClusterUseFullDetailedJobs(index));

				quotaUrlTxt.setText(mainProps.getClusterConnectionQuotaUrl(index));
				quotaUrlTxt.setCaretPosition(0);
			}
		});

		/**
		 * The ActionListrner for the OK Button It saves the data and exits the gui
		 */
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				saveData();
				console.populateTree();
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

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		pack();

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
		jobFullDetailUseChkBox.setSelected(mainProps.getClusterUseFullDetailedJobs(index));

	}

	/**
	 * Populates the properties file with data from all textfields in the UI.
	 * 
	 * @throws ConfigurationException
	 */
	private void saveData() {

		mainProps.setClusterIndex(comboConn.getSelectedIndex());
		mainProps.setGuiTimerDelay((int) refreshSpin.getValue());
		mainProps.setClusterUseFullDetailedJobs(index, jobFullDetailUseChkBox.isSelected());

		try {
			Main.saveSettings();
		} catch (IOException | URISyntaxException | ConfigurationException e) {
			logger.log(Level.WARNING, "Cannot save settings", e);
		}

	}
}
