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
		
		add("����ѵ����", leaveOneOut);
		add("�����ļ�", testingFile);
		setPreferredSize(new Dimension(300, 120));
		
		TitledBorder t = new TitledBorder("ѡ�����������Դ");
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
