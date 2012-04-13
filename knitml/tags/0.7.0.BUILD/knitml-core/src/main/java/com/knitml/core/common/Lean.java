package com.knitml.core.common;

public enum Lean {
	LEFT,RIGHT,BALANCED;
	
	public String getCanonicalName() {
		return EnumUtils.fromEnum(this);
 	}

}
