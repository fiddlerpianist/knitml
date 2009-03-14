package com.knitml.renderer.chart.writer.impl;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.knitml.renderer.chart.translator.ChartElementTranslator;
import com.knitml.renderer.chart.writer.ChartWriter;
import com.knitml.renderer.chart.writer.ChartWriterFactory;

public class FontBasedHtmlChartWriterFactory implements ChartWriterFactory {

	public Resource styleResource = new ClassPathResource(this.getClass().getPackage().getName());
	
	public FontBasedHtmlChartWriterFactory() {
		
	}
	
	public ChartWriter createChartWriter(ChartElementTranslator translator) {
		return new FontBasedHtmlChartWriter(translator);
	}
	
}
