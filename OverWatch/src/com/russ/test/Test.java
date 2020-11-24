/**
 * 
 */
package com.russ.test;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;


import com.ansys.cluster.monitor.gui.Console;
import com.ansys.cluster.monitor.gui.FontScalingGui;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * @author rmartine
 *
 */
public class Test extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -711208082799454016L;
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	private FontScalingGui gui;
	protected SGE_MonitorProp mainProps = new SGE_MonitorProp();


	int i = 0, num = 0;

	/**
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * 
	 */
	public Test() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {

		// Setting up Menu Bar
		JMenuBar menuBar = new JMenuBar();

		JMenu settingsMenu = new JMenu("Settings");

		JMenuItem settingsMenuItem = new JMenuItem("Font Scaling");
		settingsMenuItem.addActionListener(new FontScaling());
		settingsMenuItem.setMnemonic(KeyEvent.VK_C);
		settingsMenuItem.setToolTipText("Allow cluster settings to be Change and Saved");
		settingsMenu.add(settingsMenuItem);

		menuBar.add(settingsMenu);
		this.setJMenuBar(menuBar);

		// setSize(250, 150);
		setPreferredSize(new Dimension(350, 250));

	}

	private class FontScaling implements ActionListener, Runnable {

		/**
		 * Instanstiate a gui that allows the user to change settings.
		 */
		public void run() {
			gui = new FontScalingGui(Console.getFrames()[0], "Settings", true, mainProps);

			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (int) (dim.getWidth() - gui.getWidth()) / 2;
			int y = (int) (dim.getHeight() - gui.getHeight()) / 2;

			gui.setLocation(x, y);
			gui.setVisible(true);
			gui = null;

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
	 * Enables events to run on seperate thread so not to interfere with the GUI
	 * functions
	 */
	private void invokeLater(Runnable run) {
		SwingUtilities.invokeLater(run);
	}

	public void testing(int scaleAdj) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		float adjustment = (.001f * scaleAdj);
		float scale = 1.f + adjustment;
		UIManager.LookAndFeelInfo looks[] = UIManager.getInstalledLookAndFeels();

		for (UIManager.LookAndFeelInfo info : looks) {

			// if you want to change LaF to a spesific LaF,such as "GTK"
			// put here a if statement like:
			// if(info.getClassName().contains("GTK"))
			UIManager.setLookAndFeel(info.getClassName());

			UIDefaults defaults = UIManager.getDefaults();
			Enumeration<?> newKeys = defaults.keys();

			while (newKeys.hasMoreElements()) {
				Object obj = newKeys.nextElement();
				Object current = UIManager.get(obj);
				if (current instanceof FontUIResource) {
					FontUIResource resource = (FontUIResource) current;
					defaults.put(obj, new FontUIResource(resource.deriveFont(resource.getSize2D() * scale)));
					// System.out.printf("%50s : %s\n", obj, UIManager.get(obj));
				} else if (current instanceof Font) {
					Font resource = (Font) current;
					defaults.put(obj, resource.deriveFont(resource.getSize2D() * scale));
					// System.out.printf("%50s : %s\n", obj, UIManager.get(obj));
				}
			}
		}
	}

	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {

		Test t = new Test();
		t.pack();
		t.setVisible(true);
		GraphicsDevice device = t.getGraphicsConfiguration().getDevice();

		System.out.println(device.getIDstring());
		System.out.println(t.getLocationOnScreen());

	}

	private class SaveSettings extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			GraphicsDevice device = getGraphicsConfiguration().getDevice();

			System.out.println(device.getIDstring());
			System.out.println(getLocationOnScreen());

		}

	}

}
