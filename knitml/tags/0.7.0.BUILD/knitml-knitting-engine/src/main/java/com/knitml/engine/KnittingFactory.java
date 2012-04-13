package com.knitml.engine;

import com.knitml.core.common.NeedleStyle;
import com.knitml.engine.settings.MarkerBehavior;



public interface KnittingFactory {
	
	Needle createNeedle(String name, NeedleStyle needleType);
	Marker createMarker(String name, MarkerBehavior markerBehavior);
	Marker createMarker();
	Stitch createStitch(String name);
	Stitch createStitch();

}
