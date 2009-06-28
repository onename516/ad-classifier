package org.kde9.view.testing;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

public class TestingPane 
extends JTabbedPane {
	LeaveOneOut leaveOneOut;
	TestingFile testingFile;
	
	public TestingPane() {
		leaveOneOut = new LeaveOneOut();
		testingFile = new TestingFile();
		
		add("来自训练集", leaveOneOut);
		add("来自文件", testingFile);
		setPreferredSize(new Dimension(300, 120));
		
		TitledBorder t = new TitledBorder("选择测试数据来源");
		t.setTitleColor(Color.RED);
		t.setTitleJustification(TitledBorder.CENTER);
		setBorder(t);
	}
	
	public int getType() {
		return getSelectedIndex();
	}
	
	public double getValue() {
		return leaveOneOut.getValue();
	}
	
	public String getFile() {
		return testingFile.getFile();
	}
}
