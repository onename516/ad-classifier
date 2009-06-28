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
		area.append("************数据载入完成************\n");
		area.append("选取的特征数量" + fs.getFinalAttributes() + "\n");
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

					area.append("*******leave one out 分组完成*******\n");
					area.append("\n采用随机分组的方法，将数据集分为" + groupNum + "个组，每个组的数据成员序号为\n");
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
						area.append("\n正在使用 ");
						for (int k = 0; k < groupNum; k++)
							if (k != i)
								area.append(k + " ");
						area.append("组训练分类器，使用第 " + i + " 组进行测试 ……\n");
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
						area.append("\n正确率[正确判为非广告的数量" + correct + "/判为非广告总数"
								+ sum1 + "]为\n\t" + correct / (double) sum1
								+ "\n");
						area.append("覆盖率[正确判为非广告的数量" + correct + "/非广告总数"
								+ sum2 + "]为\n\t" + correct / (double) sum2
								+ "\n");
					}
					area.append("\n总正确率为 " + totalCorrect + "/" + totalSum1
							+ " = " + totalCorrect / (double) totalSum1);
					area.append("\n总覆盖率为 " + totalCorrect + "/" + totalSum2
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
					area.append("开始训练 ……\n");
					foil.foilTrainingSet(0);
					Vector<Integer>[] data = fs.processTestFile(file3);
					for(int i = 0; i < data.length; i++) {
						area.append("\n第" + i + "组测试数据为\n[");
						for(int j = 0; j < data[i].size(); j++)
							area.append(data[i].get(j) + " ");
						area.append("]\n");
						area.append("该测试数据是否为广告：\n\t" + !foil.belongToCurrentClass(data[i]) + "\n");
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
