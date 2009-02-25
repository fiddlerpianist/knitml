package com.knitml.core.model.directions.inline;

import com.knitml.core.common.Wise;
import com.knitml.core.model.directions.DiscreteInlineOperation;


public class BindOff implements DiscreteInlineOperation {
	
	protected Integer numberOfStitches;
	protected Wise type;
	protected String yarnIdRef;
	
	public Integer getNumberOfStitches() {
		return numberOfStitches;
	}
	public Wise getType() {
		return type;
	}
	public String getYarnIdRef() {
		return yarnIdRef;
	}
	public int getAdvanceCount() {
		return numberOfStitches;
	}
	public int getIncreaseCount() {
		return 0 - numberOfStitches;
	}
	
}
