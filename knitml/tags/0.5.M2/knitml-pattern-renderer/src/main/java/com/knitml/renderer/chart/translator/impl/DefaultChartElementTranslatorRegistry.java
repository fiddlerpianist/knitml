package com.knitml.renderer.chart.translator.impl;

import java.util.List;

import com.knitml.renderer.chart.translator.ChartElementTranslator;
import com.knitml.renderer.chart.translator.ChartElementTranslatorRegistry;

public class DefaultChartElementTranslatorRegistry implements ChartElementTranslatorRegistry {
	
	private List<ChartElementTranslator> translators;
	
	public DefaultChartElementTranslatorRegistry(List<ChartElementTranslator> translators) {
		this.translators = translators;
	}
	
	public ChartElementTranslator getChartElementTranslator(String id) {
		return translators.get(0);
	}

}
