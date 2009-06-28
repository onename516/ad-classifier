package org.kde9.view.training;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SelectFile 
extends JPanel 
implements ActionListener {
	private JButton b1;
	private JLabel l1;
	private JButton b2;
	private JLabel l2;
	private boolean flag = false;
	
	SelectFile() {
		b1 = new JButton("选择特征文件");
		b1.addActionListener(this);
		b1.setFocusable(false);
		l1 = new JLabel();
		b2 = new JButton("选择数据文件");
		b2.addActionListener(this);
		b2.setFocusable(false);
		l2 = new JLabel();
		
		setLayout(new GridLayout(0,1));
		add(b1);
		add(l1);
		add(b2);
		add(l2);
	}
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String[] getFile() {
		return new String[] {l1.getText(), l2.getText()};
	}
	
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("./"));
		int a = chooser.showOpenDialog(this.getRootPane());
		if(a == JFileChooser.APPROVE_OPTION) {
			if(e.getSource() == b1) {
				l1.setText(chooser.getSelectedFile().getAbsolutePath());
				l1.setToolTipText(l1.getText());
				flag = false;
			}
			else if(e.getSource() == b2) {
				l2.setText(chooser.getSelectedFile().getAbsolutePath());
				l2.setToolTipText(l2.getText());
				flag = false;
			}
		}
	}
}
