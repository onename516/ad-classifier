package org.kde9.view.feature;

import javax.swing.JPanel;

import org.kde9.view.basic.SettingUnit;

public class CHI 
extends JPanel {
	private SettingUnit u;
	
	CHI() {
		u = new SettingUnit("�ַ�ͳ����", -20, 20, 1);
		
		add(u);
	}
	
	public double getValue() {
		return u.getValue();
	}
}
