package com.knitml.core.model.directions.inline;

import com.knitml.core.common.Wise;
import com.knitml.core.model.directions.DiscreteInlineOperation;
import com.knitml.core.model.directions.StitchNature;
import com.knitml.core.model.directions.StitchNatureProducer;

public class BindOff implements DiscreteInlineOperation,
		StitchNatureProducer {

	protected Integer numberOfStitches;
	protected Wise type;
	protected String yarnIdRef;

	public BindOff(Integer numberOfStitches, Wise type, String yarnIdRef) {
		super();
		this.numberOfStitches = numberOfStitches;
		this.type = type;
		this.yarnIdRef = yarnIdRef;
	}

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

	public StitchNature getStitchNatureProduced() {
		if (type == Wise.PURLWISE) {
			return StitchNature.PURL;
		}
		return StitchNature.KNIT;
	}

}
