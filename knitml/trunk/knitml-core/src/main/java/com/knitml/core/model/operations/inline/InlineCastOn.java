package com.knitml.core.model.operations.inline;

import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.StitchNature;
import com.knitml.core.model.operations.StitchNatureProducer;

public class InlineCastOn implements DiscreteInlineOperation, StitchNatureProducer {

	protected Integer numberOfStitches;
	protected String yarnIdRef;
	protected String style;

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
	
	public InlineCastOn() {
	}
	
	public InlineCastOn(int numberOfStitches) {
		this.numberOfStitches = numberOfStitches;
	}

	public InlineCastOn(int numberOfStitches, String yarnIdRef) {
		this.numberOfStitches = numberOfStitches;
		this.yarnIdRef = yarnIdRef;
	}

	public StitchNature getStitchNatureProduced() {
		return StitchNature.KNIT;
	}

}
