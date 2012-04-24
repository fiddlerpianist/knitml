package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.common.Needle;
import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.core.model.operations.InlineOperation;

public class UsingNeedle implements InlineOperation, CompositeOperation {

	protected Needle needle;
	protected List<InlineOperation> operations = new ArrayList<InlineOperation>();

	public List<InlineOperation> getOperations() {
		return this.operations;
	}

	public Needle getNeedle() {
		return needle;
	}
	
	public UsingNeedle() {
	}

	public UsingNeedle(Needle needle, List<InlineOperation> operations) {
		this.needle = needle;
		this.operations = operations;
	}
	
	
}
