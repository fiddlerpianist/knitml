package com.knitml.core.model.directions.inline;

import com.knitml.core.common.CrossType;
import com.knitml.core.model.directions.InlineOperation;


public class CrossStitches implements InlineOperation {
	
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
	
}
