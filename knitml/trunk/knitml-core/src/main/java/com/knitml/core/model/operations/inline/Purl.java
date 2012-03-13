package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.LoopToWork;
import com.knitml.core.model.operations.StitchNature;

public class Purl extends Knit {

	public Purl(Integer numberOfTimes, LoopToWork loopToWork, String yarnIdRef) {
		super(numberOfTimes, loopToWork, yarnIdRef);
	}

	@Override
	public String toString() {
		return "Purl"
				+ (numberOfTimes != null ? " " + numberOfTimes : "")
				+ (yarnIdRef != null ? " with yarn " + yarnIdRef : "")
				+ (loopToWork != null ? " through " + loopToWork + " loop" : "");
	}

	public List<Purl> canonicalize() {
		int size = numberOfTimes == null ? 1 : numberOfTimes;
		List<Purl> newOps = new ArrayList<Purl>(size);
		for (int i = 0; i < size; i++) {
			newOps.add(new Purl(1, loopToWork == null ? LoopToWork.LEADING
					: loopToWork, null));
		}
		return newOps;
	}

	@Override
	public StitchNature getStitchNatureProduced() {
		return StitchNature.PURL;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((loopToWork == null) ? 0 : loopToWork.hashCode());
		result = prime * result
				+ ((numberOfTimes == null) ? 0 : numberOfTimes.hashCode());
		result = prime * result
				+ ((yarnIdRef == null) ? 0 : yarnIdRef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Purl other = (Purl) obj;
		if (loopToWork != other.loopToWork)
			return false;
		if (numberOfTimes == null) {
			if (other.numberOfTimes != null)
				return false;
		} else if (!numberOfTimes.equals(other.numberOfTimes))
			return false;
		if (yarnIdRef == null) {
			if (other.yarnIdRef != null)
				return false;
		} else if (!yarnIdRef.equals(other.yarnIdRef))
			return false;
		return true;
	}

}
