/**
 * 
 */
package com.knitml.core.common;

public enum Side {
	RIGHT, WRONG;

	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
	}

}