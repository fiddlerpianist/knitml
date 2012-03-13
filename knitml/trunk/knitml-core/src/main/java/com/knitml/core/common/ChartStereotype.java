package com.knitml.core.common;

public enum ChartStereotype {
	LACE, CABLES, COLOR;
	
	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
 	}

}
