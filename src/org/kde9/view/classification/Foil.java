package org.kde9.view.classification;

import javax.swing.JPanel;

import org.kde9.view.basic.SettingUnit;

public class Foil 
extends JPanel {
	SettingUnit u1;
	SettingUnit u2;
	
	Foil() {
		u1 = new SettingUnit("数据忽略比率", 0, 99, 5, 100);
		u2 = new SettingUnit("最长规则限制", 1, 20, 10, 1);
		
		add(u1);
		add(u2);
	}
	
	public double getRate() {
		return u1.getValue();
	}
	
	public double getLength() {
		return u2.getValue();
	}
}
