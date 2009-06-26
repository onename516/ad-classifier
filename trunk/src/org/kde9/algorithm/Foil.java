package org.kde9.algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class Foil {
	/**
	 * ����ֵ�ĸ���
	 */
	private int attributeNum = 0;
	
	private int maxRuleLength = 10;

	/**
	 * ÿ������ֵ���ܵ�ȡֵ������
	 * <strong>
	 * ע�⣺ÿ������ֵ��ȡֵ�����Ǵ�0��ʼ����������������
	 * </strong>
	 */
	private Vector<Integer> span;
	
	/**
	 * ÿ��TrainingSet��ÿ������ֵ��ȡֵ��
	 */
	private Vector<Vector<Integer>> attributeValue;
	
	/**
	 * ÿ������а�����TrainingSet��
	 */
	private HashMap<Integer, HashSet<Integer>> types;
	
	/**
	 * ���ɵĹ���
	 */
	private Vector<HashMap<Integer, Integer>> rules;
	
	/**
	 * ��ʱ����
	 */
	private Vector<Integer> tempVector = new Vector<Integer>();
	
	public Foil() {
		span = new Vector<Integer>();
		attributeValue = new Vector<Vector<Integer>>();
		types = new HashMap<Integer, HashSet<Integer>>();
		rules = new Vector<HashMap<Integer,Integer>>();
	}
	
	public void setMaxRuleLength(int maxRuleLength) {
		if(maxRuleLength > 0)
			this.maxRuleLength = maxRuleLength;
		else
			throw new NumberFormatException(
					"������������ĸ��������ֵ����Ϊ��������");
	}
	
	/**
	 * ����ÿ������ֵ��ȡֵ��Χ
	 * <p>
	 * <strong>ע�⣺ÿ������ֵ��ȡֵ�����Ǵ�0��ʼ����������������</strong>
	 * <p>
	 * ���磬��һ������ֵ�Ŀ���ȡֵΪ0��1��2ʱ������ȡֵ��ΧΪ3��������ȡֵ�ĸ�����
	 * @throws ArrayIndexOutOfBoundsException
	 * 		����������Vector�Ĵ�С������ֵ�ĸ�������ʱ�������׳����쳣��
	 * @throws NumberFormatException
	 * 		������ֵ�Ĵ�С����������ʱ�������׳����쳣��
	 * @param span
	 * 		������������ֵ��Χ�����顣
	 */
	public void setSpanOfAttribute(Vector<Integer> span) {
		if(span.size() != attributeNum)
			throw new ArrayIndexOutOfBoundsException(
					"Vector�Ĵ�С������ֵ�ĸ������������Ƿ����ǵ���setAttributeNum�����ˣ�");
		for(int i : span)
			if(i > 0)
				this.span.add(i);
			else
				throw new NumberFormatException("����ֵ�Ĵ�С��������������");
	}
	
	/**
	 * ����ÿ������ֵ��ȡֵ��ΧΪͬһ��ֵ
	 * <p>
	 * <strong>ע�⣺ÿ������ֵ��ȡֵ�����Ǵ�0��ʼ����������������</strong>
	 * <p>
	 * ���磬��һ������ֵ�Ŀ���ȡֵΪ0��1��2ʱ������ȡֵ��ΧΪ3��������ȡֵ�ĸ�����
	 * @throws NumberFormatException
	 * 		������ֵ�Ĵ�С����������ʱ�������׳����쳣��
	 * @param span
	 * 		������������ֵ��Χ�����顣
	 */
	public void setSpanOfAttribute(int span) {
		if(span <= 0)
			throw new NumberFormatException("����ֵ�Ĵ�С��������������");
		for(int i = 0; i < attributeNum; i++)
			this.span.add(span);
	}
	
	/**
	 * ��������ֵ�ĸ���
	 * @throws NumberFormatException
	 * 		������ֵ�Ĵ�С����������ʱ�������׳����쳣��
	 * @param size
	 * 		����ֵ�ĸ�����<strong>��������������</strong>
	 */
	public void setAttributeNum(int size) {
		if(size > 0)
			this.attributeNum = size;
		else
			throw new NumberFormatException("����ֵ�Ĵ�С��������������");
	}
	
	/**
	 * ����һ��ѵ������
	 * @throws ArrayIndexOutOfBoundsException
	 * 		����������Vector�Ĵ�С������ֵ�ĸ�������ʱ�������׳����쳣��
	 * @throws NumberFormatException
	 * 		������ֵ�Ĵ�С�������趨�ķ�Χ��ʱ�������׳����쳣��
	 * @param type
	 * 		ѵ���������������
	 * @param values
	 * 		ѵ�����ݵ���������ֵ��ȡֵ
	 * @return
	 * 		�����Ƿ�ɹ�
	 */
	public boolean insertTrainingSet(int type, Vector<Integer> values) {
		if(values.size() != attributeNum)
			throw new NumberFormatException("����ֵ�Ĵ�С��������������");
		Vector<Integer> temp = new Vector<Integer>();
		for(int i = 0; i < values.size(); i++) {
			int value = values.get(i);
			int span = this.span.get(i);
			if(value < span && value >= 0)
				temp.add(value);
			else
				throw new NumberFormatException("����ֵ��ȡֵ���趨��Χ������");
		}
		if(attributeValue.add(temp)) {
			if(!types.containsKey(type))
				types.put(type, new HashSet<Integer>());
			HashSet<Integer> set = types.get(type);
			set.add(attributeValue.size()-1);
			return true;
		} else
			return false;
	}
	
	/**
	 * �������е�����
	 * <p>
	 * �������²���ѵ����ǰ����ɵ����ݡ�
	 */
	public void clear() {
		attributeNum = 0;
		span.clear();
		attributeValue.clear();
		types.clear();
		rules.clear();
	}
	
	/**
	 * �жϸ����������Ƿ����ڸո�ѵ�������
	 * @param data
	 * 		Ҫ�жϵ����ݵ�����ֵ�ļ���
	 * @return
	 * 		�Ƿ����ڸո�ѵ�����������Ƿ���true��
	 */
	public boolean belongToCurrentClass(Vector<Integer> data) {
		for(HashMap<Integer, Integer> rule : rules) {
			if(satisfyRule(data, rule))
				return true;
		}
		return false;
	}
	
	/**
	 * FOIL�㷨ʵ��
	 * @param type
	 */
	public void foilTrainingSet(int type) {
		if(!types.containsKey(type))
			throw new ArrayIndexOutOfBoundsException(
					"û�����������" + type);
		initFoil();
		HashSet<Integer> pos = (HashSet<Integer>) types.get(type).clone();
		HashSet<Integer> neg = new HashSet<Integer>();
		for(int t : types.keySet())
			if(t != type)
				neg.addAll(types.get(t));
		while(pos.size() > 0) {
			HashSet<Integer> posx = (HashSet<Integer>) pos.clone();
			HashSet<Integer> negx = (HashSet<Integer>) neg.clone();
			HashMap<Integer, Integer> rule = getBestRule(posx, negx);
			rules.add(rule);
			adjustPos(pos, rule);
		}
	}
	
	/**
	 * FOIL�㷨�ڲ�ѭ��
	 * @param pos
	 * @param neg
	 * @return
	 */
	private HashMap<Integer, Integer> getBestRule(HashSet<Integer> pos, HashSet<Integer> neg) {
		HashMap<Integer, Integer> rule = new HashMap<Integer, Integer>();
		while(neg.size() > 0 && rule.size() < maxRuleLength) {
			int[] literal = getBestLiteral(pos, neg, rule);
			if(rule.containsKey(literal[0]) && rule.get(literal[0]).equals(literal[1]))
				break;
			rule.put(literal[0], literal[1]);
			adjustPosAndNeg(pos, neg, rule);
		}
		return rule;
	}
	
	private int[] getBestLiteral(HashSet<Integer> pos, HashSet<Integer> neg, 
			HashMap<Integer, Integer> rule) {
		int[] temp = {0, 0};
		double gain = -2147483647;
		for(int i = 0; i < attributeNum; i++)
			for(int j = 0; j < span.get(i); j++) {
				double px = 0, nx = 0;
				for(int index : pos)
					if(attributeValue.get(index).get(i).equals(j))
						px++;
				for(int index : neg)
					if(attributeValue.get(index).get(i).equals(j))
						nx++;
				double gainx = 
					px * (Math.log(px/(px+nx)) - Math.log(pos.size()/(double)(pos.size()+neg.size())));
				if(gainx > gain) {
					temp[0] = i;
					temp[1] = j;
					gain = gainx;
				}
			}
		return temp;
	}
	
	private void adjustPosAndNeg(HashSet<Integer> pos, HashSet<Integer> neg, 
			HashMap<Integer, Integer> rule) {
		tempVector.clear();
		for(int index : pos)
			if(!satisfyRule(attributeValue.get(index), rule))
				tempVector.add(index);
		for(int index : tempVector)
			pos.remove(index);
		tempVector.clear();
		for(int index : neg)
			if(!satisfyRule(attributeValue.get(index), rule))
				tempVector.add(index);
		for(int index : tempVector)
			neg.remove(index);
	}
	
	/**
	 * FOIL�㷨���ѭ����Ԫ�����
	 * <p>
	 * ɾ���������rule����Ԫ��
	 * @param pos
	 * 		��Ԫ�鼯��
	 * @param rule
	 * 		����
	 */
	private void adjustPos(HashSet<Integer> pos, HashMap<Integer, Integer> rule) {
		tempVector.clear();
		for(int index : pos) {
			if(satisfyRule(attributeValue.get(index), rule))
				tempVector.add(index);
		}
		for(int index : tempVector)
			pos.remove(index);
	}
	
	/**
	 * �жϸ����������Ƿ���������Ĺ���
	 * @param data
	 * 		����������
	 * @param rule
	 * 		�����Ĺ���
	 * @return
	 * 		�Ƿ����㣬�����㷵��true��
	 */
	private boolean satisfyRule(Vector<Integer> data, HashMap<Integer, Integer> rule) {
		for(int index : rule.keySet())
			if(!data.get(index).equals(rule.get(index)))
				return false;
		return true;
	}
	
	/**
	 * ��ʼ������
	 * <p>
	 * �������ÿգ��Ա��㷨�����µĹ��򼯡��÷�����FOIL�㷨�ڲ����ã����ڳ�ʼ�����򼯡�
	 */
	private void initFoil() {
		rules.clear();
	}
	
	/**
	 * ~~��������ʹ��~~
	 * @param detail
	 */
	public void summaru(boolean detail) {
		for (int type : types.keySet()) {
			System.out.println();
			System.out.println("class " + type + " [" + types.get(type).size() + "]");
			if (detail)
				for (int i : types.get(type)) {
					for (int j : attributeValue.get(i))
						System.out.print("\t" + j);
					System.out.println();
				}
		}
	}
	
	private void showRule() {
		System.out.println();
		System.out.println("���򼯣�");
		for(HashMap<Integer, Integer> r : rules)
			System.out.println("\t" + r);
	}
	
	public void calculate(int type) {
		int i = 0, j = 0;
		for(Vector<Integer> v : attributeValue)
			if(belongToCurrentClass(v))
				i++;
		for(int index : types.get(type))
			if(belongToCurrentClass(attributeValue.get(index)))
				j++;
		System.out.println();
		System.out.println("��ȫ����" + type + "��? " + (j == types.get(type).size()) + "\n��ȷ�ʣ� "); 
		System.out.println("\t" + j + " / " + i + " = " + j/(double)i);
	}
	
	public static void main(String[] args) {
		Foil foil = new Foil();
		foil.setAttributeNum(5);
		foil.setSpanOfAttribute(2);
		for(int i = 0; i < 18; i++) {
			Vector<Integer> v = new Vector<Integer>();
			for(int j = 0; j < 5; j++)
				if(i%5 == j)
					v.add(1);
				else
					v.add(0);
			foil.insertTrainingSet(i%5, v);
		}
		foil.foilTrainingSet(0);
		foil.summaru(true);
		foil.showRule();
		foil.calculate(0);
	}
}
