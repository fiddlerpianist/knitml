package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.EnumUtils;
import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.core.model.operations.InlineOperation;

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
	
	public Repeat() {
	}
	
	public Repeat(Until until, Integer value) {
		this.until = until;
		this.value = value;
		this.operations = new ArrayList<InlineOperation>();
	}

	public List<InlineOperation> getOperations() {
		return operations;
	}

	public Until getUntil() {
		return until;
	}

	public Integer getValue() {
		return value;
	}

	public void setUntil(Until until) {
		this.until = until;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public void setOperations(List<InlineOperation> operations) {
		this.operations = operations;
	}

	@Override
	public String toString() {
		return "Repeat " + this.operations + ", until [" + (this.value != null ? this.value + " " : "") + this.until + "]";
	}
	
}
