package com.knitml.engine.impl;

import com.knitml.engine.Marker;
import com.knitml.engine.settings.MarkerBehavior;

public class DefaultMarker implements Marker {
	
	private MarkerBehavior whenWorkedThrough;
	
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

}
