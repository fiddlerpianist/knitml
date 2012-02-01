package com.knitml.core.model.directions.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.InlineOperation;
import com.knitml.core.model.header.StitchHolder;

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
