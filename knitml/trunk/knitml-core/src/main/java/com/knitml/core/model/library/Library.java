package com.knitml.core.model.library;

import java.util.List;

import com.knitml.core.model.common.Identifiable;
import com.knitml.core.model.common.Yarn;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class Library {
	protected String schemaLocation;
	protected String languageCode;
	protected String version;
	protected String namespace;
	protected Information information;
	protected List<Yarn> yarns;
	protected List<Identifiable> instructionDefinitions;
	public void setSchemaLocation(String schemaLocation) {
		this.schemaLocation = schemaLocation;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public void setInformation(Information information) {
		this.information = information;
	}
	public void setYarns(List<Yarn> yarns) {
		this.yarns = yarns;
	}
	public void setInstructionDefinitions(List<Identifiable> instructionDefinitions) {
		this.instructionDefinitions = instructionDefinitions;
	}
	
}
