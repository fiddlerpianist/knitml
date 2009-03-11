package com.knitml.renderer.chart.writer.impl;

import com.knitml.renderer.chart.translator.ChartElementTranslator;
import com.knitml.renderer.chart.writer.ChartWriter;
import com.knitml.renderer.chart.writer.ChartWriterFactory;

public class TextArtChartWriterFactory implements ChartWriterFactory {

	public ChartWriter createChartWriter(ChartElementTranslator translator) {
		return new TextArtChartWriter(translator);
	}

}
