package com.knitml.renderer.chart.translator;

import com.knitml.renderer.chart.ChartElement;

public class NoSymbolFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoSymbolFoundException(ChartElementTranslator translator, ChartElement chartElement) {
		super("Translator: " + translator.getClass().getName() + ", ChartElement: " + chartElement.toString());
	}

}
