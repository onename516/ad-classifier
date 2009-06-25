package org.kde9.preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
	
	String relation;
	LinkedHashMap<String, Vector<String>> attribute;
	Vector<String> realAttribute;
	Vector<String> data;
	
	
	public PreProcess(){		
		try {
			fos = new FileOutputStream(adOutFile);
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			attribute = new LinkedHashMap<String, Vector<String>>();
			realAttribute = new Vector<String>();
			data = new Vector<String>();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readFile(){
		try {
			//读name文件
			fis = new FileInputStream(adNames);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			
			String title = br.readLine();
			String temp;
			while((temp = br.readLine()) != null){
				String[] result = temp.toLowerCase().split(" ");
				if(result[result.length - 1].trim().equalsIgnoreCase("classes.")){
					relation = result[0].substring(0, result[0].length() - 1) + "-or-" + result[1];
					//System.out.println(relation);
				}
				if(result[result.length - 1].trim().equalsIgnoreCase("continuous.")){
					String t = result[0].substring(0, result[0].length() - 1);
					realAttribute.add(t);
				}
				if(result[0].equalsIgnoreCase("|")){
					int sum = Integer.parseInt(result[1]);
					String attr = null;
					Vector<String> tt = new Vector<String>();
					for(int i = 0; i < sum; i++){
						String line = br.readLine();
						String [] lineresult = line.split("\\u002A");   //*转义
						attr = lineresult[0];
						String [] reresult = lineresult[1].split(":");
						tt.add(reresult[0]);
					}
					attribute.put(attr, tt);
				}
			}
			//读data文件
			fis = new FileInputStream(adData);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);

			String temp2;
			while((temp2 = br.readLine()) != null){
				//System.out.println(temp2);
				data.add(temp2);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void outputFile(){
		try {
			bw.write("@relation " + relation + System.getProperty("line.separator"));
			for(int i = 0; i < realAttribute.size(); i++){
				bw.write("@attribute '" + realAttribute.get(i) + "' real" + System.getProperty("line.separator"));
			}
			bw.write("@attribute 'local' { 0, 1 }" + System.getProperty("line.separator"));
			for(String key : attribute.keySet()){
				for(int i = 0; i < attribute.get(key).size(); i++){
					bw.write("@attribute '" + key + "*" + attribute.get(key).get(i) + "' { 0, 1 }" + System.getProperty("line.separator"));
				}
			}
			bw.write("@attribute 'classes' { ad., nonad. }" + System.getProperty("line.separator"));
			bw.write("@data" + System.getProperty("line.separator"));
			//System.out.println(data.size());
			for(int i = 0; i < data.size(); i++){
				bw.write(data.get(i) + System.getProperty("line.separator"));
			}
			bw.write(System.getProperty("line.separator"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void main(String args[]){
		PreProcess pprocess = new PreProcess();
		pprocess.readFile();
		pprocess.outputFile();
		System.out.println("Data preprocess successfully!");
	}
}
