package com.knitml.core.model.operations;

import com.knitml.core.common.EnumUtils;

public enum StitchNature {
	KNIT, PURL;
	
	public static StitchNature reverse(StitchNature nature) {
		if (nature == KNIT) {
			return PURL;
		} else {
			return KNIT;
		}
	}
	
	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
 	}
	
}
