package com.knitml.renderer.chart;


public enum ChartElement {

	K(1), P(1), SL(1), K_TW(1), P_TW(1), YO(1), M1(1), SSK(1), K2TOG(1), SSP(1), P2TOG(
			1), SKP(1), K2TOG_TBL(1), P2TOG_TBL(1), SSSK(1), K3TOG(1), P3TOG(1), CDD(
			1), SK2P(1), DECREASE(1), NS(1);

	private int width;

	ChartElement(int width) {
		this.width = width;
	}

	public int width() {
		return width;
	}
}
