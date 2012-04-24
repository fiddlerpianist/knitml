package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.Wise;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.StitchNature;
import com.knitml.core.model.operations.StitchNatureProducer;
import com.knitml.core.model.operations.YarnReferenceHolder;

public class InlinePickUpStitches implements DiscreteInlineOperation,
		StitchNatureProducer, YarnReferenceHolder {

	protected String yarnIdRef;
	protected Integer numberOfTimes;
	protected Wise type;

	public InlinePickUpStitches() {
	}

	public InlinePickUpStitches(Integer numberOfTimes, Wise type,
			String yarnIdRef) {
		this.numberOfTimes = numberOfTimes;
		this.yarnIdRef = yarnIdRef;
		this.type = type;
	}

	public List<InlinePickUpStitches> canonicalize() {
		int size = numberOfTimes == null ? 1 : numberOfTimes;
		List<InlinePickUpStitches> newOps = new ArrayList<InlinePickUpStitches>(
				size);
		for (int i = 0; i < size; i++) {
			newOps.add(new InlinePickUpStitches(1, type == null ? Wise.KNITWISE
					: type, null));
		}
		return newOps;
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
		InlinePickUpStitches other = (InlinePickUpStitches) obj;
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
