package cn.tongji.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.tongji.data.DataInput;

public class MyTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int beginYear = 2010;
		double sexRatio = 105 / 100;
		double policy = 1.48;
		// double policy = 2;
		String fileNamePeople = "people.csv";
		String fileNameDeathDate = "death.csv";
		String fileNameFertilityRule = "fertility.csv";
		String fileNameOutPopulation = "outPopulation.csv";
		String fileNameOutOlding = "outOlding.csv";
		String fileNameOutProportion = "outProportion.csv";
		FileWriter fwPopulation, fwOlding, fwProportion;
		fwPopulation = new FileWriter(fileNameOutPopulation, true);
		fwOlding = new FileWriter(fileNameOutOlding, true);
		fwProportion = new FileWriter(fileNameOutProportion, true);
		BufferedWriter bwPopulation = new BufferedWriter(fwPopulation);
		BufferedWriter bwOlding = new BufferedWriter(fwOlding);
		BufferedWriter bwProportion = new BufferedWriter(fwProportion);
		int yearForecastNum = 40;
		PopulationForecast pf = new PopulationForecast(sexRatio, policy,
				fileNamePeople, fileNameDeathDate, fileNameFertilityRule);
		for (int i = 0; i < yearForecastNum; i++) {
			pf.setHmPopulation(pf.getNextYearPopulation());
			pf.setAgeStructure();
			pf.setAgeStructureGender();
			String nowYear = String.valueOf((beginYear + i + 1));
			System.out.println(nowYear + "," + pf.getPopulationAll());
			bwPopulation.write(nowYear + "," + pf.getPopulationAll());
			bwPopulation.newLine();
			bwOlding.write(nowYear + "," + pf.getAgingOfPopulation());
			bwOlding.newLine();
			bwProportion.write(nowYear);
			bwProportion.newLine();
			if ((beginYear + i + 1) % 10 == 0) {
				for (int k = 0; k < DataInput.AGESTRUCTURENUM; k++) {
					bwProportion.write(k + ","
							+ pf.getAgeStructureGender(true, k) + ","
							+ pf.getAgeStructureGender(false, k));
					bwProportion.newLine();
				}
			}
		}
		bwPopulation.close();
		fwPopulation.close();
		bwOlding.close();
		fwOlding.close();
		bwProportion.close();
		fwProportion.close();
	}
}
