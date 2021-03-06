package com.knitml.core.model.directions.inline;

import org.apache.commons.lang.ObjectUtils;

import com.knitml.core.common.LoopToWork;
import com.knitml.core.model.directions.DiscreteInlineOperation;

public class Knit implements DiscreteInlineOperation {

	protected Integer numberOfTimes;
	protected String yarnIdRef;
	protected LoopToWork loopToWork;

	public Integer getNumberOfTimes() {
		return numberOfTimes;
	}

	public String getYarnIdRef() {
		return yarnIdRef;
	}

	public LoopToWork getLoopToWork() {
		return loopToWork;
	}

	protected boolean hasContent() {
		return numberOfTimes != null;
	}

	public Knit(Integer numberOfTimes, String yarnIdRef, LoopToWork loopToWork) {
		super();
		this.numberOfTimes = numberOfTimes;
		this.yarnIdRef = yarnIdRef;
		this.loopToWork = loopToWork;
	}

	public int getAdvanceCount() {
		return numberOfTimes == null ? 1 : numberOfTimes;
	}
	
	public int getIncreaseCount() {
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Knit && !(obj instanceof Purl)) {
			Knit that = (Knit) obj;
			return (ObjectUtils.equals(this.numberOfTimes, that.numberOfTimes)
					&& ObjectUtils.equals(this.yarnIdRef, that.yarnIdRef) && ObjectUtils
					.equals(this.loopToWork, that.loopToWork));
		}
		return false;
	}

}
