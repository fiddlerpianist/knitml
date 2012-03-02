package com.knitml.core.model.common;



/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class StitchHolder implements Identifiable {

	protected String id;
	protected String messageKey;
	protected String label;
	

	public String getLabel() {
		return label;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return getLabel();
	}
	
}
