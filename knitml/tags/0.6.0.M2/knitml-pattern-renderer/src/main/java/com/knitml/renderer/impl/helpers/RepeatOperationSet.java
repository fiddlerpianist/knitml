package com.knitml.renderer.impl.helpers;

public class RepeatOperationSet extends OperationSet {

	private String untilInstruction;
	private boolean toEnd = false;

	public RepeatOperationSet() {
		super(Type.REPEAT);
	}

	public String getUntilInstruction() {
		return untilInstruction;
	}

	public void setUntilInstruction(String untilInstruction) {
		this.untilInstruction = untilInstruction;
	}

	public void setToEnd(boolean b) {
		this.toEnd = b;
	}

	public boolean isToEnd() {
		return toEnd;
	}

	@Override
	public String toString() {
		return "{repeat=" + getOperations() + "; until=" + getUntilInstruction() + "; toEnd=" + toEnd + "}";
	}

}
