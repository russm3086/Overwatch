/**
 * 
 */
package com.russ.test;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Enumeration;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import com.russ.util.TimeUtil;

/**
 * @author rmartine
 *
 */
public class Test extends JFrame {
	MeteredBar mb1;
	MeteredBar mb2;
	MeteredBar mb3;
	JProgressBar jb;
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

		mb1 = new MeteredBar("Overall", 302, 100, 402, "core(s)", "Avaiable Quota");
		mb2 = new MeteredBar("VNC", 0, 1, 1, "session(s)", "Avaiable Quota");
		mb3 = new MeteredBar("DCV", 1, 0, 1, "session(s)", "Avaiable Quota");

		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

		add(mb1);
		add(mb2);
		add(mb3);

		// setLayout(null);

		// setSize(250, 150);
		setPreferredSize(new Dimension(300, 200));

	}

	public void iterate() {
		while (i <= 2000) {
			jb.setValue(i);
			i = i + 20;
			try {
				Thread.sleep(150);
			} catch (Exception e) {
			}
		}
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
		// t.iterate();
	}

}
