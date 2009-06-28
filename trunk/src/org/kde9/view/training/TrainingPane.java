package org.kde9.view.training;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

public class TrainingPane 
extends JPanel {
	SelectFile selectFile;
	
	public TrainingPane() {
		selectFile = new SelectFile();
		
		setLayout(new BorderLayout());
		add(selectFile);
		setFocusable(false);
		setPreferredSize(new Dimension(300, 150));
		
		TitledBorder t = new TitledBorder("选择训练数据集");
		t.setTitleColor(Color.RED);
		t.setTitleJustification(TitledBorder.CENTER);
		setBorder(t);
	}
	
	public boolean isFlag() {
		return selectFile.isFlag();
	}

	public void setFlag(boolean flag) {
		selectFile.setFlag(flag);
	}

	public String[] getFile() {
		return selectFile.getFile();
	}
}
