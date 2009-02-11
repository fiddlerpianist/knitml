package com.knitml.core.model.directions.inline;

import java.util.List;

import com.knitml.core.common.EnumUtils;
import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.InlineOperation;

public class Repeat implements InlineOperation, CompositeOperation {

	public enum Until {
		END, BEFORE_END, BEFORE_GAP, BEFORE_MARKER, MARKER, TIMES;
		public String getCanonicalName() {
			return EnumUtils.fromEnum(this);
		}

	}

	protected Until until;
	protected Integer value;
	protected List<InlineOperation> operations;

	public List<InlineOperation> getOperations() {
		return operations;
	}

	public Until getUntil() {
		return until;
	}

	public Integer getValue() {
		return value;
	}
	

}
