package com.knitml.core.xml;

import org.apache.commons.lang.StringUtils;

public class DataConversion {

	public static String serializeNullableInteger(Integer value) {
		if (value == null) {
			return "";
		}
		return String.valueOf(value);
	}
	public static Integer deserializeNullableInteger(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		return Integer.valueOf(value);
	}
	public static String serializeIntArray(int[] values) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				buff.append(' ');
			}
			buff.append(values[i]);
		}
		return buff.toString();
	}

	public static int[] deserializeIntArray(String text) {
	    if (text == null) {
	        return new int[0];
	    } else {
	        int split = 0;
	        text = text.trim();
	        int fill = 0;
	        int[] values = new int[10];
	        while (split < text.length()) {
	            int base = split;
	            split = text.indexOf(' ', split);
	            if (split < 0) {
	                split = text.length();
	            }
	            int value = Integer.parseInt(text.substring(base, split));
	            if (fill >= values.length) {
	                values = resizeArray(values, values.length*2);
	            }
	            values[fill++] = value;
	            while (split < text.length() && text.charAt(++split) == ' ');
	        }
	        return resizeArray(values, fill);
	    }
	}
	
	private static int[] resizeArray(int[] array, int size) {
	    int[] copy = new int[size];
	    System.arraycopy(array, 0, copy, 0, Math.min(array.length, size));
	    return copy;
	}	
}
