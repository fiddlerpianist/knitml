package com.knitml.core.model.directions.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.InlineOperation;
import com.knitml.core.model.header.Needle;

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
