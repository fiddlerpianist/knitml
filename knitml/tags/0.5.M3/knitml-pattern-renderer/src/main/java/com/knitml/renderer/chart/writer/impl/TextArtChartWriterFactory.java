package com.knitml.renderer.chart.writer.impl;

import com.knitml.renderer.chart.symboladvisor.ChartSymbolAdvisor;
import com.knitml.renderer.chart.writer.ChartWriter;
import com.knitml.renderer.chart.writer.ChartWriterFactory;

public class TextArtChartWriterFactory implements ChartWriterFactory {

	public ChartWriter createChartWriter(ChartSymbolAdvisor translator) {
		return new TextArtChartWriter(translator);
	}

}
