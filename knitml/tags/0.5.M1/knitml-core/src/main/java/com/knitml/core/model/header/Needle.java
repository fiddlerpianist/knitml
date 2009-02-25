package com.knitml.core.model.header;

import com.knitml.core.common.ValidationException;
import com.knitml.core.model.Identifiable;


/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class Needle implements Identifiable {

	private String id;
	private String messageKey;
	private NeedleType type;
	private String label;
	
	private String labelOverride;
	
	public void setLabel(String newLabel) {
		this.labelOverride = newLabel;
	}

	public String getLabel() {
		return labelOverride == null ? label : labelOverride;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public NeedleType getType() {
		return type;
	}

	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return getLabel();
	}
	
	public void afterPropertiesSet() {
		if (type == null) {
			throw new ValidationException("No needle type was able to be found for this needle", this);
		}
	}

}
