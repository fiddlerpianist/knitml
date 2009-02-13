package com.knitml.core.model.directions.inline;

import com.knitml.core.common.CrossType;
import com.knitml.core.model.directions.DiscreteInlineOperation;


public class CrossStitches implements DiscreteInlineOperation {
	
	protected Integer first;
	protected Integer next;
	protected CrossType type;
	
	public Integer getFirst() {
		return first;
	}
	public Integer getNext() {
		return next;
	}
	public CrossType getType() {
		return type;
	}
	public int getAdvanceCount() {
		return 0;
	}
	public int getIncreaseCount() {
		return 0;
	}
	
}
