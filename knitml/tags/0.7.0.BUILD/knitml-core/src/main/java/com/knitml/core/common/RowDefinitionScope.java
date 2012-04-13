package com.knitml.core.common;

public enum RowDefinitionScope {
	EVEN, ODD, NONE;
	
	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
 	}

}
