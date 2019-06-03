package com.ubiquisoft.evaluation.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Car {

	private String year;
	private String make;
	private String model;

	private List<Part> parts;

	public Map<PartType, Integer> getMissingPartsMap() {
		/*
		 * Return map of the part types missing.
		 *
		 * Each car requires one of each of the following types:
		 *      ENGINE, ELECTRICAL, FUEL_FILTER, OIL_FILTER
		 * and four of the type: TIRE
		 *
		 * Example: a car only missing three of the four tires should return a map like this:
		 *
		 *      {
		 *          "TIRE": 3
		 *      }
		 */

		Map<PartType, Integer> missingPartsMap = new HashMap<>();

		if(parts != null){

			Integer tireCount = getPartTypeCount(PartType.TIRE);
			Integer oilFilterCount = getPartTypeCount(PartType.OIL_FILTER);
			Integer electricalCount = getPartTypeCount(PartType.ELECTRICAL);
			Integer engineCount = getPartTypeCount(PartType.ENGINE);
			Integer fuelFilterCount = getPartTypeCount(PartType.FUEL_FILTER);


			if(tireCount < 4) {
				missingPartsMap.put(PartType.TIRE, tireCount);
			}

			if(oilFilterCount < 1) {
				missingPartsMap.put(PartType.OIL_FILTER, oilFilterCount);
			}

			if(electricalCount < 1) {
				missingPartsMap.put(PartType.ELECTRICAL, electricalCount);
			}

			if(engineCount < 1) {
				missingPartsMap.put(PartType.ENGINE, engineCount);
			}

			if(fuelFilterCount < 1) {
				missingPartsMap.put(PartType.FUEL_FILTER, fuelFilterCount);
			}
		}

		return missingPartsMap;
	}

	private Integer getPartTypeCount(PartType partTypeToFind){
		int partTypeCount = 0;

		if(parts != null){
			partTypeCount =
					parts
						.stream()
						.filter(x -> x.getType() == partTypeToFind)
						.toArray()
						.length;
		}

		return partTypeCount;
	}

	@Override
	public String toString() {
		return "Car{" +
				       "year='" + year + '\'' +
				       ", make='" + make + '\'' +
				       ", model='" + model + '\'' +
				       ", parts=" + parts +
				       '}';
	}

	/* --------------------------------------------------------------------------------------------------------------- */
	/*  Getters and Setters *///region
	/* --------------------------------------------------------------------------------------------------------------- */

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public List<Part> getParts() {
		return parts;
	}

	public void setParts(List<Part> parts) {
		this.parts = parts;
	}

	/* --------------------------------------------------------------------------------------------------------------- */
	/*  Getters and Setters End *///endregion
	/* --------------------------------------------------------------------------------------------------------------- */

}
