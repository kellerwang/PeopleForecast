package cn.tongji.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.tongji.data.DataInput;

public class PopulationForecast {

	private final double PERCENT = 100;
	private Map<String, Double> hmPopulation = new HashMap<String, Double>();
	private Map<String, Double> hmFertilityRule = new HashMap<String, Double>();
	private Map<String, Double> hmDeathDate = new HashMap<String, Double>();
	private Map<Integer, Double> hmAgeStructure = null;
	private Map<String, Double> hmAgeStructureGender = null;
	private double sexRatio = 0;
	private double policy = 0;
	private double deathNum = 0;

	public double getDeathNum() {
		return deathNum;
	}

	public PopulationForecast(double sexRatio, double policy,
			String fileNamePeople, String fileNameDeathDate,
			String fileNameFertilityRule) {
		this.sexRatio = sexRatio;
		this.policy = policy;
		hmPopulation = DataInput.readFileByLines2(fileNamePeople);
		hmDeathDate = DataInput.readFileByLines2(fileNameDeathDate);
		hmFertilityRule = DataInput.readFileByLines1(fileNameFertilityRule);
		setAgeStructure();
		setAgeStructureGender();
	}

	public Map<String, Double> getHmPopulation() {
		return hmPopulation;
	}

	public void setHmPopulation(Map<String, Double> hmPopulation) {
		this.hmPopulation = hmPopulation;
	}

	public int getIntegerFormString(String str) {
		return Integer.valueOf(str.substring(1));
	}

	public void setAgeStructure() {
		hmAgeStructure = new HashMap<Integer, Double>();
		for (int k = 0; k < DataInput.AGESTRUCTURENUM; k++) {
			double temp = hmPopulation.get("男" + k) + hmPopulation.get("女" + k);
			temp = temp / getPopulationAll();
			hmAgeStructure.put(k, temp);
		}
	}

	public void setAgeStructureGender() {
		hmAgeStructureGender = new HashMap<String, Double>();
		for (int k = 0; k < DataInput.AGESTRUCTURENUM; k++) {
			double temp = hmPopulation.get("男" + k) / getPopulationMale();
			hmAgeStructureGender.put("男" + k, temp);
			temp = hmPopulation.get("女" + k) / getPopulationFemale();
			hmAgeStructureGender.put("女" + k, temp);
		}
	}

	// 65岁以上
	public double getAgingOfPopulation() {
		double sum = 0;
		for (int i = 14; i < DataInput.AGESTRUCTURENUM; i++) {
			sum += getAgeStructure(i);
		}
		return sum;
	}

	public double getAgeStructure(int ageStructureIndex) {
		return hmAgeStructure.get(ageStructureIndex);
	}

	public double getAgeStructureGender(boolean gender, int ageStructureIndex) {
		double temp = 0;
		if (gender) {
			temp = hmAgeStructureGender.get("男" + ageStructureIndex);
		} else {
			temp = hmAgeStructureGender.get("女" + ageStructureIndex);
		}
		return temp;
	}

	public double getPopulationMale() {
		double result = 0;
		Iterator iter = hmPopulation.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Double> entry = (Entry<String, Double>) iter.next();
			String key = entry.getKey();
			Double val = entry.getValue();
			if (key.startsWith("男")) {
				result += val;
			}
		}
		return result;
	}

	public double getPopulationFemale() {
		double result = 0;
		Iterator iter = hmPopulation.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Double> entry = (Entry<String, Double>) iter.next();
			String key = entry.getKey();
			Double val = entry.getValue();
			if (key.startsWith("女")) {
				result += val;
			}
		}
		return result;
	}

	public double getPopulationAll() {
		double result = 0;
		Iterator iter = hmPopulation.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Double> entry = (Entry<String, Double>) iter.next();
			String key = entry.getKey();
			Double val = entry.getValue();
			result += val;
		}
		return result;
	}

	public Map<String, Double> getNextYearPopulation() {
		deathNum = 0;
		Map<String, Double> hmTempPopulation = new HashMap<String, Double>();
		String tempGende = "女";
		Iterator iter = hmPopulation.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Double> entry = (Entry<String, Double>) iter.next();
			String key = entry.getKey();
			Double val = entry.getValue();
			double newVal = 0;
			int index = getIntegerFormString(key);
			boolean gender = true;
			if (index == 0) {
				if (key.startsWith(tempGende)) {
					gender = false;
				}
				newVal = getBirthNumberGender(gender);
				hmTempPopulation.put(key, newVal);
			} else {
				if (index == 1) {
					if (key.startsWith(tempGende)) {
						gender = false;
						newVal = val
								+ hmPopulation.get(tempGende + 0)
								* (1 - (hmDeathDate.get(tempGende + 0) / PERCENT))
								- getF(index) - getFD(index);
					} else {
						newVal = val + hmPopulation.get("男" + 0)
								* (1 - (hmDeathDate.get("男" + 0) / PERCENT))
								- getM(index) - getMD(index);
					}
				} else {
					if (index == (DataInput.AGESTRUCTURENUM - 1)) {
						if (key.startsWith(tempGende)) {
							gender = false;
							newVal = val + getF(index - 1) - getFD(index);
						} else {
							newVal = val + getM(index - 1) - getMD(index);
						}
					} else {
						if (key.startsWith(tempGende)) {
							gender = false;
							newVal = val + getF(index - 1) - getF(index)
									- getFD(index);
						} else {
							newVal = val + getM(index - 1) - getM(index)
									- getMD(index);
						}
					}
				}
				hmTempPopulation.put(key, newVal);
			}
		}
		return hmTempPopulation;
	}

	public double getF(int n) {
		String temp = "女" + n;
		return hmPopulation.get(temp) / 5
				* (1 - (hmDeathDate.get(temp) / PERCENT));
	}

	public double getFD(int n) {
		String temp = "女" + n;
		double dTemp = hmDeathDate.get(temp) * hmPopulation.get(temp) / 5;
		deathNum += dTemp;
		return dTemp;

	}

	public double getM(int n) {
		String temp = "男" + n;
		return hmPopulation.get(temp) / 5
				* (1 - (hmDeathDate.get(temp) / PERCENT));
	}

	public double getMD(int n) {
		String temp = "男" + n;
		double dTemp = hmDeathDate.get(temp) * hmPopulation.get(temp) / 5;
		deathNum += dTemp;
		return dTemp;
	}

	public double getSexRatio() {
		return getPopulationMale() / getPopulationFemale();
	}

	public double getBirthNumber() {
		return hmPopulation.get("男0") + hmPopulation.get("女0");
	}

	public double getBirthNumberGender(boolean gender) {
		String tempGende = "女";
		double birthBumber = 0;
		double sum = 0;
		Iterator iter = hmPopulation.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Double> entry = (Entry<String, Double>) iter.next();
			String key = entry.getKey();
			if (key.startsWith(tempGende)) {
				Double val = entry.getValue();
				sum += val * hmFertilityRule.get(key);
			}
		}
		double tempSexRatio = 1 / (sexRatio + 1);
		String temp0DeathDate = "女0";
		if (gender) {
			tempSexRatio = 1 / ((1 / sexRatio) + 1);
			temp0DeathDate = "男0";
		}
		birthBumber = sum / 5 * tempSexRatio * policy
				* (1 - hmDeathDate.get(temp0DeathDate));
		deathNum += sum / 5 * tempSexRatio * policy
				* hmDeathDate.get(temp0DeathDate);
		return birthBumber;
	}
}
