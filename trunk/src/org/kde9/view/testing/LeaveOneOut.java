package org.kde9.view.testing;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.kde9.view.basic.SettingUnit;

public class LeaveOneOut 
extends JPanel {
	JLabel l;
	SettingUnit u1;
	
	public LeaveOneOut() {
		l = new JLabel("使用 leave one out 方法");
		l.setPreferredSize(new Dimension(200, 15));
		l.setHorizontalAlignment(JLabel.CENTER);
		u1 = new SettingUnit("分组个数", 2, 20, 1);
		
		add(l);
		add(u1);
	}
	
	public double getValue() {
		return u1.getValue();
	}
}
