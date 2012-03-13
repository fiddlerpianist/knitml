package com.knitml.renderer.chart.writer.impl;

import com.knitml.renderer.chart.symbol.SymbolProvider;
import com.knitml.renderer.chart.writer.ChartWriter;
import com.knitml.renderer.chart.writer.ChartWriterFactory;

public class NoOpChartWriterFactory implements ChartWriterFactory {

	public ChartWriter createChartWriter(SymbolProvider symbolProvider) {
		return new NoOpChartWriter();
	}

}
