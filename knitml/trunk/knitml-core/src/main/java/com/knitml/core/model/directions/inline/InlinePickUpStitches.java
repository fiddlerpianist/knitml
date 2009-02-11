package com.knitml.core.model.directions.inline;

import com.knitml.core.common.Wise;
import com.knitml.core.model.directions.InlineOperation;

public class InlinePickUpStitches implements InlineOperation {
	
	protected String yarnIdRef;
	protected Integer numberOfTimes;
	protected Wise type;
	
	public InlinePickUpStitches() {
	}
	
	public InlinePickUpStitches(Integer numberOfTimes, Wise type) {
		this.numberOfTimes = numberOfTimes;
		this.type = type;
	}

	public String getYarnIdRef() {
		return yarnIdRef;
	}

	public Integer getNumberOfTimes() {
		return numberOfTimes;
	}

	public Wise getType() {
		return type;
	}

}
