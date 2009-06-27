package org.kde9.view.classification;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

public class ClassificationPane 
extends JTabbedPane {
	Foil foil;
	
	public ClassificationPane() {
		foil = new Foil();
		
		add("FOIL", foil);
		setPreferredSize(new Dimension(300, 150));
		
		TitledBorder t = new TitledBorder("—°‘Ò∑÷¿‡∆˜");
		t.setTitleColor(Color.RED);
		t.setTitleJustification(TitledBorder.CENTER);
		setBorder(t);
	}
	
	public int getType() {
		return getSelectedIndex();
	}
	
	public double getFoilRate() {
		return foil.getRate();
	}
	
	public double getFoilLength() {
		return foil.getLength();
	}
}