package com.knitml.core.common;

public enum YarnPosition {
	FRONT, BACK;
	
	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
 	}

}
