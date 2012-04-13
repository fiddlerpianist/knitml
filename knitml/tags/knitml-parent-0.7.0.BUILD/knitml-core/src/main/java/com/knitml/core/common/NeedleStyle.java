package com.knitml.core.common;

public enum NeedleStyle {
	STRAIGHT, CIRCULAR, DPN;
	
	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
 	}

}
