/**
 * 
 */
package com.knitml.core.common;

public enum LoopToWork {
	LEADING, TRAILING;

	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
	}
}