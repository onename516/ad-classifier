package org.kde9.feature;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

import org.kde9.preprocess.PreProcess;

public class FeatureSelection {
	Vector<String> data;
	String [][] tempData;
	Vector<Integer>[] processedData;
	Vector<Integer> type;
	Vector<Integer> result;
	Vector<Integer> valueSpan;

	int []flag;
	int n;      //离散化区间大小
	double []min = {999999, 999999, 999999};
	double []max = {0, 0, 0};
	double []dx;
	int TFYIELD = 50;   //TF算法域值
	
	public FeatureSelection() {
		n = 8;
		PreProcess pProcess = new PreProcess();
		pProcess.run();
		data = new Vector<String>();
		data = pProcess.getData();
		tempData = new String[data.size()][];
		processedData = new Vector[data.size()];
		for(int i = 0; i < data.size(); i++)
			processedData[i] = new Vector<Integer>();
		result = new Vector<Integer>();
		valueSpan = new Vector<Integer>();
		type = new Vector<Integer>();
		dx = new double[3];
	}
	
	public void dataConversion(){
		for(int i = 0; i < data.size(); i++){
			String temp = data.get(i);
			tempData[i] = temp.split(",");
			for(int j = 0; j < 3; j++){
				if(tempData[i][j].trim().equalsIgnoreCase("?")){
					tempData[i][j] = "0";
				}
				if(Double.parseDouble(tempData[i][j]) < min[j])
					min[j] = Double.parseDouble(tempData[i][j]);
				if(Double.parseDouble(tempData[i][j]) > max[j])
					max[j] = Double.parseDouble(tempData[i][j]);
			}
			if(tempData[i][3].trim().equalsIgnoreCase("?")){
				tempData[i][3] = "0";
			}
		}
		for(int i = 0; i < 3; i++)
			dx[i] = (max[i] - min[i])/n;
		for(int i = 0; i < tempData.length; i++){
			for(int j = 0; j < 3; j++){
				int temp = (int)(Double.parseDouble(tempData[i][j])/dx[j]);
				tempData[i][j] = String.valueOf(temp);
			}
		}
	}
	
	public void TF(){   //特征频率算法Term Frequency
		int []frequency = new int[tempData[0].length];
		flag = new int[tempData[0].length];
		for(int j = 0; j < tempData[0].length; j++){
			frequency[j] = 0;
			flag[j] = 0;
		}
		for(int j = 0; j < tempData[0].length - 1; j++){
			for(int i = 0; i < tempData.length; i++){
				if(Integer.valueOf(tempData[i][j]) > 0)
					frequency[j]++;
			}
		}
		for(int i = 0; i < frequency.length; i++){
			if(frequency[i] > TFYIELD){
				flag[i] = 1;
			}	
		}
		for(int i = 0; i < flag.length - 1; i++){
			if(flag[i] == 1){
				result.add(i);
				if(i == 0 || i == 1 || i == 2){
					valueSpan.add(8);
				}else{
					valueSpan.add(2);
				}
				for(int j = 0; j < tempData.length; j++){
					processedData[j].add(Integer.valueOf(tempData[j][i]));
				}
			}
		}
		for(int i = 0; i < tempData.length; i++){
			if(tempData[i][tempData[0].length - 1].trim().equalsIgnoreCase("ad."))
				type.add(1);
			else if(tempData[i][tempData[0].length - 1].trim().equalsIgnoreCase("nonad."))
				type.add(0);
			//processedData[i].add(tempData[i][tempData[0].length - 1]);
		}
	}
	
	public void CHI(){   //卡方统计量
		int[][] A = new int[tempData[0].length - 1][];
		int[][] B = new int[tempData[0].length - 1][];
		int[][] C = new int[tempData[0].length - 1][];
		int[][] D = new int[tempData[0].length - 1][];
		int[][]kafang = new int[tempData[0].length - 1][];
		for(int i = 0; i < tempData[0].length - 1; i++){
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
		for(int i = 0; i < tempData.length; i++){
			if(tempData[i][tempData[i].length -1].trim().equalsIgnoreCase("ad.")){
				for(int j = 0; j < tempData[0].length - 1; j++){
					if(Integer.valueOf(tempData[i][j]) > 0){
						A[j][0]++;
						B[j][1]++;
					}else if(Integer.valueOf(tempData[i][j]) == 0){
						C[j][0]++;
						D[j][1]++;
					}
				}
				P[0]++;
			}else if(tempData[i][tempData[i].length -1].trim().equalsIgnoreCase("nonad.")){
				for(int j = 0; j < tempData[0].length - 1; j++){
					if(Integer.valueOf(tempData[i][j]) > 0){
						A[j][1]++;
						B[j][0]++;
					}else if(Integer.valueOf(tempData[i][j]) == 0){
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
	 * @return the tempData
	 */
	public String[][] getTempData() {
		return tempData;
	}

	public static void main(String args[]){
		FeatureSelection fs = new FeatureSelection();
		fs.dataConversion();
		fs.TF();
//		FileOutputStream fos;
//		OutputStreamWriter osw;
//		BufferedWriter bw;
//		try {
//			fos = new FileOutputStream("test.txt");
//			osw = new OutputStreamWriter(fos);
//			bw = new BufferedWriter(osw);
//			for(int i  = 0; i < fs.tempData.length; i++){
//				//if(fs.tempData[i].length != 1559)
//					//System.out.println(fs.tempData[i].length);
//				for(int j = 0; j < fs.tempData[i].length; j++){
////					if(fs.tempData[i][j].trim().equalsIgnoreCase("\\u003F"))
////						System.out.print(fs.tempData[i][j] + " ");
//					bw.write(fs.tempData[i][j] + " ");
//				}
//				//System.out.println();
//				bw.write(System.getProperty("line.separator"));
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
		//System.out.println(fs.tempData[0][fs.tempData[0].length - 1]);
//		for(int i = 0; i < 3; i++){
//			System.out.println(fs.dx[i]);
//			//System.out.println(fs.max[i]);
//		}
		System.out.println(fs.processedData[0].size());
		System.out.println(fs.result.size());
		System.out.println(fs.data.size());
	}
}
