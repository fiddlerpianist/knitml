package com.knitml.core.model.directions.block;

import com.knitml.core.common.Wise;
import com.knitml.core.model.directions.BlockOperation;

public class PickUpStitches implements BlockOperation {
	
	protected String yarnIdRef;
	protected Integer numberOfTimes;
	protected Wise type;
	
	public PickUpStitches() {
	}
	
	public PickUpStitches(Integer numberOfTimes, String yarnIdRef, Wise type) {
		this.numberOfTimes = numberOfTimes;
		this.yarnIdRef = yarnIdRef;
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
