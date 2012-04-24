package com.knitml.core.common;

public enum CrossType {
	FRONT, BACK;
	
	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
 	}

}
