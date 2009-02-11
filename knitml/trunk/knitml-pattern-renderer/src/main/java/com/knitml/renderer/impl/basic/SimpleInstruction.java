package com.knitml.renderer.impl.basic;

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

}
