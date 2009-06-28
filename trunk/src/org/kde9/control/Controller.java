package org.kde9.control;

import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTextArea;

import org.kde9.algorithm.Foil;
import org.kde9.feature.FeatureSelection;
import org.kde9.view.training.TrainingPane;

public class Controller {
	private JTextArea area;
	private JButton start;
	private JButton stop;
	
	private FeatureSelection fs;
	private Foil foil;
	private Vector<Integer>[] groups;
	private int groupNum;
	
	private boolean flag = true;
	
	public Controller(JTextArea area, JButton start, JButton stop) {
		this.area = area;
		this.start = start;
		this.stop = stop;
		foil = new Foil();
	}
	
	public void init(String file1, String file2, int groupNum) {
		this.groupNum = groupNum;
//		fs = new FeatureSelection(file1, file2, null);
		fs = new FeatureSelection();
		
		area.append("************������������************\n");
		fs.run();
		area.append("************�����������************\n");
		
		groups = new Vector[groupNum];
		Random random = new Random();
		for (int i = 0; i < groupNum; i++)
			groups[i] = new Vector<Integer>();
		for (int i = 0; i < fs.getInstances(); i++) {
			groups[random.nextInt(groupNum)].add(i);
		}
		
		area.append("*******leave one out �������*******\n");
		area.append("\n�����������ķ����������ݼ���Ϊ" + groupNum + "���飬ÿ��������ݳ�Ա���Ϊ\n");
		for(int i = 0; i < groupNum; i++) {
			area.append("size:" + groups[i].size() + " [");
			for(int j = 0; j < groups[i].size(); j++)
				area.append(groups[i].get(j) + " ");
			area.append("]\n");
		}
	}
	
	public void start() {
		flag = true;
		new Thread() {
			public void run() {
				int totalSum1 = 0, totalSum2 = 0, totalCorrect = 0;
				for (int i = 0; i < groupNum; i++) {
					if(!flag)
						break;
					area.append("\n����ʹ�� ");
					for (int k = 0; k < groupNum; k++)
						if (k != i)
							area.append(k + " ");
					area.append("��ѵ����������ʹ�õ� " + i + " ����в��� ����\n");
					foil.clear();
					foil.setIgnoreRate(0.05);
					foil.setLimit(1);
					foil.setWRate(0.5);
					foil.setMaxRuleLength(5);
					foil.setAttributeNum(fs.getFinalAttributes());
					foil.setSpanOfAttribute(fs.getValueSpan());
					for (int j = 0; j < groupNum; j++) {
						if (i == j)
							continue;
						for (int index : groups[j])
							foil.insertTrainingSet(fs.getType().get(index), (fs
									.getProcessedData())[index]);
					}
					foil.foilTrainingSet(0);
					int sum1 = 0, sum2 = 0, correct = 0;
					for (int index : groups[i]) {
						if (fs.getType().get(index) == 0)
							sum2++;
						if (foil
								.belongToCurrentClass((fs.getProcessedData())[index])) {
							sum1++;
							if (fs.getType().get(index) == 0)
								correct++;
						}
					}
					totalSum1 += sum1;
					totalSum2 += sum2;
					totalCorrect += correct;
					area.append("��ȷ��[��ȷ��Ϊ�ǹ�������" + correct + "/��Ϊ�ǹ������" + sum1
							+ "]Ϊ\n\t" + correct / (double) sum1 + "\n");
					area.append("������[��ȷ��Ϊ�ǹ�������" + correct + "/�ǹ������" + sum2
							+ "]Ϊ\n\t" + correct / (double) sum2 + "\n");
				}
				area.append("\n����ȷ��Ϊ " + totalCorrect + "/" + totalSum1 + " = "
						+ totalCorrect / (double) totalSum1);
				area.append("\n�ܸ�����Ϊ " + totalCorrect + "/" + totalSum2 + " = "
						+ totalCorrect / (double) totalSum2);
				start.setEnabled(true);
				stop.setEnabled(false);
			}
		}.start();
	}
	
	public void stop() {
		this.flag = false;
		foil.setFlag(false);
	}
}
