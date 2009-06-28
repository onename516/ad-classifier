package org.kde9.view.feature;

import javax.swing.JPanel;

import org.kde9.view.basic.SettingUnit;

public class CHI 
extends JPanel {
	private SettingUnit u;
	
	CHI() {
		u = new SettingUnit("χ方统计量", -500, 500, 0, 100);
		
		add(u);
	}
	
	public double getValue() {
		return u.getValue();
	}
}
