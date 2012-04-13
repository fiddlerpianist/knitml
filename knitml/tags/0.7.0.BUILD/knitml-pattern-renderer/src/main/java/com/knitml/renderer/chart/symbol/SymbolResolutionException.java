package com.knitml.renderer.chart.symbol;

import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.common.RenderingException;

public class SymbolResolutionException extends RenderingException {

	private static final long serialVersionUID = 1L;
	private SymbolProvider symbolProvider;
	private ChartElement chartElement;

	public SymbolProvider getSymbolProvider() {
		return symbolProvider;
	}
	public ChartElement getChartElement() {
		return chartElement;
	}
	public SymbolResolutionException(SymbolProvider provider, ChartElement chartElement) {
		super("Could not resolve symbol [" + chartElement.toString() + "] using symbol provider [" + provider.getClass().getName() + "]");
		this.symbolProvider = provider;
		this.chartElement = chartElement;
	}
	
	public SymbolResolutionException(SymbolProvider provider, ChartElement chartElement, String message) {
		super(message);
		this.symbolProvider = provider;
		this.chartElement = chartElement;
	}

}
