package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.Wise;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.StitchNature;
import com.knitml.core.model.operations.StitchNatureProducer;
import com.knitml.core.model.operations.YarnReferenceHolder;

public class BindOff implements DiscreteInlineOperation, StitchNatureProducer,
		YarnReferenceHolder {

	protected Integer numberOfStitches;
	protected Wise type;
	protected String yarnIdRef;

	public BindOff(Integer numberOfStitches, Wise type, String yarnIdRef) {
		super();
		this.numberOfStitches = numberOfStitches;
		this.type = type;
		this.yarnIdRef = yarnIdRef;
	}

	public List<BindOff> canonicalize() {
		int size = numberOfStitches == null ? 1 : numberOfStitches;
		List<BindOff> newOps = new ArrayList<BindOff>(size);
		for (int i = 0; i < size; i++) {
			newOps.add(new BindOff(1, this.type == null ? Wise.KNITWISE
					: this.type, null));
		}
		return newOps;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((numberOfStitches == null) ? 0 : numberOfStitches.hashCode());
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
		BindOff other = (BindOff) obj;
		if (numberOfStitches == null) {
			if (other.numberOfStitches != null)
				return false;
		} else if (!numberOfStitches.equals(other.numberOfStitches))
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
