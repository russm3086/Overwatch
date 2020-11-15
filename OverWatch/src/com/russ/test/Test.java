/**
 * 
 */
package com.russ.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;

import com.ansys.cluster.monitor.gui.tree.DetailedInfoProp;
import com.ansys.cluster.monitor.gui.tree.ProgressBar;
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
	ProgressBar mb1;
	ProgressBar mb2;
	ProgressBar mb3;
	JProgressBar jb;
	GraphicsConfiguration config;
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
		testing(0);
		this.addWindowListener(new SaveSettings());

		DetailedInfoProp qutoaSumDiProp = new DetailedInfoProp();
		qutoaSumDiProp.setPanelName("Quota Summary");

		int[] data1 = { 302, 100, 402 };
		qutoaSumDiProp.addSeries("Overall", data1, "core(s)", "All Avaiable Quota");

		int[] data2 = { 0, 1, 1 };
		qutoaSumDiProp.addSeries("vnc", data2, "core(s)", "All Avaiable Quota");

		int[] data3 = { 1, 0, 1 };
		qutoaSumDiProp.addSeries("dcv", data3, "core(s)", "All Avaiable Quota");

		mb1 = new ProgressBar("Overall", 302, 100, 402, "core(s)", "Avaiable Quota");
		mb2 = new ProgressBar("VNC", 0, 1, 1, "session(s)", "Avaiable Quota");
		mb3 = new ProgressBar("DCV", 1, 0, 1, "session(s)", "Avaiable Quota");

		JPanel panel = createProgressBarChart(qutoaSumDiProp);

		add(panel);

		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

		// setLayout(null);

		// setSize(250, 150);
		setPreferredSize(new Dimension(350, 250));

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
