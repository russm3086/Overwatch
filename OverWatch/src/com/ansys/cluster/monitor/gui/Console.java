/**
  */

package com.ansys.cluster.monitor.gui;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.json.JSONException;

import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeInterface;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.gui.tree.ClusterTreeCellRenderer;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoFactory;
import com.ansys.cluster.monitor.gui.tree.TreeBuilder;
import com.ansys.cluster.monitor.main.Main;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.concurrent.QueuableWorker;
import com.russ.util.concurrent.WorkerManager;
import com.russ.util.gui.DisplayTool;
import com.russ.util.nio.ResourceLoader;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EventObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

/**
 * The <code>Console</code> class provides the main graphical interface for the
 * database
 */
public class Console extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -985281784855593074L;
	private Timer timer;
	private JTree tree;
	private JEditorPane editorPane;
	private JPanel statusBar;
	protected SGE_MonitorProp mainProps;

	private static JLabel statusLabel;
	WorkerManager connectionManager;
	ExecutorService executor;

	/**
	 * The name of current class. To be used with the logging subsystem.
	 */
	private final String sourceClass = this.getClass().getName();

	/**
	 * The Logger instance. All log messages from this class are handle here.
	 */
	private Logger logger = Logger.getLogger(sourceClass);
	JScrollPane scrollPane;

	/**
	 * Constructs the UI's appearance
	 * 
	 * @throws IOException
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	public Console(String title, SGE_MonitorProp mainProps) throws IOException, InterruptedException {
		super(title);

		executor = Executors.newFixedThreadPool(1);
		connectionManager = new WorkerManager(3, executor);

		this.mainProps = mainProps;
		this.setDefaultCloseOperation(Console.EXIT_ON_CLOSE);
		this.addWindowListener(new SaveSettings());
		SetupKeyBinding();

		ImageIcon img = new ImageIcon(ResourceLoader.load(GUI_Const.Icon_Ansys_Overwatch));
		setIconImage(img.getImage());

		// Setting up Menu Bar
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setToolTipText("Connects to Cluster, Changes Settings, and " + "Quits Program");

		JMenu settingsMenu = new JMenu("Settings");

		// Connect to Db MenuItem

		JMenuItem connectMenuItem = new JMenuItem("Connect to Cluster");
		connectMenuItem.addActionListener(new ConnectCluster());
		connectMenuItem.setMnemonic(KeyEvent.VK_C);
		connectMenuItem.setToolTipText("Connects to the CLuster");
		fileMenu.add(connectMenuItem);

		// Settings MenuItem

		JMenuItem settingsMenuItem = new JMenuItem("Cluster Settings");
		settingsMenuItem.addActionListener(new ClusterConnectionSettings(settingsMenuItem.getText()));
		settingsMenuItem.setMnemonic(KeyEvent.VK_C);
		settingsMenuItem.setToolTipText("Allow cluster settings to be Change and Saved");
		settingsMenu.add(settingsMenuItem);

		JMenuItem fontMenuItem = new JMenuItem("Font Scale Settings");
		fontMenuItem.addActionListener(new FontScalingSettings(fontMenuItem.getText()));
		fontMenuItem.setMnemonic(KeyEvent.VK_F);
		fontMenuItem.setToolTipText("Allow font scaling to be Change and Saved");
		settingsMenu.add(fontMenuItem);

		// Quit MenuItem
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(new Exit());
		quitMenuItem.setMnemonic(KeyEvent.VK_Q);
		quitMenuItem.setToolTipText("Quits Program");
		fileMenu.add(quitMenuItem);

		// Setting up MenuBar
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);

		settingsMenu.setMnemonic(KeyEvent.VK_S);
		menuBar.add(settingsMenu);

		createAdmin(menuBar);
		this.setJMenuBar(menuBar);

		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);

		tree = new JTree(new DefaultMutableTreeNode("Cluster(s)"));
		tree.setMinimumSize(new Dimension(300, 200));
		tree.setVisibleRowCount(20);

		ClusterTreeCellRenderer renderer = new ClusterTreeCellRenderer();

		tree.setCellRenderer(renderer);
		tree.addTreeSelectionListener(new NodeSelection());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.putClientProperty("JTree.lineStyle", "Angled");
		tree.setRowHeight(0);

		ToolTipManager.sharedInstance().registerComponent(tree);

		JScrollPane treeView = new JScrollPane(tree);

		treeView.setMinimumSize(new Dimension(325, 200));
		treeView.setPreferredSize(new Dimension(350, 200));
		splitPane.setTopComponent(treeView);

		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		splitPane.setBottomComponent(scrollPane);

		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setMaximumSize(new Dimension(200, 100));
		editorPane.setPreferredSize(new Dimension(200, 200));
		editorPane.setMinimumSize(new Dimension(50, 100));

		// bottom panel
		statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		statusBar.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(4, 4, 4, 4)));

		// Setting the Record Count and Booking panel
		JPanel functionalPanel = new JPanel(new BorderLayout());
		functionalPanel.add(BorderLayout.SOUTH, statusBar);

		statusLabel = new JLabel("Status");
		statusLabel.setPreferredSize(new Dimension(500, 20));
		statusBar.add(statusLabel);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(functionalPanel, BorderLayout.SOUTH);
		this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		// Time Refresh
		if (mainProps.getGuiTimer()) {
			TimeUnit time = TimeUnit.MILLISECONDS;
			TimeUnit unit = mainProps.getGuiTimerDelayTimeUnitTU();
			int delay = (int) time.convert(mainProps.getGuiTimerDelay(), unit);
			timer = new Timer(delay, new AutoRefresh());
			timer.start();
		}

		// Finishing frame
		this.pack();

		getFrameSize();

		this.setVisible(true);

		populateTree();

	}

	/**
	 * Enables events to run on seperate thread so not to interfere with the GUI
	 * functions
	 */
	private void invokeLater(Runnable run) {
		SwingUtilities.invokeLater(run);
	}

	private void createAdmin(JMenuBar menuBar) {

		String userName = getUsername(mainProps);

		try {

			boolean isAdmin = AdminMenuConfig.isAdmin(mainProps.getAdminKey(), mainProps.getAdminPassword(), userName);

			if (isAdmin) {
				menuBar.add(new JMenu(""));

				JMenu adminMenu = new JMenu("Admin");
				adminMenu.setMnemonic(KeyEvent.VK_A);

				JMenuItem aliasMenuItem = new JMenuItem("Alias");
				aliasMenuItem.setMnemonic(KeyEvent.VK_L);
				aliasMenuItem.addActionListener(new AliasSettings(aliasMenuItem.getText()));
				adminMenu.add(aliasMenuItem);
				menuBar.add(adminMenu);

				logger.info("Admin mode enabled");
			}

		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | NumberFormatException e) {

			logger.log(Level.SEVERE, "Error wiht verfing admin access", e);
		}

	}

	private String getUsername(SGE_MonitorProp mainProps) {

		String userName = System.getProperty("user.name");
		if (mainProps.getUsernameOverride() != null && (mainProps.getUsernameOverride().trim().length() != 0))
			userName = mainProps.getUsernameOverride();
		return userName;

	}

	private void getFrameSize() {

		DisplayTool displayTool = new DisplayTool();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int height = mainProps.getFrameHeight();
		int width = mainProps.getFrameWidth();
		boolean displayExist = displayTool.deviceExist(this.getGraphicsConfiguration().getDevice().getIDstring());

		int y = mainProps.getFrameY();
		int x = mainProps.getFrameX();

		if ((height > dim.getHeight() || width > dim.getWidth()) && !displayExist) {

			setExtendedState(MAXIMIZED_BOTH);
		} else {

			if (height == 0 || width == 0) {

				width = (int) (dim.getWidth() / mainProps.getFrameScreenRatio());
				height = (int) (dim.getHeight() / mainProps.getFrameScreenRatio());

				logger.info("Console Width: " + width + " Height: " + height);
				x = (int) (dim.getWidth() - this.getWidth()) / 2;
				y = (int) (dim.getHeight() - this.getHeight()) / 2;
			}

			this.setSize(width, height);

			this.setLocation(x, y);
		}
	}

	private void setFrameSize() {
		mainProps.setFrameHeight(this.getHeight());
		mainProps.setFrameWidth(this.getWidth());
	}

	private void setFrameLocation() {
		mainProps.setFrameY(this.getY());
		mainProps.setFrameX(this.getX());
		mainProps.setGuiDeviceId(this.getGraphicsConfiguration().getDevice().getIDstring());
	}

	private class RetrieveTree extends QueuableWorker<Void, Void> {
		public RetrieveTree(WorkerManager manager) {
			super(manager);
		}

		public Void doInBackground() {

			try {

				tree.setRootVisible(true);

				logger.info("Connecting to cluster");
				setStatusLabel("Connecting to cluster");

				ClusterDataCollector worker = new ClusterDataCollector(mainProps);
				Cluster cluster = worker.retrieveClusterData();

				if (cluster != null) {
					logger.info("Building tree");
					setStatusLabel("Populating GUI");

					TreeBuilder tbmt = new TreeBuilder(mainProps, tree, cluster);
					tbmt.refreshTree();

					tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

					LocalDateTime currentDateTime = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
					String formattedDateTime = currentDateTime.format(formatter);
					Console.setStatusLabel("Updated " + formattedDateTime);
				} else {

					Console.setAlertStatusLabel("Problems retrieving cluster data");

				}

			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error getting data", e);
			} finally {

			}
			return null;
		}

		@Override
		protected void completed() {
			logger.info("Retrieved cluster data and refresh tree");
		}

	}

	protected void populateTree() {
		logger.finer("Calling for Tree refress");
		connectionManager.queueExecution((new RetrieveTree(connectionManager)));
		logger.fine("Setting ExecutorService to shutdown");
	}

	private void displayURL(ClusterNodeAbstract clusterNode) {
		if (clusterNode != null) {

			scrollPane.setAutoscrolls(true);
			JPanel panel = DetailedInfoFactory.createDetailedInfoPanel(clusterNode, tree);
			scrollPane.getViewport().add(panel);

		} else {
			editorPane.setText("Not Found");
		}
	}

	private void SetupKeyBinding() {
		JPanel contentPane = (JPanel) this.getContentPane();
		InputMap iMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap aMap = contentPane.getActionMap();

		String keyAction = "REFRESH";
		KeyStroke keyStroke = KeyStroke.getKeyStroke("F5");
		iMap.put(keyStroke, keyAction);
		aMap.put(keyAction, new AutoRefresh());

	}

	/**
	 * Exits console
	 */
	private class Exit implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			System.exit(0);
		}
	}

	private class AutoRefresh extends AbstractAction implements ActionListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2469989292110815749L;

		public AutoRefresh() {
			// TODO Auto-generated constructor stub
		}

		/**
		 * Enables the object to run in the <code>SwingUtilities.invokeLater</code>
		 * method
		 */
		public void actionPerformed(ActionEvent ae) {
			populateTree();
		}

	}

	private class NodeSelection implements TreeSelectionListener, Runnable {
		EventObject eo;

		@Override
		public void run() {

			// TODO Auto-generated method stub

			TreePath treePath = ((TreeSelectionEvent) eo).getNewLeadSelectionPath();

			if (treePath == null)
				treePath = ((TreeSelectionEvent) eo).getOldLeadSelectionPath();

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();

			logger.finer("Node selection: " + node);

			Object objectInfo = node.getUserObject();
			if (objectInfo instanceof ClusterNodeInterface) {
				ClusterNodeAbstract nodeInfo = (ClusterNodeAbstract) objectInfo;
				displayURL(nodeInfo);
			}
		}

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			// TODO Auto-generated method stub
			eo = e;
			invokeLater(this);

		}

	}

	/**
	 * The ActionListener for the connectMenuItem
	 */
	private class ConnectCluster implements ActionListener {
		public ConnectCluster() {

		}

		/**
		 * Enables the object to run in the <code>SwingUtilities.invokeLater</code>
		 * method
		 */
		public void actionPerformed(ActionEvent ae) {
			populateTree();
		}
	}

	/**
	 * @return the statusLabel
	 */
	public JLabel getStatusLabel() {
		return statusLabel;
	}

	public static void setStatusLabel(String text) {
		setStatusLabel(text, Color.BLACK);

	}

	public static void setAlertStatusLabel(String text) {
		setStatusLabel(text, Color.RED);
	}

	/**
	 * @param statusLabel the statusLabel to set
	 */
	public static void setStatusLabel(String text, Color fg) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (Console.statusLabel != null) {

					statusLabel.setForeground(fg);
					statusLabel.setText(text);
					statusLabel.updateUI();

				}
			}
		});

	}

	private class FontScalingSettings extends Settings {

		public FontScalingSettings(String title) {
			super(new FontScalingGui(Console.getFrames()[0], title, true, mainProps));
		}
	}

	private class ClusterConnectionSettings extends Settings {

		public ClusterConnectionSettings(String title) {
			super(new ClusterConnectionGUI(Console.getFrames()[0], title, true, mainProps));
		}
	}

	private class AliasSettings extends Settings {

		public AliasSettings(String title) {
			super(new AliasGUI(Console.getFrames()[0], title, true, mainProps));
		}
	}

	private abstract class Settings implements ActionListener, Runnable {
		protected JDialog dialog;

		public Settings(JDialog dialog) {
			this.dialog = dialog;
		}

		/**
		 * Instanstiate a gui that allows the user to change settings.
		 */
		public void run() {
			// JDialog gui = new ParamGUI(Console.getFrames()[0], "Settings", true,
			// mainProps);

			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (int) (dim.getWidth() - dialog.getWidth()) / 2;
			int y = (int) (dim.getHeight() - dialog.getHeight()) / 2;

			dialog.setLocation(x, y);
			dialog.setVisible(true);

		}

		/**
		 * Enables the object to run in the <code>SwingUtilities.invokeLater</code>
		 * method
		 */
		public void actionPerformed(ActionEvent ae) {
			invokeLater(this);
		}
	}

	private class SaveSettings extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {

			setFrameSize();
			setFrameLocation();
			executor.shutdown();
			mainProps.setUsernameAlias("");

			try {
				Main.saveSettings(mainProps);
			} catch (IOException | URISyntaxException | ConfigurationException ex) {
				// TODO Auto-generated catch block
				logger.log(Level.FINER, "Issue saving settings", ex);
			}
		}
	}
}
