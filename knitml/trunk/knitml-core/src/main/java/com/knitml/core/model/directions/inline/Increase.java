package com.knitml.core.model.directions.inline;

import com.knitml.core.common.IncreaseType;
import com.knitml.core.model.directions.DiscreteInlineOperation;
import com.knitml.core.model.directions.StitchNature;
import com.knitml.core.model.directions.StitchNatureProducer;

public class Increase implements DiscreteInlineOperation,
		StitchNatureProducer {

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

	public int getAdvanceCount() {
		if (type != null) {
			switch (type) {
			case KFB:
			case PFB:
			case MOSS:
				// these increases use up an existing stitch
				return numberOfTimes == null ? 1 : numberOfTimes;
			default:
				// all other increases do not
				return 0;
			}
		}
		return 0;
	}

	public int getIncreaseCount() {
		return numberOfTimes == null ? 1 : numberOfTimes;
	}

	public StitchNature getStitchNatureProduced() {
		if (type == null) {
			return StitchNature.KNIT;
		}
		switch (type) {
		case M1P:
		case M1LP:
		case M1RP:
		case PLL:
		case PRL:
		case PFB:
			return StitchNature.PURL;
		default:
			return StitchNature.KNIT;
		}
	}
}
