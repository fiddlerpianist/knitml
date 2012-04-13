package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.Lean;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.StitchNature;
import com.knitml.core.model.operations.StitchNatureProducer;
import com.knitml.core.model.operations.YarnReferenceHolder;

public class MultipleDecrease implements DiscreteInlineOperation, StitchNatureProducer,
		YarnReferenceHolder {

	protected Integer stitchesIntoOne;
	protected String yarnIdRef;
	protected Lean lean;
	protected StitchNature stitchNatureProduced;

	public MultipleDecrease(Integer stitchesIntoOne, StitchNature nature) {
		this.stitchesIntoOne = stitchesIntoOne;
		this.stitchNatureProduced = nature;
	}

	public MultipleDecrease(Integer stitchesIntoOne, StitchNature nature, Lean lean, String yarnIdRef) {
		this.stitchesIntoOne = stitchesIntoOne;
		this.lean = lean;
		this.stitchNatureProduced = nature;
		this.yarnIdRef = yarnIdRef;
	}

	public List<? extends MultipleDecrease> canonicalize() {
		List<MultipleDecrease> newOps = new ArrayList<MultipleDecrease>(1);
		newOps.add(new MultipleDecrease(this.stitchesIntoOne, this.stitchNatureProduced, this.lean, null));
		return newOps;
	}
	
	
	public Lean getLean() {
		return lean;
	}

	public Integer getStitchesIntoOne() {
		return stitchesIntoOne;
	}

	public String getYarnIdRef() {
		return yarnIdRef;
	}

	public int getAdvanceCount() {
		return stitchesIntoOne;
	}

	public int getIncreaseCount() {
		return 1 - stitchesIntoOne;
	}

	public StitchNature getStitchNatureProduced() {
		return stitchNatureProduced;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stitchNatureProduced == null) ? 0 : stitchNatureProduced.hashCode());
		result = prime
				* result
				+ ((stitchesIntoOne == null) ? 0 : stitchesIntoOne
						.hashCode());
		result = prime * result + ((lean == null) ? 0 : lean.hashCode());
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
		MultipleDecrease other = (MultipleDecrease) obj;
		if (stitchNatureProduced != other.stitchNatureProduced)
			return false;
		if (stitchesIntoOne == null) {
			if (other.stitchesIntoOne != null)
				return false;
		} else if (!stitchesIntoOne.equals(other.stitchesIntoOne))
			return false;
		if (lean != other.lean)
			return false;
		if (yarnIdRef == null) {
			if (other.yarnIdRef != null)
				return false;
		} else if (!yarnIdRef.equals(other.yarnIdRef))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Multiple Decrease [stitchesIntoOne=" + stitchesIntoOne
				+ ", yarnIdRef=" + yarnIdRef + ", type=" + lean + ", stitchNatureProduced="
				+ stitchNatureProduced + "]";
	}

	
}
