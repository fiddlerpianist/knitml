package com.knitml.core.model.directions;

public enum StitchNature {
	KNIT, PURL;
	
	public static StitchNature reverse(StitchNature nature) {
		if (nature == KNIT) {
			return PURL;
		} else {
			return KNIT;
		}
	}
}
