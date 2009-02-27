package com.knitml.core.model;

import com.knitml.core.model.directions.Directions;
import com.knitml.core.model.header.Directives;
import com.knitml.core.model.header.GeneralInformation;
import com.knitml.core.model.header.Supplies;

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
