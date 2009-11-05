package com.knitml.core.model.directions.inline;

import org.apache.commons.lang.ObjectUtils;

import com.knitml.core.model.directions.DiscreteInlineOperation;

public class WorkEven implements DiscreteInlineOperation {

	protected Integer numberOfTimes;
	protected String yarnIdRef;

	public Integer getNumberOfTimes() {
		return numberOfTimes;
	}

	public String getYarnIdRef() {
		return yarnIdRef;
	}

	protected boolean hasContent() {
		return numberOfTimes != null;
	}

	public WorkEven(Integer numberOfTimes, String yarnIdRef) {
		super();
		this.numberOfTimes = numberOfTimes;
		this.yarnIdRef = yarnIdRef;
	}

	public int getAdvanceCount() {
		return numberOfTimes == null ? 1 : numberOfTimes;
	}
	
	public int getIncreaseCount() {
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WorkEven) {
			WorkEven that = (WorkEven) obj;
			return (ObjectUtils.equals(this.numberOfTimes, that.numberOfTimes)
					&& ObjectUtils.equals(this.yarnIdRef, that.yarnIdRef));
		}
		return false;
	}

}
