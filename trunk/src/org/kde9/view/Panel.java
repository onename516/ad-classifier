package org.kde9.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.kde9.control.Controller;
import org.kde9.util.Constants;
import org.kde9.view.classification.ClassificationPane;
import org.kde9.view.classification.PRM;
import org.kde9.view.feature.FeaturePane;
import org.kde9.view.testing.TestingPane;
import org.kde9.view.training.TrainingPane;

public class Panel 
extends JPanel
implements Constants, ActionListener {
	TrainingPane trainingPane;
	TestingPane testingPane;
	FeaturePane featurePane;
	ClassificationPane classificationPane;
	
	JButton start;
	JButton stop;
	JTextArea area;
	
	Controller controller;
	
	Panel() {
		trainingPane = new TrainingPane();
		testingPane = new TestingPane();
		featurePane = new FeaturePane();
		classificationPane = new ClassificationPane();
		
		JPanel left = new JPanel();
		left.setPreferredSize(new Dimension(300, 600));
		left.add(trainingPane);
		left.add(testingPane);
		left.add(featurePane);
		left.add(classificationPane);
		
		start = new JButton("¿ªÊ¼");
		start.addActionListener(this);
		stop = new JButton("Í£Ö¹");
		stop.addActionListener(this);
		stop.setEnabled(false);
		
		JPanel rightUp = new JPanel();
		rightUp.setLayout(new GridLayout(0, 2));
		rightUp.add(start);
		rightUp.add(stop);
		rightUp.setPreferredSize(new Dimension(400, 100));
		rightUp.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		
		area = new JTextArea();
		area.setEditable(false);
		
		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());
		right.add("North", rightUp);
		right.add("Center", new JScrollPane(area));
		right.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		
		setLayout(new BorderLayout());
		add("West", left);
		add("Center", right);
		
		controller = new Controller(area, start, stop);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == start) {
			area.setText("");
			setting();
			if(!trainingPane.isFlag()) {
				controller.init();
				trainingPane.setFlag(true);
			}
			start.setEnabled(false);
			stop.setEnabled(true);
			controller.start();
		} else if(e.getSource() == stop) {
			controller.stop();
			start.setEnabled(true);
			stop.setEnabled(false);
		}
	}
	
	private void setting() {
		controller.setFile1(trainingPane.getFile()[0]);
		controller.setFile2(trainingPane.getFile()[1]);
		
		controller.setDataType(testingPane.getType());
		controller.setFile3(testingPane.getFile());
		controller.setGroupNum((int)testingPane.getValue());
		
		controller.setFeatureType(featurePane.getType());
		controller.setTFYIELD((int)featurePane.getValueTF());
		controller.setCHIYIELD(featurePane.getValueCHI());
		
		controller.setClassifyType(classificationPane.getType());
		if(classificationPane.getType() == 0) {
			controller.setIgnoreRate(classificationPane.getFoilRate());
			controller.setMaxRuleLength((int)classificationPane.getFoilLength());
		} else if(classificationPane.getType() == 1) {
			controller.setIgnoreRate(classificationPane.getPRMRate());
			controller.setLimit(classificationPane.getPRMLimit());
			controller.setWRate(classificationPane.getPRMWRate());
		}
	}
}
