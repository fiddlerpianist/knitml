package com.knitml.core.model.directions.inline;

import com.knitml.core.common.Wise;
import com.knitml.core.model.directions.InlineOperation;


public class BindOff implements InlineOperation {
	
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
	
}
