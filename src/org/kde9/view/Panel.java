package org.kde9.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.kde9.util.Constants;
import org.kde9.view.classification.ClassificationPane;
import org.kde9.view.feature.FeaturePane;
import org.kde9.view.testing.TestingPane;
import org.kde9.view.training.TrainingPane;

public class Panel 
extends JPanel
implements Constants {
	TrainingPane trainingPane;
	TestingPane testingPane;
	FeaturePane featurePane;
	ClassificationPane classificationPane;
	
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
		
		setLayout(new BorderLayout());
		add("West", left);
	}
}
