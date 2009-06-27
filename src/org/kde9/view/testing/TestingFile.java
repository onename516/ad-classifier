package org.kde9.view.testing;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class TestingFile 
extends JPanel 
implements ActionListener {
	private JButton b;
	private JLabel l;
	
	TestingFile() {
		b = new JButton("—°‘Ò≤‚ ‘ ˝æ›");
		b.addActionListener(this);
		l = new JLabel();
		
		setLayout(new GridLayout(0,1));
		add(b);
		add(l);
	}
	
	public String getFile() {
		return l.getText();
	}
	
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("./"));
		int a = chooser.showOpenDialog(this.getRootPane());
		if(a == JFileChooser.APPROVE_OPTION) {
			if(e.getSource() == b) {
				l.setText(chooser.getSelectedFile().getAbsolutePath());
				l.setToolTipText(l.getText());
			}
		}
	}
}
