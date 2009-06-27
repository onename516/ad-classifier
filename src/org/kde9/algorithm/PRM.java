package org.kde9.algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class PRM 
extends Foil {
	private Vector<Double> weight;
	private double wRate = 0;
	private double totalWeight = 0;
	private double limit = 0.1;
	
	public PRM() {
		super();
		weight = new Vector<Double>();	
	}
	
	public void setLimit(double limit) {
		this.limit = limit;
	}

	public void setWRate(double rate) {
		if(rate >= 0 && rate < 1)
			wRate = rate;
	}

	public boolean insertTrainingSet(int type, Vector<Integer> values) {
		weight.add(100.0);
		totalWeight += 100;
		return super.insertTrainingSet(type, values);
	}
	
	public void clear() {
		super.clear();
		weight.clear();
		wRate = 0;
		limit = 0.1;
	}
	
	public void foilTrainingSet(int type) {
		if(!types.containsKey(type))
			throw new ArrayIndexOutOfBoundsException(
					"没有这样的类别：" + type);
		initFoil();
		HashSet<Integer> pos = (HashSet<Integer>) types.get(type).clone();
		HashSet<Integer> neg = new HashSet<Integer>();
		double start = totalWeight;
		for(int t : types.keySet())
			if(t != type)
				neg.addAll(types.get(t));
		double i = 0;
		while(totalWeight > ignoreRate*start) {
			HashSet<Integer> posx = (HashSet<Integer>) pos.clone();
			HashSet<Integer> negx = (HashSet<Integer>) neg.clone();
			HashMap<Integer, Integer> rule = getBestRule(posx, negx);
			rules.add(rule);
			adjustPos(pos, rule);
			double rate = 1-totalWeight/start;
//			if(10*rate > i) {
//				i = 10*rate + 1;
				System.out.println((100*rate) + "%");/////////////////////////////////////
//			}
		}
	}
	
	protected void adjustPos(HashSet<Integer> pos, HashMap<Integer, Integer> rule) {
		totalWeight = 0;
		for(int index : pos) {
			if(satisfyRule(attributeValue.get(index), rule)) {
				weight.setElementAt(weight.get(index)*wRate, index);
			}
			totalWeight += weight.get(index);
		}
	}
	
	protected HashMap<Integer, Integer> getBestRule(HashSet<Integer> pos, HashSet<Integer> neg) {
		HashMap<Integer, Integer> rule = new HashMap<Integer, Integer>();
		while(true) {
			int[] literal = getBestLiteral(pos, neg, rule);
			if(literal == null)
				break;
			if(rule.containsKey(literal[0]) && rule.get(literal[0]).equals(literal[1]))
				break;
			rule.put(literal[0], literal[1]);
			adjustPosAndNeg(pos, neg, rule);
		}
		return rule;
	}
	
	protected int[] getBestLiteral(HashSet<Integer> pos, HashSet<Integer> neg, 
			HashMap<Integer, Integer> rule) {
		int[] temp = {0, 0};
		double gain = -2147483647;
		for(int i = 0; i < attributeNum; i++)
			for(int j = 0; j < span.get(i); j++) {
				double px = 0, nx = 0;
				double p = 0, n = 0;
				for(int index : pos) {
					p += weight.get(index);
					if(attributeValue.get(index).get(i).equals(j))
						px += weight.get(index);
				}
				for(int index : neg) {
					n += weight.get(index);
					if(attributeValue.get(index).get(i).equals(j))
						nx += weight.get(index);
				}
				double gainx = 
					px * (Math.log(px/(px+nx)) - Math.log(p/(p+n)));
				if(gainx > gain) {
					temp[0] = i;
					temp[1] = j;
					gain = gainx;
				}
			}
		if(gain < limit)
			return null;
		return temp;
	}
}
