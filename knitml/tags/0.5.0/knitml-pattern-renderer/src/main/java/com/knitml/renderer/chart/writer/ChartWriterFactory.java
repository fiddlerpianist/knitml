package com.knitml.renderer.chart.writer;

import com.knitml.renderer.chart.symbol.SymbolProvider;

public interface ChartWriterFactory {

	ChartWriter createChartWriter(SymbolProvider symbolProvider);
	
}
