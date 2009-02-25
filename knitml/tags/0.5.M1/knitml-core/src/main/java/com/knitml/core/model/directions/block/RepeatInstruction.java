package com.knitml.core.model.directions.block;

import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.BlockOperation;

public class RepeatInstruction implements BlockOperation {

	public enum Until { UNTIL_DESIRED_LENGTH, UNTIL_STITCHES_REMAIN, UNTIL_STITCHES_REMAIN_ON_NEEDLES, UNTIL_MEASURES, ADDITIONAL_TIMES, UNTIL_EQUALS };
	
	protected Identifiable ref;
	protected Until until;
	protected Object value;
	
	public Identifiable getRef() {
		return ref;
	}

	public Until getUntil() {
		return until;
	}

	public Object getValue() {
		return value;
	}

	public void setRef(Identifiable instructionRef) {
		this.ref = instructionRef;
	}

	public void setUntil(Until until) {
		this.until = until;
	}

	public void setValue(Object value) {
		this.value = value;
	}


}
