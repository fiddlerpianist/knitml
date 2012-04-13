package com.knitml.core.model.pattern;


/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class Pattern {
	protected Directives directives;
	protected GeneralInformation generalInformation;
	protected Supplies supplies;
	protected Directions directions;
	protected String schemaLocation;
	protected String languageCode;
	protected String version;
	
	public Pattern(Directions directions) {
		this.directions = directions;
	}
	
	public Directives getDirectives() {
		return directives;
	}
	public GeneralInformation getGeneralInformation() {
		return generalInformation;
	}
	public Supplies getSupplies() {
		return supplies;
	}
	public Directions getDirections() {
		return directions;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public String getVersion() {
		return version;
	}
}
