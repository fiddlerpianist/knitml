package com.knitml.core.model.directions.inline;

import com.knitml.core.common.IncreaseType;
import com.knitml.core.model.directions.InlineOperation;



public class Increase implements InlineOperation {
	
	protected Integer numberOfTimes;
	protected String yarnIdRef;
	protected IncreaseType type;
	
	public Increase() {
	}
	
	public Increase(int numberOfTimes) {
		this.numberOfTimes = numberOfTimes;
	}
	
	public Integer getNumberOfTimes() {
		return numberOfTimes;
	}
	public String getYarnIdRef() {
		return yarnIdRef;
	}
	public IncreaseType getType() {
		return type;
	}
	
}
