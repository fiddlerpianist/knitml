package com.knitml.core.common;

public class EnumUtils {
	
	private EnumUtils() {}
	
	public static <T extends Enum<T>> T toEnum(String name, Class<T> target) {
		if (name == null) {
			return null;
		}
		return T.valueOf(target, name.toUpperCase().replace('-', '_'));
	}
	
	public static String fromEnum(Enum<?> e) {
		if (e == null) {
			return null;
		}
		return e.name().toLowerCase().replace('_', '-');
	}
	
}
