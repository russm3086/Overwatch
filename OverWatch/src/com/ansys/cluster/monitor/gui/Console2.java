/**
  */

package com.ansys.cluster.monitor.gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.json.JSONException;

import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeInterface;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.gui.tree.ClusterTreeCellRenderer;
import com.ansys.cluster.monitor.gui.tree.ClusterTreeListener;
import com.ansys.cluster.monitor.gui.tree.TreeBuilderMonitorThread;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.nio.ResourceLoader;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.EventObject;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

/**
 * The <code>Console</code> class provides the main graphical interface for the
 * database
 */
public class Console2 extends JFrame {
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
	private ConcurrentLinkedQueue<Cluster> blockingQueue = new ConcurrentLinkedQueue<Cluster>();

	/**
	 * The name of current class. To be used with the logging subsystem.
	 */
	private final String sourceClass = this.getClass().getName();

	/**
	 * The Logger instance. All log messages from this class are handle here.
	 */
	private Logger logger = Logger.getLogger(sourceClass);

	/**
	 * Constructs the UI's appearance
	 * 
	 * @throws IOException
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	public Console2(String title, SGE_MonitorProp mainProps) throws JSONException, IOException, InterruptedException {
		super(title);

		this.mainProps = mainProps;
		this.setDefaultCloseOperation(Console2.EXIT_ON_CLOSE);

		ImageIcon img = new ImageIcon(ResourceLoader.load(GUI_Const.Icon_Ansys));
		setIconImage(img.getImage());

		// Setting up Menu Bar
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setToolTipText("Connects to Cluster, Changes Settings, and " + "Quits Program");

		/**
		 * // Connect to Db MenuItem JMenuItem connectMenuItem = new JMenuItem("Connect
		 * to Cluster"); connectMenuItem.addActionListener(new ConnectCluster());
		 * connectMenuItem.setMnemonic(KeyEvent.VK_C);
		 * connectMenuItem.setToolTipText("Connects to the CLuster");
		 * fileMenu.add(connectMenuItem);
		 * 
		 * // Settings MenuItem JMenuItem settingsMenuItem = new JMenuItem("Settings");
		 * // settingsMenuItem.addActionListener(new Settings());
		 * settingsMenuItem.setMnemonic(KeyEvent.VK_S);
		 * settingsMenuItem.setToolTipText("Allow Settings to be Change and Saved");
		 * fileMenu.add(settingsMenuItem);
		 * 
		 */
		// Quit MenuItem
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(new Exit());
		quitMenuItem.setMnemonic(KeyEvent.VK_Q);
		quitMenuItem.setToolTipText("Quits Program");
		fileMenu.add(quitMenuItem);

		// Setting up MenuBar
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
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
		
		ToolTipManager.sharedInstance().registerComponent(tree);

		JScrollPane treeView = new JScrollPane(tree);

		treeView.setMinimumSize(new Dimension(300, 200));
		treeView.setPreferredSize(new Dimension(300, 200));
		splitPane.setTopComponent(treeView);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setBottomComponent(scrollPane);

		editorPane = new JEditorPane();
		editorPane.setMaximumSize(new Dimension(200, 100));
		editorPane.setPreferredSize(new Dimension(200, 200));
		editorPane.setMinimumSize(new Dimension(50, 100));
		scrollPane.setViewportView(editorPane);

		// bottom panel
		statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		statusBar.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(4, 4, 4, 4)));

		// Setting the Record Count and Booking panel
		JPanel functionalPanel = new JPanel(new BorderLayout());
		functionalPanel.add("South", statusBar);

		statusLabel = new JLabel("Status");
		statusLabel.setPreferredSize(new Dimension(300, 20));
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
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (dim.getWidth() / mainProps.getFrameScreenRatio());
		int height = (int) (dim.getHeight() / mainProps.getFrameScreenRatio());

		
		logger.info("Console Width: " + width + " Height: " + height);
		this.setSize(width, height);

		int x = (int) (dim.getWidth() - this.getWidth()) / 2;
		int y = (int) (dim.getHeight() - this.getHeight()) / 2;
		this.setLocation(x, y);

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

	private void populateTree() throws JSONException, IOException, InterruptedException {

		logger.info("Connecting to cluster Otterfing");

		tree.setRootVisible(true);
		ClusterMonitorThread clusterMonitor = new ClusterMonitorThread(mainProps, blockingQueue);
		clusterMonitor.retrieveData();
		
		tree.getModel().addTreeModelListener(new ClusterTreeListener(tree));
		TreeBuilderMonitorThread tbmt = new TreeBuilderMonitorThread(mainProps, blockingQueue, tree);
		tbmt.buildTree();
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

	}

	private void displayURL(ClusterNodeAbstract clusterNode) {
		if (clusterNode != null) {

			editorPane.setText(clusterNode.getSummary());

		} else {
			editorPane.setText("Not Found");
		}
	}

	/**
	 * Exits console
	 */
	private class Exit implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			System.exit(-1);
		}
	}

	private class AutoRefresh implements ActionListener, Runnable {

		/**
		 * Checks what room is selected and querys the database with the last search
		 * criteria.
		 */
		public void run() {

			try {

				populateTree();

			} catch (JSONException | IOException | InterruptedException e) {
				// TODO Auto-generated catch block

				logger.log(Level.FINER, "AutoRefresh Erorr", e);

			}

		}

		/**
		 * Enables the object to run in the <code>SwingUtilities.invokeLater</code>
		 * method
		 */
		public void actionPerformed(ActionEvent ae) {
			invokeLater(this);
		}
	}

	private class NodeSelection implements TreeSelectionListener, Runnable {
		EventObject eo;

		@Override
		public void run() {

			// TODO Auto-generated method stub

			TreePath treePath = ((TreeSelectionEvent) eo).getNewLeadSelectionPath();

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
	private class ConnectCluster implements ActionListener, Runnable {

		/**
		 * Loads the console parameter settings, instantiates a new database handler,
		 * reloads the <code>tableData</code> object with all records, sets the frame
		 * title, and starts the timer.
		 */
		public void run() {
			try {

				populateTree();

			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error connecting to the database", e);

			}
		}

		/**
		 * Enables the object to run in the <code>SwingUtilities.invokeLater</code>
		 * method
		 */
		public void actionPerformed(ActionEvent ae) {
			invokeLater(this);
		}
	}

	/**
	 * @return the statusLabel
	 */
	public static JLabel getStatusLabel() {
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
		Console2.statusLabel.setText(text);
		Console2.statusLabel.setForeground(fg);

	}

}
