package com.knitml.core.model.header;

import java.util.List;

public class GeneralInformation {
	
	private String patternName;
	private String description;
	private String languageCode;
	private String dimensions;
	private List<String> techniques;
	private Gauge gauge;
	private Author author;
	private String copyright;

	public Gauge getGauge() {
		return gauge;
	}
	public String getPatternName() {
		return patternName;
	}
	public String getDescription() {
		return description;
	}
	public String getDimensions() {
		return dimensions;
	}
	public List<String> getTechniques() {
		return techniques;
	}
	public Author getAuthor() {
		return author;
	}
	public String getCopyright() {
		return copyright;
	}
	public String getLanguageCode() {
		return languageCode;
	}
}
