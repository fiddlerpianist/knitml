/**
 * 
 */
package com.knitml.core.common;

public enum Slip {
	LEADING, TRAILING;

	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
	}
}