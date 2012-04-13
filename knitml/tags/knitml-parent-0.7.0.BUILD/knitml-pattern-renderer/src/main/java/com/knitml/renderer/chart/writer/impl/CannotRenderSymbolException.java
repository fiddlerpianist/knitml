package com.knitml.renderer.chart.writer.impl;

import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.symbol.SymbolProvider;
import com.knitml.renderer.chart.symbol.SymbolResolutionException;

public class CannotRenderSymbolException extends SymbolResolutionException {

	public CannotRenderSymbolException(String message, SymbolProvider provider,
			ChartElement chartElement) {
		super(provider, chartElement, message);
	}

	private static final long serialVersionUID = 1L;


}
