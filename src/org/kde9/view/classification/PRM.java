package org.kde9.view.classification;

import javax.swing.JPanel;

import org.kde9.view.basic.SettingUnit;

public class PRM 
extends JPanel {
	SettingUnit u1;
	SettingUnit u2;
	SettingUnit u3;
	
	PRM() {
		u1 = new SettingUnit("结束阈值", 0, 100, 10, 100);
		u2 = new SettingUnit("增益下限", 1, 100, 10, 10);
		u3 = new SettingUnit("衰减比率", 0, 99, 10, 100);
		
		add(u1);
		add(u2);
		add(u3);
	}
	
	public double getWRate() {
		return u3.getValue();
	}
	
	public double getLimit() {
		return u2.getValue();
	}
	
	public double getRate() {
		return u1.getValue();
	}
}
