package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.IncreaseType;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.StitchNature;
import com.knitml.core.model.operations.StitchNatureProducer;
import com.knitml.core.model.operations.YarnReferenceHolder;

public class Increase implements DiscreteInlineOperation, StitchNatureProducer,
		YarnReferenceHolder {

	protected Integer numberOfTimes;
	protected String yarnIdRef;
	protected IncreaseType type;

	public Increase() {
	}

	public Increase(int numberOfTimes) {
		this.numberOfTimes = numberOfTimes;
	}

	public Increase(int numberOfTimes, IncreaseType type) {
		this.numberOfTimes = numberOfTimes;
		this.type = type;
	}

	public Increase(int numberOfTimes, IncreaseType type, String yarnIdRef) {
		this.numberOfTimes = numberOfTimes;
		this.type = type;
		this.yarnIdRef = yarnIdRef;
	}

	public List<Increase> canonicalize() {
		int size = numberOfTimes == null ? 1 : numberOfTimes;
		List<Increase> newOps = new ArrayList<Increase>(size);
		for (int i = 0; i < size; i++) {
			newOps.add(new Increase(1, this.type, null));
		}
		return newOps;
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
		Increase other = (Increase) obj;
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
