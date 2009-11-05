package com.knitml.core.model.directions.inline;

import com.knitml.core.common.DecreaseType;
import com.knitml.core.common.Lean;
import com.knitml.core.model.directions.DiscreteInlineOperation;
import com.knitml.core.model.directions.StitchNature;
import com.knitml.core.model.directions.StitchNatureProducer;

public class Decrease implements DiscreteInlineOperation,
		StitchNatureProducer {

	protected Integer numberOfTimes;
	protected String yarnIdRef;
	protected DecreaseType type;

	public DecreaseType getType() {
		return type;
	}

	public Integer getNumberOfTimes() {
		return numberOfTimes;
	}

	public String getYarnIdRef() {
		return yarnIdRef;
	}

	public int getAdvanceCount() {
		int numberOfDecreases = numberOfTimes == null ? 1 : numberOfTimes;
		return 2 * numberOfDecreases;
	}

	public int getIncreaseCount() {
		int numberOfDecreases = numberOfTimes == null ? 1 : numberOfTimes;
		return -1 * numberOfDecreases;
	}

	public Lean getLean() {
		switch (type) {
		case K2TOG:
		case SSP:
		case P2TOG_TBL:
		case K3TOG:
			return Lean.RIGHT;
		case K2TOG_TBL:
		case SSK:
		case SSSK:
		case SKP:
		case P2TOG:
		case P3TOG:
		case SK2P:
			return Lean.LEFT;
		case CDD:
			return Lean.BALANCED;
		default:
			return null;
		}
	}

	public StitchNature getStitchNatureProduced() {
		switch (type) {
		case SSP:
		case P2TOG_TBL:
		case P2TOG:
		case P3TOG:
			return StitchNature.PURL;
		default:
			return StitchNature.KNIT;
		}
	}

}
