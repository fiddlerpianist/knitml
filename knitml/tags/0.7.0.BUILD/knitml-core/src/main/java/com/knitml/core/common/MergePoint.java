package com.knitml.core.common;

public enum MergePoint {
	ROW, END;

	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
 	}

}
