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
	double CHIYIELD = 3;  //CHI算法域值
	int attributes;
	int instances;
	int finalAttributes = 0;
	double []finalKafang;
	
	PreProcess pProcess;
	
	public FeatureSelection(){
		pProcess = new PreProcess();
		pProcess.run(null);
		attributes = pProcess.getAttributes();
		instances = pProcess.getInstances();
		type = pProcess.getType();
		tempData = pProcess.getData();
		processedData = new Vector[instances];
		for(int i = 0; i < instances; i++)
			processedData[i] = new Vector<Integer>();
		result = new Vector<Integer>();
		valueSpan = new Vector<Integer>();
		finalKafang = new double[attributes];
		flag = new int[attributes];
	}
	
	public FeatureSelection(String adNames, String adData) {
		if(adNames != null && adData != null){
			pProcess = new PreProcess(adNames, adData);
			pProcess.run(null);
			attributes = pProcess.getAttributes();
			instances = pProcess.getInstances();
			type = pProcess.getType();
			tempData = pProcess.getData();
			processedData = new Vector[instances];
			for(int i = 0; i < instances; i++)
				processedData[i] = new Vector<Integer>();
			result = new Vector<Integer>();
			valueSpan = new Vector<Integer>();
			finalKafang = new double[attributes];
			flag = new int[attributes];
		}
	}
	
	public FeatureSelection(String adNames, String adData, String outfileName) {
		if(adNames != null && adData != null && outfileName != null){
			pProcess = new PreProcess(adNames, adData, outfileName);
			pProcess.run(null);
			attributes = pProcess.getAttributes();
			instances = pProcess.getInstances();
			type = pProcess.getType();
			tempData = pProcess.getData();
			processedData = new Vector[instances];
			for(int i = 0; i < instances; i++)
				processedData[i] = new Vector<Integer>();
			result = new Vector<Integer>();
			valueSpan = new Vector<Integer>();
			finalKafang = new double[attributes];
			flag = new int[attributes];
		}
	}
	
	public void TF(){   //特征频率算法Term Frequency
		result.clear();
		valueSpan.clear();
		for(int i = 0; i < instances; i++){
			processedData[i].clear();
		}
		int []frequency = new int[attributes];
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
		result.clear();
		valueSpan.clear();
		for(int i = 0; i < instances; i++){
			processedData[i].clear();
		}
		int[][] A = new int[attributes][];
		int[][] B = new int[attributes][];
		int[][] C = new int[attributes][];
		int[][] D = new int[attributes][];
		double[][]kafang = new double[attributes][];
		for(int i = 0; i < attributes; i++){
			kafang[i] = new double[2];
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
						A[j][1]++;
						B[j][0]++;
					}else if(tempData[i][j] == 0){
						C[j][1]++;
						D[j][0]++;
					}
				}
				P[1]++;
			}else if(type.get(i) == 0){
				for(int j = 0; j < attributes; j++){
					if(tempData[i][j] > 0){
						A[j][0]++;
						B[j][1]++;
					}else if(tempData[i][j] == 0){
						C[j][0]++;
						D[j][1]++;
					}
				}
				P[1]++;
			}
		}
		for(int j = 0; j < attributes; j++){
			for(int index = 0; index < 2; index++){
				int temp0 = (A[j][index]*D[j][index]-C[j][index]*B[j][index]);
				int temp1 = A[j][index] + C[j][index];
				int temp2 = B[j][index] + D[j][index];
				int temp3 = A[j][index] + B[j][index];
				int temp4 = C[j][index] + D[j][index];
				kafang[j][index] = instances*temp0*temp0/(temp1*temp2*temp3*temp4);
				finalKafang[j] = (double)(P[0]/instances)*kafang[j][0] + (double)(P[1]/instances)*kafang[j][1];
			}
		}
		for(int j = 0; j < attributes; j++){
			flag[j] = 0;
		}
		for(int i = 0; i < attributes; i++){
			if(finalKafang[i] > CHIYIELD){
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
	
	public Vector<Integer>[] processTestFile(String testFile){
		PreProcess pp = new PreProcess(testFile);
		if(result != null){
			pp.run(result);
		}
		return pp.getProcessedData();
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

	public int getAttributes() {
		return attributes;
	}

	/**
	 * @return the finalAttributes
	 */
	public int getFinalAttributes() {
		return finalAttributes;
	}

	/**
	 * @return the finalKafang
	 */
	public double[] getFinalKafang() {
		return finalKafang;
	}
	
	/**
	 * @param tfyield the tFYIELD to set
	 */
	public void setTFYIELD(int tfyield) {
		TFYIELD = tfyield;
	}

	/**
	 * @param chiyield the cHIYIELD to set
	 */
	public void setCHIYIELD(double chiyield) {
		CHIYIELD = chiyield;
	}

	public void run(int flag){
		if(flag == 0){
			TF();
		}else if(flag == 1){
			CHI();
		}
	}

	public static void main(String args[]){
		FeatureSelection fs = new FeatureSelection();
		fs.run(0);
		System.out.println(fs.finalAttributes);
		System.out.println(fs.result.size());
	}
}
