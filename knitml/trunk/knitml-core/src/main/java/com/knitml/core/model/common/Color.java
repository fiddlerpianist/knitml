/**
 * 
 */
package com.knitml.core.model.common;

public class Color {
	protected String name;
	protected String description;
	protected String number;
	protected String rgbValue;
	
	public String getRgbValue() {
		return rgbValue;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getNumber() {
		return number;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public void setRgbValue(String rgbValue) {
		this.rgbValue = rgbValue;
	}
	
	
}