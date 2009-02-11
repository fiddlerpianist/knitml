package com.knitml.core.model.directions.inline;

import com.knitml.core.model.directions.InlineOperation;

public class InlineCastOn implements InlineOperation {

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

}
