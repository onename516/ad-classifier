package org.kde9.view;

import java.security.AccessControlException;
import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import ch.randelshofer.quaqua.QuaquaManager;
import ch.randelshofer.quaqua.util.Methods;

public class Main 
extends JFrame {
	
	private JPanel panel;
	
	public Main() {
		panel = new Panel();
		setContentPane(panel);
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		final java.util.List argList = Arrays.asList(args);
		
        System.setProperty("sun.java2d.noddraw", "true");
		
		if (System.getProperty("os.name").toLowerCase().indexOf("mac") == -1) {
			System.setProperty("Quaqua.Debug.crossPlatform", "true");
			System.setProperty("swing.aatext", "true");
			//System.setProperty("JButton.style", "bevel");
		}
		try {
			// System.setProperty("Quaqua.TabbedPane.design", "jaguar");
			String lafClassName = QuaquaManager.getLookAndFeelClassName();
			System.out.println(lafClassName);
			UIManager.setLookAndFeel(lafClassName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (System.getProperty("apple.laf.useScreenMenuBar") == null
					&& System.getProperty("com.apple.macos.useScreenMenuBar") == null) {
				System.setProperty("apple.laf.useScreenMenuBar", "true");
				System.setProperty("com.apple.macos.useScreenMenuBar", "true");
			}
		} catch (AccessControlException e) {
			// can't do anything about this
		}

		boolean useDefaultLookAndFeelDecoration = !System
				.getProperty("os.name").toLowerCase().startsWith("mac")
				&& !System.getProperty("os.name").toLowerCase().startsWith(
						"darwin");
		int index = argList.indexOf("-decoration");
		if (index != -1 && index < argList.size() - 1) {
			useDefaultLookAndFeelDecoration = argList.get(index + 1).equals(
					"true");
		}

		if (useDefaultLookAndFeelDecoration) {
			try {
				Methods.invokeStatic(JFrame.class,
						"setDefaultLookAndFeelDecorated", Boolean.TYPE,
						Boolean.TRUE);
				Methods.invokeStatic(JDialog.class,
						"setDefaultLookAndFeelDecorated", Boolean.TYPE,
						Boolean.TRUE);
			} catch (NoSuchMethodException e) {
				// can't do anything about this
				e.printStackTrace();
			}
		}
		
		new Main();
	}
}
