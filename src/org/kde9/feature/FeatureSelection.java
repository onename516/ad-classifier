package org.kde9.feature;

import java.util.Vector;
import org.kde9.preprocess.PreProcess;

public class FeatureSelection {
	int [][] tempData;
	Vector<Integer>[] processedData;
	Vector<Integer> type;
	Vector<Integer> result;
	Vector<Integer> valueSpan;

	int []flag;
	int TFYIELD = 100;   //TF算法域值
	int attributes;
	int instances;
	int finalAttributes = 0;
	
	public FeatureSelection() {
		PreProcess pProcess = new PreProcess();
		pProcess.run();
		attributes = pProcess.getAttributes();
		instances = pProcess.getInstances();
		type = pProcess.getType();
		tempData = pProcess.getData();
		processedData = new Vector[instances];
		for(int i = 0; i < instances; i++)
			processedData[i] = new Vector<Integer>();
		result = new Vector<Integer>();
		valueSpan = new Vector<Integer>();
	}
	
	public void TF(){   //特征频率算法Term Frequency
		int []frequency = new int[attributes];
		flag = new int[attributes];
		for(int j = 0; j < attributes; j++){
			frequency[j] = 0;
			flag[j] = 0;
		}
		for(int j = 0; j < attributes; j++){
			for(int i = 0; i < instances; i++){
				if(tempData[i][j] > 0)
					frequency[j]++;
			}
		}
		for(int i = 0; i < attributes; i++){
			if(frequency[i] > TFYIELD){
				flag[i] = 1;
			}	
		}
		for(int i = 0; i < attributes; i++){
			if(flag[i] == 1){
				result.add(i);
				if(i == 0 || i == 1 || i == 2){
					valueSpan.add(8);
				}else{
					valueSpan.add(2);
				}
				for(int j = 0; j < instances; j++){
					processedData[j].add(tempData[j][i]);
				}
			}
		}
		finalAttributes = processedData[0].size();
	}
	
	public void CHI(){   //卡方统计量
		int[][] A = new int[attributes][];
		int[][] B = new int[attributes][];
		int[][] C = new int[attributes][];
		int[][] D = new int[attributes][];
		int[][]kafang = new int[attributes][];
		for(int i = 0; i < attributes; i++){
			kafang[i] = new int[2];
			A[i] = new int[2];
			B[i] = new int[2];
			C[i] = new int[2];
			D[i] = new int[2];
			for(int j = 0; j< 2; j++){
				A[i][j] = 0;
				B[i][j] = 0;
				C[i][j] = 0;
				D[i][j] = 0;
			}
		}
		int []P = new int[2];
		P[0] = 0;
		P[1] = 0;
		for(int i = 0; i < instances; i++){
			if(type.get(i) == 1){  //广告
				for(int j = 0; j < attributes; j++){
					if(tempData[i][j] > 0){
						A[j][0]++;
						B[j][1]++;
					}else if(tempData[i][j] == 0){
						C[j][0]++;
						D[j][1]++;
					}
				}
				P[0]++;
			}else if(type.get(i) == 0){
				for(int j = 0; j < attributes; j++){
					if(tempData[i][j] > 0){
						A[j][1]++;
						B[j][0]++;
					}else if(tempData[i][j] == 0){
						C[j][1]++;
						D[j][0]++;
					}
				}
				P[1]++;
			}
		}
		
	}
	
	/**
	 * @return the result
	 */
	public Vector<Integer> getResult() {
		return result;
	}
	
	/**
	 * @return the valueSpan
	 */
	public Vector<Integer> getValueSpan() {
		return valueSpan;
	}

	/**
	 * @return the processedData
	 */
	public Vector<Integer>[] getProcessedData() {
		return processedData;
	}

	/**
	 * @return the type
	 */
	public Vector<Integer> getType() {
		return type;
	}

	/**
	 * @return the instances
	 */
	public int getInstances() {
		return instances;
	}

	/**
	 * @return the finalAttributes
	 */
	public int getFinalAttributes() {
		return finalAttributes;
	}

	public static void main(String args[]){
		FeatureSelection fs = new FeatureSelection();
		fs.TF();
		System.out.println(fs.finalAttributes);
		System.out.println(fs.result.size());
	}
}
