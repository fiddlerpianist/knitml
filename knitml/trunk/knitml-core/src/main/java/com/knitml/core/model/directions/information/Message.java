/**
 * 
 */
package com.knitml.core.model.directions.information;


public class Message {
	protected String messageKey;
	protected String label;
	
	public Message() {
	}
	
	public Message(String label) {
		this.label = label;
	}
	
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String getLabel() {
		return label;
	}
	
	
}