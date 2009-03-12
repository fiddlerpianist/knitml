package com.knitml.renderer.chart.writer.impl;

import com.knitml.renderer.chart.translator.ChartElementTranslator;
import com.knitml.renderer.chart.translator.FontBasedChartElementTranslator;
import com.knitml.renderer.chart.writer.ChartWriter;
import com.knitml.renderer.chart.writer.ChartWriterFactory;

public class FontBasedHtmlChartWriterFactory implements ChartWriterFactory {

	public ChartWriter createChartWriter(ChartElementTranslator translator) {
		if (!(translator instanceof FontBasedChartElementTranslator)) {
			throw new IllegalArgumentException("The supplied ChartElementTranslator must be a FontBasedChartElementTranslator to use this ChartWriter");
		}
		return new FontBasedHtmlChartWriter((FontBasedChartElementTranslator)translator);
	}

}
