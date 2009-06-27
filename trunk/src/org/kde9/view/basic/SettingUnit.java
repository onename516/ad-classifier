package org.kde9.view.basic;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SettingUnit 
extends JPanel 
implements ChangeListener {
	JLabel name;
	JSlider slider;
	JLabel value;
	double rate;
	
	public SettingUnit(String name, int begin, int end, double rate) {
		this.rate = rate;
		this.name = new JLabel(name);
		this.name.setHorizontalAlignment(JLabel.CENTER);
		slider = new JSlider(begin, end);
		slider.addChangeListener(this);
		slider.setPreferredSize(new Dimension(150, 20));
		value = new JLabel(String.valueOf(slider.getValue()/rate));
		value.setHorizontalAlignment(JLabel.CENTER);
		value.setPreferredSize(new Dimension(40, 20));
		FlowLayout f = new FlowLayout();
		f.setAlignment(FlowLayout.LEFT);
		setLayout(f);
		add(this.name);
		add(slider);
		add(value);
	}

	public double getValue() {
		return slider.getValue()/rate;
	}
	
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		value.setText(String.valueOf(slider.getValue()/rate));
	}
}
