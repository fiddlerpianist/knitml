package com.knitml.renderer.impl.helpers;

public class SimpleInstruction {

	public enum Type {
		KNIT, PURL
	}

	private Type type;

	public SimpleInstruction(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return String.valueOf(getType());
	}

}
