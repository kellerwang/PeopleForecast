package cn.tongji.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataInput {
	
	public static final int AGESTRUCTURENUM = 22;

	public static Map<String, Double> readFileByLines2(String fileName) {
		Map<String, Double> hmTemp = new HashMap<String, Double>();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 0;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				System.out.println("line " + line + ": " + tempString);
				String[] temp = tempString.split(";");
				if (temp.length == 2) {
					String mStr = "男" + line;
					double mVal = Double.valueOf(temp[0]);
					String fStr = "女" + line;
					double fVal = Double.valueOf(temp[1]);
					hmTemp.put(mStr, mVal);
					hmTemp.put(fStr, fVal);
					System.out.println(mStr + ": " + mVal);
					System.out.println(fStr + ": " + fVal);
				}
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return hmTemp;
	}

	public static Map<String, Double> readFileByLines1(String fileName) {
		Map<String, Double> hmTemp = new HashMap<String, Double>();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 0;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				System.out.println("line " + line + ": " + tempString);
				String fStr = "女" + line;
				double fVal = Double.valueOf(tempString);
				hmTemp.put(fStr, fVal);
				System.out.println(fStr + ": " + fVal);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return hmTemp;
	}
}
