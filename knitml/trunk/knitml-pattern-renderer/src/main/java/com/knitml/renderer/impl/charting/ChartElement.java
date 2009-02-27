package com.knitml.renderer.impl.charting;

import com.knitml.core.common.Lean;

public enum ChartElement {

	K(1), P(1), SL(1), K_TRAILING(1), P_TRAILING(1), YO(1), SSK(1), K2TOG(1);

	private int width;

	ChartElement(int width) {
		this.width = width;
	}

	public int width() {
		return width;
	}

	public Lean lean() {
		switch (this) {
		case K2TOG:
			return Lean.RIGHT;
		case SSK:
			return Lean.LEFT;
		default:
			return null;
		}
	}
	
	
}
