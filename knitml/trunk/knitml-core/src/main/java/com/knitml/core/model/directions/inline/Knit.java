package com.knitml.core.model.directions.inline;

import com.knitml.core.common.LoopToWork;
import com.knitml.core.model.directions.DiscreteInlineOperation;
import com.knitml.core.model.directions.StitchNature;
import com.knitml.core.model.directions.StitchNatureProducer;

public class Knit implements DiscreteInlineOperation, StitchNatureProducer {

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

//	@Override
//	public boolean equals(Object obj) {
//		if (obj instanceof Knit && !(obj instanceof Purl)) {
//			Knit that = (Knit) obj;
//			return (ObjectUtils.equals(this.numberOfTimes, that.numberOfTimes)
//					&& ObjectUtils.equals(this.yarnIdRef, that.yarnIdRef) && ObjectUtils
//						.equals(this.loopToWork, that.loopToWork));
//		}
//		return false;
//	}
//
	@Override
	public String toString() {
		return "Knit" + (numberOfTimes != null ? " " + numberOfTimes : "")
				+ (yarnIdRef != null ? " with yarn " + yarnIdRef : "")
				+ (loopToWork != null ? " through " + loopToWork + " loop": "");
	}

	public StitchNature getStitchNatureProduced() {
		return StitchNature.KNIT;
	}

}
