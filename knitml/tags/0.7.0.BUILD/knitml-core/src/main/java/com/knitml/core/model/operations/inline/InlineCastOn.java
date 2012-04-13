package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.StitchNature;
import com.knitml.core.model.operations.StitchNatureProducer;
import com.knitml.core.model.operations.YarnReferenceHolder;

public class InlineCastOn implements DiscreteInlineOperation,
		StitchNatureProducer, YarnReferenceHolder {

	protected Integer numberOfStitches;
	protected String yarnIdRef;
	protected String style;

	public InlineCastOn() {
	}

	public InlineCastOn(Integer numberOfStitches) {
		this.numberOfStitches = numberOfStitches;
	}

	public InlineCastOn(Integer numberOfStitches, String yarnIdRef) {
		this.numberOfStitches = numberOfStitches;
		this.yarnIdRef = yarnIdRef;
	}

	public InlineCastOn(Integer numberOfStitches, String style, String yarnIdRef) {
		this.numberOfStitches = numberOfStitches;
		this.style = style;
		this.yarnIdRef = yarnIdRef;
	}
	
	public List<InlineCastOn> canonicalize() {
		int size = numberOfStitches == null ? 1 : numberOfStitches;
		List<InlineCastOn> newOps = new ArrayList<InlineCastOn>(size);
		for (int i = 0; i < size; i++) {
			newOps.add(new InlineCastOn(1, this.style, null));
		}
		return newOps;
	}
	
	public StitchNature getStitchNatureProduced() {
		return StitchNature.KNIT;
	}

	public Integer getNumberOfStitches() {
		return numberOfStitches;
	}

	public String getYarnIdRef() {
		return yarnIdRef;
	}

	public String getStyle() {
		return style;
	}

	public void setNumberOfStitches(Integer numberOfStitches) {
		this.numberOfStitches = numberOfStitches;
	}

	public void setYarnIdRef(String yarnIdRef) {
		this.yarnIdRef = yarnIdRef;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getAdvanceCount() {
		return 0;
	}

	public int getIncreaseCount() {
		return numberOfStitches == null ? 1 : numberOfStitches;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((numberOfStitches == null) ? 0 : numberOfStitches.hashCode());
		result = prime * result + ((style == null) ? 0 : style.hashCode());
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
		InlineCastOn other = (InlineCastOn) obj;
		if (numberOfStitches == null) {
			if (other.numberOfStitches != null)
				return false;
		} else if (!numberOfStitches.equals(other.numberOfStitches))
			return false;
		if (style == null) {
			if (other.style != null)
				return false;
		} else if (!style.equals(other.style))
			return false;
		if (yarnIdRef == null) {
			if (other.yarnIdRef != null)
				return false;
		} else if (!yarnIdRef.equals(other.yarnIdRef))
			return false;
		return true;
	}
}
