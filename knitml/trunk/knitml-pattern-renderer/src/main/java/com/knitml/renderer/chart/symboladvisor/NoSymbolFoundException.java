package com.knitml.renderer.chart.symboladvisor;

import com.knitml.renderer.chart.ChartElement;

public class NoSymbolFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoSymbolFoundException(ChartSymbolAdvisor translator, ChartElement chartElement) {
		super("Translator: " + translator.getClass().getName() + ", ChartElement: " + chartElement.toString());
	}

}
