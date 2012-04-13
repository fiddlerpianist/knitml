package com.knitml.renderer.impl.helpers;

public class FromStitchHolderOperationSet extends OperationSet {

	private String label;

	public FromStitchHolderOperationSet() {
		super(Type.FROM_STITCH_HOLDER);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
