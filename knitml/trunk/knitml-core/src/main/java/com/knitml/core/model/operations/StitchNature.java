package com.knitml.core.model.operations;

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
