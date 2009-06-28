package org.kde9.preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class PreProcess {
	
	FileInputStream fis;
	InputStreamReader isr;
	BufferedReader br;
	FileOutputStream fos;
	OutputStreamWriter osw;
	BufferedWriter bw;
	
	String adNames = "./data/ad.names";
	String adData = "./data/ad.data";
	String adOutFile = "./data/ad2.arff";
	String adTest;
	
	String relation;
	Vector<String> attribute;
	Vector<String> dataString;
	Vector<Integer> type;
	Vector<Integer>[] processedData;
	int attributes;
	int instances;
	int n;   //离散化区间
	int [][]data;
	int runType;
	
	public PreProcess(){
		try {
			runType = 0;
			fos = new FileOutputStream(adOutFile);
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			attribute = new Vector<String>();
			dataString = new Vector<String>();
			type = new Vector<Integer>();
			n = 7;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PreProcess(String testFile){
		runType = 1;
		this.adTest = testFile;
		attribute = new Vector<String>();
		dataString = new Vector<String>();
		type = new Vector<Integer>();
		n = 7;
	}
	
	public PreProcess(String adNames, String adData){
		this.adNames = adNames;
		this.adData = adData;
		runType = 2;
		attribute = new Vector<String>();
		dataString = new Vector<String>();
		type = new Vector<Integer>();
		n = 7;
	}

	public PreProcess(String adNames, String adData, String outfileName){
		this.adNames = adNames;
		this.adData = adData;
		this.adOutFile = outfileName;
		runType = 3;
		try {
			fos = new FileOutputStream(adOutFile);
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			attribute = new Vector<String>();
			dataString = new Vector<String>();
			type = new Vector<Integer>();
			n = 7;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void readFile(){
		try {
			//读name文件
			fis = new FileInputStream(adNames);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			
			String title = br.readLine();
			String temp;
			while((temp = br.readLine()) != null){
				if(temp.endsWith("classes.")){
					String []t = temp.toLowerCase().split(" ");
					relation = t[0].substring(0, t[0].length() - 1) + "-or-" + t[1];
				}else if(!temp.startsWith("|") && temp.trim().length() != 0){
					String[] result = temp.toLowerCase().split(":");
					attribute.add(result[0]);
				}
			}
			attributes = attribute.size();
			//读data文件
			fis = new FileInputStream(adData);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			String temp2;
			int index = 0;
			while((temp2 = br.readLine()) != null){
				//System.out.println(temp2);
				dataString.add(temp2);
				if(temp2.trim().endsWith(",ad.")){
					type.add(1);
				}else if(temp2.trim().endsWith("nonad.")){
					type.add(0);
				}
			}
			instances = dataString.size();
			
			double []min = {999999, 999999, 999999};
			double []max = {0, 0, 0};
			double []dx = new double[3];
			
			String [][]tempData = new String[instances][];
			data = new int[instances][];
			for(int i = 0; i < instances; i++){
				tempData[i] = new String[attributes];
				data[i] = new int[attributes];
			}
			for(int i = 0; i < instances; i++){
				String t = dataString.get(i);
				String []r = t.split(",");
				for(int j = 0; j < attributes; j++){
					if(r[j].trim().equalsIgnoreCase("?")){
						tempData[i][j] = "0";
					}else{
						tempData[i][j] = r[j];
					}
				}
				for(int j = 0; j < 3; j++){
					if(Double.parseDouble(tempData[i][j]) < min[j])
						min[j] = Double.parseDouble(tempData[i][j]);
					if(Double.parseDouble(tempData[i][j]) > max[j])
						max[j] = Double.parseDouble(tempData[i][j]);
				}
			}
			for(int i = 0; i < 3; i++)
				dx[i] = (max[i] - min[i])/n;
			for(int i = 0; i < instances; i++){
				for(int j = 0; j < 3; j++){
					int h = (int)(Double.parseDouble(tempData[i][j])/dx[j]);
					tempData[i][j] = String.valueOf(h);
				}
			}
			for(int i = 0; i < instances; i++){
				for(int j = 0; j < attributes; j++){
					data[i][j] = Integer.valueOf(tempData[i][j]);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void outputFile(){
		try {
			bw.write("@relation " + relation + System.getProperty("line.separator"));
			for(int i = 0; i < 3; i++){
				bw.write("@attribute '" + attribute.get(i) + "' real" + System.getProperty("line.separator"));
			}
			for(int i = 3; i < attribute.size(); i++){
				bw.write("@attribute '" + attribute.get(i) + "' { 0, 1 }" + System.getProperty("line.separator"));
			}
			bw.write("@attribute 'classes' { ad., nonad. }" + System.getProperty("line.separator"));
			bw.write("@data" + System.getProperty("line.separator"));
			//System.out.println(data.size());
			for(int i = 0; i < dataString.size(); i++){
				bw.write(dataString.get(i) + System.getProperty("line.separator"));
			}
			bw.write(System.getProperty("line.separator"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void readTestFile(Vector<Integer> flag){
		try {
			//读data文件
			fis = new FileInputStream(adTest);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			String temp;
			int index = 0;
			while((temp = br.readLine()) != null){
				if(!temp.trim().startsWith("@") && temp.trim().length() != 0)
					dataString.add(temp);
				else if(temp.trim().startsWith("@"))
					index++;
			}
			instances = dataString.size();
			attributes = index - 3;
			
			double []min = {999999, 999999, 999999};
			double []max = {0, 0, 0};
			double []dx = new double[3];
			
			String [][]tempData = new String[instances][];
			data = new int[instances][];
			for(int i = 0; i < instances; i++){
				tempData[i] = new String[attributes];
				data[i] = new int[attributes];
			}
			processedData = new Vector[instances];
			for(int i = 0; i < instances; i++)
				processedData[i] = new Vector<Integer>();
			
			for(int i = 0; i < instances; i++){
				String t = dataString.get(i);
				String []r = t.split(",");
				for(int j = 0; j < attributes; j++){
					if(r[j].trim().equalsIgnoreCase("?")){
						tempData[i][j] = "0";
					}else{
						tempData[i][j] = r[j];
					}
				}
				for(int j = 0; j < 3; j++){
					if(Double.parseDouble(tempData[i][j]) < min[j])
						min[j] = Double.parseDouble(tempData[i][j]);
					if(Double.parseDouble(tempData[i][j]) > max[j])
						max[j] = Double.parseDouble(tempData[i][j]);
				}
			}
			for(int i = 0; i < 3; i++)
				dx[i] = (max[i] - min[i])/n;
			for(int i = 0; i < instances; i++){
				for(int j = 0; j < 3; j++){
					int h = (int)(Double.parseDouble(tempData[i][j])/dx[j]);
					tempData[i][j] = String.valueOf(h);
				}
			}
			for(int i = 0; i < instances; i++){
				for(int j = 0; j < attributes; j++){
					data[i][j] = Integer.valueOf(tempData[i][j]);
				}
			}
			for(int i  = 0; i < flag.size(); i++){
				for(int j = 0; j < instances; j++){
					processedData[j].add(data[j][flag.get(i)]);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(Vector<Integer> flag){
		if(runType == 0 || runType == 3){
			readFile();
			outputFile();
		}else if(runType == 1){
			if(flag != null)
				readTestFile(flag);
		}else if(runType == 2){
			readFile();
		}
	}
	
	/**
	 * @return the attribute
	 */
	public Vector<String> getAttribute() {
		return attribute;
	}

	/**
	 * @return the type
	 */
	public Vector<Integer> getType() {
		return type;
	}

	/**
	 * @return the attributes
	 */
	public int getAttributes() {
		return attributes;
	}

	/**
	 * @return the instances
	 */
	public int getInstances() {
		return instances;
	}

	/**
	 * @return the data
	 */
	public int[][] getData() {
		return data;
	}

	/**
	 * @return the processedData
	 */
	public Vector<Integer>[] getProcessedData() {
		return processedData;
	}

	public static void main(String args[]){
		PreProcess pprocess = new PreProcess();
		pprocess.run(null);
		System.out.println(pprocess.attribute.size());
		System.out.println(pprocess.dataString.size());
		System.out.println("Data preprocess successfully!");
	}
}
