package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.DecreaseType;
import com.knitml.core.common.Lean;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.StitchNature;
import com.knitml.core.model.operations.StitchNatureProducer;
import com.knitml.core.model.operations.YarnReferenceHolder;

public class Decrease implements DiscreteInlineOperation, StitchNatureProducer,
		YarnReferenceHolder {

	protected Integer numberOfTimes;
	protected String yarnIdRef;
	protected DecreaseType type;

	public Decrease() {
	}
	
	public Decrease(Integer numberOfTimes, DecreaseType type, String yarnIdRef) {
		this.numberOfTimes = numberOfTimes;
		this.type = type;
		this.yarnIdRef = yarnIdRef;
	}

	public List<? extends Decrease> canonicalize() {
		int size = numberOfTimes == null ? 1 : numberOfTimes;
		List<Decrease> newOps = new ArrayList<Decrease>(size);
		for (int i = 0; i < size; i++) {
			newOps.add(new Decrease(null, this.type, null));
		}
		return newOps;
	}
	
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((numberOfTimes == null) ? 0 : numberOfTimes.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result
					+ ((yarnIdRef == null) ? 0 : yarnIdRef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Decrease other = (Decrease) obj;
		if (numberOfTimes == null) {
			if (other.numberOfTimes != null)
				return false;
		} else if (!numberOfTimes.equals(other.numberOfTimes))
			return false;
		if (type != other.type)
			return false;
		if (yarnIdRef == null) {
			if (other.yarnIdRef != null)
				return false;
		} else if (!yarnIdRef.equals(other.yarnIdRef))
			return false;
		return true;
	}

}
