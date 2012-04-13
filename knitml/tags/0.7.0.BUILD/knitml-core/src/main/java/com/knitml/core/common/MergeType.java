package com.knitml.core.common;

public enum MergeType {
	PHYSICAL, LOGICAL;

	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
 	}

}
