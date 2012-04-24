package com.knitml.core.common;

public enum IncreaseType {
	M1, M1P, M1A, M1T, M1L, M1R, M1LP, M1RP, KLL, KRL, PLL, PRL, KFB, PFB, MOSS, DECORATIVE, YO;
	
	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
 	}

}
