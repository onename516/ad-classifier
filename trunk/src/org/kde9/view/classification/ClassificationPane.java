package org.kde9.view.classification;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

public class ClassificationPane 
extends JTabbedPane {
	Foil foil;
	PRM prm;
	
	public ClassificationPane() {
		foil = new Foil();
		prm = new PRM();
		
		add("FOIL", foil);
		add("PRM", prm);
		setPreferredSize(new Dimension(300, 160));
		
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
	
	public double getPRMRate() {
		return prm.getRate();
	}
	
	public double getPRMLimit() {
		return prm.getLimit();
	}
	
	public double getPRMWRate() {
		return prm.getWRate();
	}
}