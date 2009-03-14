package com.knitml.renderer.chart.writer;

import com.knitml.renderer.chart.symboladvisor.ChartSymbolAdvisor;

public interface ChartWriterFactory {

	ChartWriter createChartWriter(ChartSymbolAdvisor translator);
	
}
