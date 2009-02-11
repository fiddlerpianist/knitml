package com.knitml.core.common;

public enum DecreaseType {
	SSK, K2TOG, SSP, P2TOG, SKP, K2TOG_TBL, P2TOG_TBL;
	
	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
 	}

}
