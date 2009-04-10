package com.knitml.renderer.chart.writer.impl;

import com.knitml.renderer.chart.symbol.SymbolProvider;
import com.knitml.renderer.chart.writer.ChartWriter;
import com.knitml.renderer.chart.writer.ChartWriterFactory;

public class HtmlChartWriterFactory implements ChartWriterFactory {

	public HtmlChartWriterFactory() {
	}
	
	public ChartWriter createChartWriter(SymbolProvider symbolProvider) {
		return new HtmlChartWriter(symbolProvider);
	}
	
}
