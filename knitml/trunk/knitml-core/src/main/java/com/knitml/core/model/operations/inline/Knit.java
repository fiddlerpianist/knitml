package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.LoopToWork;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.StitchNature;
import com.knitml.core.model.operations.StitchNatureProducer;
import com.knitml.core.model.operations.YarnReferenceHolder;

public class Knit implements DiscreteInlineOperation, StitchNatureProducer,
		YarnReferenceHolder {

	protected Integer numberOfTimes;
	protected String yarnIdRef;
	protected LoopToWork loopToWork;

	public Integer getNumberOfTimes() {
		return numberOfTimes;
	}

	public String getYarnIdRef() {
		return yarnIdRef;
	}

	public LoopToWork getLoopToWork() {
		return loopToWork;
	}

	protected boolean hasContent() {
		return numberOfTimes != null;
	}

	public Knit(Integer numberOfTimes, LoopToWork loopToWork, String yarnIdRef) {
		super();
		this.numberOfTimes = numberOfTimes;
		this.yarnIdRef = yarnIdRef;
		this.loopToWork = loopToWork;
	}

	public List<? extends Knit> canonicalize() {
		int size = numberOfTimes == null ? 1 : numberOfTimes;
		List<Knit> newOps = new ArrayList<Knit>(size);
		for (int i = 0; i < size; i++) {
			newOps.add(new Knit(1, loopToWork == null ? LoopToWork.LEADING
					: loopToWork, null));
		}
		return newOps;
	}

	public int getAdvanceCount() {
		return numberOfTimes == null ? 1 : numberOfTimes;
	}

	public int getIncreaseCount() {
		return 0;
	}

	@Override
	public String toString() {
		return "Knit"
				+ (numberOfTimes != null ? " " + numberOfTimes : "")
				+ (yarnIdRef != null ? " with yarn " + yarnIdRef : "")
				+ (loopToWork != null ? " through " + loopToWork + " loop" : "");
	}

	public StitchNature getStitchNatureProduced() {
		return StitchNature.KNIT;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Knit other = (Knit) obj;
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
