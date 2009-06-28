package org.kde9.view.feature;

import javax.swing.JPanel;

import org.kde9.view.basic.SettingUnit;

public class TF 
extends JPanel {
	private SettingUnit u;
	
	TF() {
		u = new SettingUnit("ÆµÂÊãÐÖµ", 0, 500, 100, 1);
		
		add(u);
	}
	
	public double getValue() {
		return u.getValue();
	}
}
