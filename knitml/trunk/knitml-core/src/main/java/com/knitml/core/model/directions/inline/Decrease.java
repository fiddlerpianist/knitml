package com.knitml.core.model.directions.inline;

import com.knitml.core.model.directions.InlineOperation;



public class Decrease implements InlineOperation {
	
	protected Integer numberOfTimes;
	protected String yarnIdRef;
	protected String type;
	
	public Integer getNumberOfTimes() {
		return numberOfTimes;
	}
	public String getYarnIdRef() {
		return yarnIdRef;
	}
	public String getType() {
		return type;
	}
	
}
