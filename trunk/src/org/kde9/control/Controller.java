package org.kde9.control;

import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTextArea;

import org.kde9.algorithm.Foil;
import org.kde9.algorithm.PRM;
import org.kde9.feature.FeatureSelection;
import org.kde9.view.training.TrainingPane;

public class Controller {
	private JTextArea area;
	private JButton start;
	private JButton stop;

	private FeatureSelection fs;
	private Foil foil;
	private Vector<Integer>[] groups;

	private boolean flag = true;

	private int featureType;
	private int classifyType;
	private int dataType;
	private int groupNum;
	private double ignoreRate;
	private double limit;
	private double wRate;
	private int maxRuleLength;
	private String file1;
	private String file2;
	private String file3;
	private int TFYIELD;
	private double CHIYIELD;

	public Controller(JTextArea area, JButton start, JButton stop) {
		this.area = area;
		this.start = start;
		this.stop = stop;
	}

	public void init() {
		this.groupNum = groupNum;
		fs = new FeatureSelection(file1, file2);
	}

	public void start() {
		flag = true;
		if (classifyType == 0)
			foil = new Foil(area);
		else if (classifyType == 1)
			foil = new PRM(area);
		fs.setTFYIELD(TFYIELD);
		fs.setCHIYIELD(CHIYIELD);
		fs.run(featureType);
		area.append("************�����������************\n");
		area.append("ѡȡ����������" + fs.getFinalAttributes() + "\n");
		new Thread() {
			public void run() {
				if (dataType == 0) {
					groups = new Vector[groupNum];
					Random random = new Random();
					for (int i = 0; i < groupNum; i++)
						groups[i] = new Vector<Integer>();
					for (int i = 0; i < fs.getInstances(); i++) {
						groups[random.nextInt(groupNum)].add(i);
					}

					area.append("*******leave one out �������*******\n");
					area.append("\n�����������ķ����������ݼ���Ϊ" + groupNum + "���飬ÿ��������ݳ�Ա���Ϊ\n");
					for (int i = 0; i < groupNum; i++) {
						area.append("size:" + groups[i].size() + " [");
						for (int j = 0; j < groups[i].size(); j++)
							area.append(groups[i].get(j) + " ");
						area.append("]\n");
					}
					int totalSum1 = 0, totalSum2 = 0, totalCorrect = 0;
					for (int i = 0; i < groupNum; i++) {
						if (!flag)
							break;
						area.append("\n����ʹ�� ");
						for (int k = 0; k < groupNum; k++)
							if (k != i)
								area.append(k + " ");
						area.append("��ѵ����������ʹ�õ� " + i + " ����в��� ����\n");
						foil.clear();
						foil.setIgnoreRate(ignoreRate);
						foil.setLimit(limit);
						foil.setWRate(wRate);
						foil.setMaxRuleLength(maxRuleLength);
						foil.setAttributeNum(fs.getFinalAttributes());
						foil.setSpanOfAttribute(fs.getValueSpan());
						for (int j = 0; j < groupNum; j++) {
							if (i == j)
								continue;
							for (int index : groups[j])
								foil.insertTrainingSet(fs.getType().get(index),
										(fs.getProcessedData())[index]);
						}
						foil.foilTrainingSet(0);
						int sum1 = 0, sum2 = 0, correct = 0;
						for (int index : groups[i]) {
							if (fs.getType().get(index) == 0)
								sum2++;
							if (foil.belongToCurrentClass((fs
									.getProcessedData())[index])) {
								sum1++;
								if (fs.getType().get(index) == 0)
									correct++;
							}
						}
						totalSum1 += sum1;
						totalSum2 += sum2;
						totalCorrect += correct;
						area.append("\n��ȷ��[��ȷ��Ϊ�ǹ�������" + correct + "/��Ϊ�ǹ������"
								+ sum1 + "]Ϊ\n\t" + correct / (double) sum1
								+ "\n");
						area.append("������[��ȷ��Ϊ�ǹ�������" + correct + "/�ǹ������"
								+ sum2 + "]Ϊ\n\t" + correct / (double) sum2
								+ "\n");
					}
					area.append("\n����ȷ��Ϊ " + totalCorrect + "/" + totalSum1
							+ " = " + totalCorrect / (double) totalSum1);
					area.append("\n�ܸ�����Ϊ " + totalCorrect + "/" + totalSum2
							+ " = " + totalCorrect / (double) totalSum2);
				} else if (dataType == 1) {
					foil.clear();
					foil.setIgnoreRate(ignoreRate);
					foil.setLimit(limit);
					foil.setWRate(wRate);
					foil.setMaxRuleLength(maxRuleLength);
					foil.setAttributeNum(fs.getFinalAttributes());
					foil.setSpanOfAttribute(fs.getValueSpan());
					for (int i = 0; i < fs.getInstances(); i++) {
						foil.insertTrainingSet(fs.getType().get(i),
								(fs.getProcessedData())[i]);
					}
					area.append("��ʼѵ�� ����\n");
					foil.foilTrainingSet(0);
					Vector<Integer>[] data = fs.processTestFile(file3);
					for(int i = 0; i < data.length; i++) {
						area.append("\n��" + i + "���������Ϊ\n[");
						for(int j = 0; j < data[i].size(); j++)
							area.append(data[i].get(j) + " ");
						area.append("]\n");
						area.append("�ò��������Ƿ�Ϊ��棺\n\t" + !foil.belongToCurrentClass(data[i]) + "\n");
					}
				}
				start.setEnabled(true);
				stop.setEnabled(false);
			}
		}.start();
	}

	public void stop() {
		this.flag = false;
		foil.setFlag(false);
	}

	public void setFeatureType(int featureType) {
		this.featureType = featureType;
	}

	public void setClassifyType(int classifyType) {
		this.classifyType = classifyType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}

	public void setIgnoreRate(double ignoreRate) {
		this.ignoreRate = ignoreRate;
	}

	public void setLimit(double limit) {
		this.limit = limit;
	}

	public void setWRate(double rate) {
		wRate = rate;
	}

	public void setMaxRuleLength(int maxRuleLength) {
		this.maxRuleLength = maxRuleLength;
	}

	public void setFile1(String file1) {
		this.file1 = file1;
	}

	public void setFile2(String file2) {
		this.file2 = file2;
	}

	public void setFile3(String file3) {
		this.file3 = file3;
	}

	public void setTFYIELD(int tfyield) {
		TFYIELD = tfyield;
	}

	public void setCHIYIELD(double chiyield) {
		CHIYIELD = chiyield;
	}
}
