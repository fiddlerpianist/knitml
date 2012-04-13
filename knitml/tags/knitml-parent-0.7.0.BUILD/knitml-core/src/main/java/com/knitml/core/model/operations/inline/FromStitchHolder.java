package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.common.StitchHolder;
import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.core.model.operations.InlineOperation;

public class FromStitchHolder implements InlineOperation, CompositeOperation {

	protected StitchHolder stitchHolder;
	protected List<InlineOperation> operations = new ArrayList<InlineOperation>();

	public List<InlineOperation> getOperations() {
		return this.operations;
	}

	public StitchHolder getStitchHolder() {
		return stitchHolder;
	}
	
	public FromStitchHolder() {
	}

	public FromStitchHolder(StitchHolder stitchHolder, List<InlineOperation> operations) {
		this.stitchHolder = stitchHolder;
		this.operations = operations;
	}
	
	
}
