package com.knitml.core.common;

public enum KnittingShape {
	FLAT, ROUND;

	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
 	}

}
