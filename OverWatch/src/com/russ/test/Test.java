/**
 * 
 */
package com.russ.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;

import com.ansys.cluster.monitor.gui.Console;
import com.ansys.cluster.monitor.gui.FontScaling;
import com.ansys.cluster.monitor.gui.tree.DetailedInfoProp;
import com.ansys.cluster.monitor.gui.tree.ProgressBar;
import com.ansys.cluster.monitor.main.SGE_DataConst;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.WrapLayout;

/**
 * @author rmartine
 *
 */
public class Test extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -711208082799454016L;
	private FontScaling gui;
	protected SGE_MonitorProp mainProps = new SGE_MonitorProp();
	private JTextField selectedField;
	private JLabel testLabel;
	private float defaultFontSize;

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

		setLayout(new BorderLayout());

		defaultFontSize = UIManager.getFont("TextField.font").getSize2D();
		
		// Setting up Menu Bar
		JMenuBar menuBar = new JMenuBar();

		JMenu settingsMenu = new JMenu("Settings");

		JMenuItem settingsMenuItem = new JMenuItem("Font Scaling");
		settingsMenuItem.addActionListener(new Settings());
		settingsMenuItem.setMnemonic(KeyEvent.VK_C);
		settingsMenuItem.setToolTipText("Allow cluster settings to be Change and Saved");
		settingsMenu.add(settingsMenuItem);

		menuBar.add(settingsMenu);
		this.setJMenuBar(menuBar);

		JLabel connLabel = new JLabel("Font Scaling: ");

		int scaling = (int) mainProps.getGuiFontScaling();

		int scalingMaxValue = (int) SGE_DataConst.app_font_max_scaling;

		int scalingMinValue = -1 * (int) SGE_DataConst.app_font_max_scaling;

		JLabel refreshLabel = new JLabel("Scaling range " + scalingMinValue + " to " + scalingMaxValue);

		JSlider slider = new JSlider(JSlider.HORIZONTAL, scalingMinValue, scalingMaxValue, scaling);

		slider.addChangeListener(new SliderChange());

		// slider.setPreferredSize(new Dimension (300,50));

		JLabel selectedLabel = new JLabel("Selected Value:");
		selectedField = new JTextField(3);
		selectedField.setText(String.valueOf(scaling));

		refreshLabel.setLabelFor(slider);
		selectedLabel.setLabelFor(selectedField);

		JPanel selectedPanel = new JPanel();
		selectedPanel.setLayout(new FlowLayout());
		selectedPanel.add(selectedLabel);
		selectedPanel.add(selectedField);

		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		sliderPanel.add(refreshLabel);
		sliderPanel.add(slider);

		testLabel = new JLabel("TEST");
		testLabel.setFont(testLabel.getFont().deriveFont(defaultFontSize));
		
		add(sliderPanel, BorderLayout.NORTH);
		add(selectedPanel, BorderLayout.SOUTH);
		add(testLabel, BorderLayout.CENTER);

		slider.setMinorTickSpacing(5);
		slider.setMajorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		// setSize(250, 150);
		setPreferredSize(new Dimension(350, 250));

	}

	private class Settings implements ActionListener, Runnable {

		/**
		 * Instanstiate a gui that allows the user to change settings.
		 */
		public void run() {
			gui = new FontScaling(Console.getFrames()[0], "Settings", true, mainProps);

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

	private class SliderChange implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub

			JSlider slider = (JSlider) e.getSource();

			// Get the selection value of JSlider
			selectedField.setText(String.valueOf(slider.getValue()));

			UIDefaults defaults = UIManager.getDefaults();

			try {
				testing(slider.getValue());

				float scaleValue = (float) (1 + (slider.getValue() * .01));

				float newSize = (float) (defaultFontSize * scaleValue);

				Font font = testLabel.getFont().deriveFont(newSize);
				testLabel.setFont(font);

				System.out.println(slider.getValue());
				System.out.println(scaleValue);
				System.out.println(font.getSize2D());

			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	protected JPanel createProgressBarChart(DetailedInfoProp diProp) {

		JPanel panel = getPanel(diProp.getPanelName());

		ArrayList<DetailedInfoProp> list = diProp.getDetailedInfoPropList();

		for (DetailedInfoProp diPropChart : list) {

			int[] data = diPropChart.getProgressBarData();

			ProgressBar progressBar = new ProgressBar(diPropChart.getProgressBarLabel(), data[0], data[1], data[2],
					diPropChart.getProgressBarUnits(), diPropChart.getProgressBarToolTips());
			panel.add(progressBar);
		}

		return panel;
	}

	protected JPanel getPanel(String strTitle) {

		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(600, 100));
		panel.setLayout(new WrapLayout(FlowLayout.LEADING));

		LineBorder roundedLineBorder = new LineBorder(Color.blue, 1, true);
		TitledBorder title = BorderFactory.createTitledBorder(roundedLineBorder, strTitle);
		panel.setBorder(title);
		panel.setBackground(Color.WHITE);
		return panel;

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
