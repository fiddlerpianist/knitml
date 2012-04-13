package com.knitml.core.model.operations.block;

import com.knitml.core.model.common.Needle;
import com.knitml.core.model.operations.BlockOperation;


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
