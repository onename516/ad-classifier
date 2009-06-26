package org.kde9.algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class Foil {
	/**
	 * 属性值的个数
	 */
	private int attributeNum = 0;
	
	private int maxRuleLength = 10;

	/**
	 * 每个属性值可能的取值个数。
	 * <strong>
	 * 注意：每个属性值的取值必须是从0开始的连续递增整数。
	 * </strong>
	 */
	private Vector<Integer> span;
	
	/**
	 * 每个TrainingSet中每个属性值的取值。
	 */
	private Vector<Vector<Integer>> attributeValue;
	
	/**
	 * 每个类别中包含的TrainingSet。
	 */
	private HashMap<Integer, HashSet<Integer>> types;
	
	/**
	 * 生成的规则集
	 */
	private Vector<HashMap<Integer, Integer>> rules;
	
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
					"规则的条件数的最大值必须为正整数！");
	}
	
	/**
	 * 设置每个属性值的取值范围
	 * <p>
	 * <strong>注意：每个属性值的取值必须是从0开始的连续递增整数。</strong>
	 * <p>
	 * 例如，当一个属性值的可能取值为0、1、2时，它的取值范围为3，即可能取值的个数。
	 * @throws ArrayIndexOutOfBoundsException
	 * 		当传进来的Vector的大小与属性值的个数不符时，可能抛出该异常。
	 * @throws NumberFormatException
	 * 		当属性值的大小不是正整数时，可能抛出该异常。
	 * @param span
	 * 		包含所有属性值范围的数组。
	 */
	public void setSpanOfAttribute(Vector<Integer> span) {
		if(span.size() != attributeNum)
			throw new ArrayIndexOutOfBoundsException(
					"Vector的大小与属性值的个数不符，您是否忘记调用setAttributeNum函数了？");
		for(int i : span)
			if(i > 0)
				this.span.add(i);
			else
				throw new NumberFormatException("属性值的大小必须是正整数！");
	}
	
	/**
	 * 设置每个属性值的取值范围为同一个值
	 * <p>
	 * <strong>注意：每个属性值的取值必须是从0开始的连续递增整数。</strong>
	 * <p>
	 * 例如，当一个属性值的可能取值为0、1、2时，它的取值范围为3，即可能取值的个数。
	 * @throws NumberFormatException
	 * 		当属性值的大小不是正整数时，可能抛出该异常。
	 * @param span
	 * 		包含所有属性值范围的数组。
	 */
	public void setSpanOfAttribute(int span) {
		if(span <= 0)
			throw new NumberFormatException("属性值的大小必须是正整数！");
		for(int i = 0; i < attributeNum; i++)
			this.span.add(span);
	}
	
	/**
	 * 设置属性值的个数
	 * @throws NumberFormatException
	 * 		当属性值的大小不是正整数时，可能抛出该异常。
	 * @param size
	 * 		属性值的个数，<strong>必须是正整数！</strong>
	 */
	public void setAttributeNum(int size) {
		if(size > 0)
			this.attributeNum = size;
		else
			throw new NumberFormatException("属性值的大小必须是正整数！");
	}
	
	/**
	 * 增加一个训练数据
	 * @throws ArrayIndexOutOfBoundsException
	 * 		当传进来的Vector的大小与属性值的个数不符时，可能抛出该异常。
	 * @throws NumberFormatException
	 * 		当属性值的大小不在所设定的范围内时，可能抛出该异常。
	 * @param type
	 * 		训练数据所属的类别
	 * @param values
	 * 		训练数据的所有属性值的取值
	 * @return
	 * 		增加是否成功
	 */
	public boolean insertTrainingSet(int type, Vector<Integer> values) {
		if(values.size() != attributeNum)
			throw new NumberFormatException("属性值的大小必须是正整数！");
		Vector<Integer> temp = new Vector<Integer>();
		for(int i = 0; i < values.size(); i++) {
			int value = values.get(i);
			int span = this.span.get(i);
			if(value < span && value >= 0)
				temp.add(value);
			else
				throw new NumberFormatException("属性值的取值和设定范围不符！");
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
	 * 清理所有的数据
	 * <p>
	 * 用于重新插入训练集前清除旧的数据。
	 */
	public void clear() {
		attributeNum = 0;
		span.clear();
		attributeValue.clear();
		types.clear();
		rules.clear();
	}
	
	/**
	 * 判断给定的数据是否属于刚刚训练的类别
	 * @param data
	 * 		要判断的数据的属性值的集合
	 * @return
	 * 		是否属于刚刚训练的类别，如果是返回true。
	 */
	public boolean belongToCurrentClass(Vector<Integer> data) {
		for(HashMap<Integer, Integer> rule : rules) {
			if(satisfyRule(data, rule))
				return true;
		}
		return false;
	}
	
	/**
	 * FOIL算法实现
	 * @param type
	 */
	public void foilTrainingSet(int type) {
		if(!types.containsKey(type))
			throw new ArrayIndexOutOfBoundsException(
					"没有这样的类别：" + type);
		initFoil();
		HashSet<Integer> pos = (HashSet<Integer>) types.get(type).clone();
		HashSet<Integer> neg = new HashSet<Integer>();
		for(int t : types.keySet())
			if(t != type)
				neg.addAll(types.get(t));
		while(pos.size() > 0) {
			HashSet<Integer> posx = (HashSet<Integer>) pos.clone();
			HashSet<Integer> negx = (HashSet<Integer>) neg.clone();
			HashMap<Integer, Integer> rule = getRule(posx, negx);
			rules.add(rule);
			adjustPos(pos, rule);
		}
	}
	
	/**
	 * FOIL算法内层循环
	 * @param pos
	 * @param neg
	 * @return
	 */
	private HashMap<Integer, Integer> getRule(HashSet<Integer> pos, HashSet<Integer> neg) {
		HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();
		// TODO
		return temp;
	}
	
	/**
	 * FOIL算法外层循环正元组调整
	 * @param pos
	 * @param rule
	 */
	private void adjustPos(HashSet<Integer> pos, HashMap<Integer, Integer> rule) {
		// TODO
	}
	
	/**
	 * 判断给定的数据是否满足给定的规则
	 * @param data
	 * 		给定的数据
	 * @param rule
	 * 		给定的规则
	 * @return
	 * 		是否满足，如满足返回true。
	 */
	private boolean satisfyRule(Vector<Integer> data, HashMap<Integer, Integer> rule) {
		for(int index : rule.keySet())
			if(!data.get(index).equals(rule.get(index)))
				return false;
		return true;
	}
	
	/**
	 * 初始化规则集
	 * <p>
	 * 将规则集置空，以便算法计算新的规则集。该方法在FOIL算法内部调用，用于初始化规则集。
	 */
	private void initFoil() {
		rules.clear();
	}
	
	/**
	 * ~~仅供测试使用~~
	 * @param detail
	 */
	public void summaru(boolean detail) {
		for (int type : types.keySet()) {
			System.out.println(type + ": " + types.get(type).size());
			if (detail)
				for (int i : types.get(type)) {
					for (int j : attributeValue.get(i))
						System.out.print("\t" + j);
					System.out.println();
				}
		}
	}
	
	public static void main(String[] args) {
		Foil foil = new Foil();
		foil.setAttributeNum(5);
		foil.setSpanOfAttribute(2);
		for(int i = 0; i < 5; i++) {
			Vector<Integer> v = new Vector<Integer>();
			for(int j = 0; j < 5; j++)
				if(i == j)
					v.add(1);
				else
					v.add(0);
			foil.insertTrainingSet(i, v);
		}
		foil.summaru(true);
	}
}
