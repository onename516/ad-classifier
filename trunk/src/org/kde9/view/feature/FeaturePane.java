package org.kde9.view.feature;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

public class FeaturePane 
extends JTabbedPane {
	TF tf;
	CHI chi;
	
	public FeaturePane() {
		tf = new TF();
		chi = new CHI();
		
		add("TF", tf);
		add("CHI", chi);
		setPreferredSize(new Dimension(300, 100));
		
		TitledBorder t = new TitledBorder("ѡ������ѡȡ����");
		t.setTitleColor(Color.RED);
		t.setTitleJustification(TitledBorder.CENTER);
		setBorder(t);
	}
	
	public int getType() {
		return getSelectedIndex();
	}
	
	public double getValueTF() {
		return tf.getValue();
	}
	
	public double getValueCHI() {
		return chi.getValue();
	}
}