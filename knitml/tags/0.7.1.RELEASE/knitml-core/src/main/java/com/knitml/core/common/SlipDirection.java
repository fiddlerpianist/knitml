package com.knitml.core.common;

public enum SlipDirection {
	FORWARD, REVERSE;
	
	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
 	}

}
