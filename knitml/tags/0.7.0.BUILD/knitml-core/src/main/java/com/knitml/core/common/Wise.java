/**
 * 
 */
package com.knitml.core.common;

public enum Wise {
	KNITWISE, PURLWISE;

	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
	}
}