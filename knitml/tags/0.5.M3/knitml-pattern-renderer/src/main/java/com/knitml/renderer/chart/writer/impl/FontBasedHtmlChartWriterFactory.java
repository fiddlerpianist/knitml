package com.knitml.renderer.chart.writer.impl;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.knitml.renderer.chart.symboladvisor.ChartSymbolAdvisor;
import com.knitml.renderer.chart.writer.ChartWriter;
import com.knitml.renderer.chart.writer.ChartWriterFactory;

public class FontBasedHtmlChartWriterFactory implements ChartWriterFactory {

	public Resource styleResource = new ClassPathResource(this.getClass().getPackage().getName());
	
	public FontBasedHtmlChartWriterFactory() {
		
	}
	
	public ChartWriter createChartWriter(ChartSymbolAdvisor translator) {
		return new FontBasedHtmlChartWriter(translator);
	}
	
}
