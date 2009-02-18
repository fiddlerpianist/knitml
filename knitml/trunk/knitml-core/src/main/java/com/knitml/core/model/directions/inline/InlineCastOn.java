package com.knitml.core.model.directions.inline;

import com.knitml.core.model.directions.DiscreteInlineOperation;

public class InlineCastOn implements DiscreteInlineOperation {

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

}
