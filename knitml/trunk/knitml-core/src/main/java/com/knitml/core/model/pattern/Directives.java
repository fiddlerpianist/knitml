package com.knitml.core.model.pattern;

import java.util.List;


public class Directives {

	protected String type;
	protected List<String> messageSources;
	protected List<Import> imports;
	protected List<Object> instructionDefinitions;
	
	public List<Import> getImports() {
		return imports;
	}
	public void setImports(List<Import> imports) {
		this.imports = imports;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setMessageSources(List<String> messageSources) {
		this.messageSources = messageSources;
	}
	public void setInstructionDefinitions(List<Object> instructionDefinitions) {
		this.instructionDefinitions = instructionDefinitions;
	}
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
