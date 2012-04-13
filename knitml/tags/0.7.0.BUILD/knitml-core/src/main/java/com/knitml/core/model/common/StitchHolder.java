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
	
	public void setId(String id) {
		this.id = id;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return getLabel();
	}
	
}
