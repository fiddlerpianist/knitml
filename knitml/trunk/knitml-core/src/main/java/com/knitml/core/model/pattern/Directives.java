package com.knitml.core.model.pattern;

import java.util.List;


public class Directives {

	protected String type;
	protected List<String> messageSources;
	protected List<Object> instructionDefinitions;
	
	public String getType() {
		return type;
	}
	public List<String> getMessageSources() {
		return messageSources;
	}
	public List<Object> getInstructionDefinitions() {
		return instructionDefinitions;
	} 
	
	
}
