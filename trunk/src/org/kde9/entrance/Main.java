package org.kde9.entrance;

import java.util.Random;
import java.util.Vector;

import org.kde9.algorithm.Foil;
import org.kde9.algorithm.PRM;
import org.kde9.feature.FeatureSelection;

public class Main {
	public static void main(String[] args) {
		System.out.println("************������������************");
		
		FeatureSelection fs = new FeatureSelection();
		fs.run(0);
		
		System.out.println("************�����������************");
		
		int groupNum = 5;
		Vector<Integer>[] groups = new Vector[groupNum];
		Random random = new Random();
		for (int i = 0; i < groupNum; i++)
			groups[i] = new Vector<Integer>();
		for (int i = 0; i < fs.getInstances(); i++) {
			groups[random.nextInt(groupNum)].add(i);
		}

		System.out.println("*******leave one out �������*******");
		System.out.println("\n�����������ķ����������ݼ���Ϊ" + groupNum + "���飬ÿ��������ݳ�Ա���Ϊ");
		for(int i = 0; i < groupNum; i++) {
			System.out.print("size:" + groups[i].size() + " [");
			for(int j = 0; j < groups[i].size(); j++)
				System.out.print(groups[i].get(j) + " ");
			System.out.println("]");
		}

		System.out.println("\n��ʼ����leave one out���� ����");
		
		Foil foil = new Foil(null);
		int totalSum1 = 0, totalSum2 = 0, totalCorrect = 0;
		for (int i = 0; i < groupNum; i++) {
			System.out.print("\n����ʹ�� ");
			for(int k = 0; k < groupNum; k++)
				if(k != i)
					System.out.print(k + " ");
			System.out.println("��ѵ����������ʹ�õ� " + i + " ����в��� ����");
			foil.clear();
			foil.setIgnoreRate(0.05);
			foil.setLimit(1);
			foil.setWRate(0.5);
			foil.setMaxRuleLength(5);
			foil.setAttributeNum(fs.getFinalAttributes());
			foil.setSpanOfAttribute(fs.getValueSpan());
			for (int j = 0; j < groupNum; j++) {
				if(i == j)
					continue;
				for(int index : groups[j])
					foil.insertTrainingSet(
							fs.getType().get(index), (fs.getProcessedData())[index]);
			}
			System.out.println("������������������������");
			foil.foilTrainingSet(0);
			int sum1 = 0, sum2 = 0, correct = 0;
			for(int index : groups[i]) {
				if(fs.getType().get(index) == 0) 
					sum2++;
				if(foil.belongToCurrentClass((fs.getProcessedData())[index])) {
					sum1++;
					if(fs.getType().get(index) == 0) 
						correct++;
				}
			}
			totalSum1 += sum1;
			totalSum2 += sum2;
			totalCorrect += correct;
			System.out.println("��ȷ��[��ȷ��Ϊ�ǹ�������" + correct +
					"/��Ϊ�ǹ������" + sum1 +
					"]Ϊ\n\t" + correct/(double)sum1);
			System.out.println("������[��ȷ��Ϊ�ǹ�������" + correct +
					"/�ǹ������" + sum2 +
					"]Ϊ\n\t" + correct/(double)sum2);
		}
		System.out.println();
		System.out.println("����ȷ��Ϊ " + totalCorrect +"/" + totalSum1 + " = " + 
				totalCorrect/(double)totalSum1);
		System.out.println("�ܸ�����Ϊ " + totalCorrect +"/" + totalSum2 + " = " + 
				totalCorrect/(double)totalSum2);
	}
}
