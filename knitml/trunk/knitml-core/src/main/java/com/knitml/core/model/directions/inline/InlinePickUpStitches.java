package com.knitml.core.model.directions.inline;

import com.knitml.core.common.Wise;
import com.knitml.core.model.directions.DiscreteInlineOperation;
import com.knitml.core.model.directions.StitchNature;
import com.knitml.core.model.directions.StitchNatureProducer;

public class InlinePickUpStitches implements DiscreteInlineOperation, StitchNatureProducer {
	
	protected String yarnIdRef;
	protected Integer numberOfTimes;
	protected Wise type;
	
	public InlinePickUpStitches() {
	}
	
	public InlinePickUpStitches(Integer numberOfTimes, String yarnIdRef, Wise type) {
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

	public int getAdvanceCount() {
		return 0;
	}

	public int getIncreaseCount() {
		return numberOfTimes == null ? 1 : numberOfTimes;
	}

	public StitchNature getStitchNatureProduced() {
		if (type == Wise.PURLWISE) {
			return StitchNature.PURL;
		}
		return StitchNature.KNIT;
	}
	
}
