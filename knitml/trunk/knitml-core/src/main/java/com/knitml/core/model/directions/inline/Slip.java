package com.knitml.core.model.directions.inline;

import com.knitml.core.common.Wise;
import com.knitml.core.common.YarnPosition;
import com.knitml.core.model.directions.InlineOperation;

public class Slip implements InlineOperation {
	
	protected Integer numberOfTimes;
	protected Wise type;
	protected YarnPosition yarnPosition;
	
	public Slip() {
	}
	
	public Slip(Integer numberOfTimes, Wise type, YarnPosition yarnPosition) {
		this.numberOfTimes = numberOfTimes;
		this.type = type;
		this.yarnPosition = yarnPosition;
	}
	public Integer getNumberOfTimes() {
		return numberOfTimes;
	}
	public Wise getType() {
		return type;
	}
	public YarnPosition getYarnPosition() {
		return yarnPosition;
	}
	
}
