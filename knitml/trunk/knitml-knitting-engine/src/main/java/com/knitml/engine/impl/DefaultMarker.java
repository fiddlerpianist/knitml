package com.knitml.engine.impl;

import com.knitml.engine.Marker;
import com.knitml.engine.settings.MarkerBehavior;

public class DefaultMarker implements Marker {
	
	private MarkerBehavior whenWorkedThrough;
	private boolean gapMarker = false;
	
	public DefaultMarker() {
		this.whenWorkedThrough = MarkerBehavior.THROW_EXCEPTION;
	}
	
	public DefaultMarker(MarkerBehavior whenWorkedThrough) {
		this.whenWorkedThrough = whenWorkedThrough;
	}

	/**
	 * @see com.knitml.engine.Marker#getWhenWorkedThrough()
	 */
	public MarkerBehavior getWhenWorkedThrough() {
		return whenWorkedThrough;
	}

	public void setWhenWorkedThrough(
			MarkerBehavior whenWorkedThrough) {
		this.whenWorkedThrough = whenWorkedThrough;
	}

	/**
	 * @see com.knitml.engine.Marker#isGapMarker()
	 */
	public boolean isGapMarker() {
		return gapMarker;
	}

	public void setGapMarker(boolean marksGap) {
		this.gapMarker = marksGap;
	}

}
