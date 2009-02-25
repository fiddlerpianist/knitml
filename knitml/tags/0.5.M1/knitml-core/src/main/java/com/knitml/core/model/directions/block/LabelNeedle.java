package com.knitml.core.model.directions.block;

import com.knitml.core.model.directions.BlockOperation;
import com.knitml.core.model.header.Needle;


public class LabelNeedle implements BlockOperation {
	
	protected Needle needle;
	protected String messageKey;
	protected String label;
	
	public Needle getNeedle() {
		return needle;
	}

	public String getMessageKey() {
		return this.messageKey;
	}

	public String getLabel() {
		return label;
	}
	
}
