package com.knitml.renderer.chart.writer;

import com.knitml.renderer.chart.translator.ChartElementTranslator;

public interface ChartWriterFactory {

	ChartWriter createChartWriter(ChartElementTranslator translator);
	
}
