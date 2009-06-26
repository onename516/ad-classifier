package org.kde9.feature;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

import org.kde9.preprocess.PreProcess;

public class FeatureSelection {
	Vector<String> data;
	String [][] tempData;
	Vector<String>[] processedData;
	int []flag;
	int n;      //离散化区间大小
	double []min = {999999, 999999, 999999};
	double []max = {0, 0, 0};
	double []dx;
	int TFYIELD = 80;   //TF算法域值
	
	public FeatureSelection() {
		n = 8;
		PreProcess pProcess = new PreProcess();
		pProcess.run();
		data = new Vector<String>();
		data = pProcess.getData();
		tempData = new String[data.size()][];
		processedData = new Vector[data.size()];
		for(int i = 0; i < data.size(); i++)
			processedData[i] = new Vector<String>();
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
				for(int j = 0; j < tempData.length; j++){
					processedData[j].add(tempData[j][i]);
				}
			}
		}
		for(int i = 0; i < tempData.length; i++)
			processedData[i].add(tempData[i][tempData[0].length - 1]);
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
		System.out.println(fs.data.size());
	}
}
