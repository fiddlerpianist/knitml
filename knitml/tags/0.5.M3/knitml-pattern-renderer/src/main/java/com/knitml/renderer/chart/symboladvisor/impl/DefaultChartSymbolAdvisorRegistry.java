package com.knitml.renderer.chart.symboladvisor.impl;

import java.util.List;

import com.knitml.renderer.chart.symboladvisor.ChartSymbolAdvisor;
import com.knitml.renderer.chart.symboladvisor.ChartSymbolAdvisorRegistry;

public class DefaultChartSymbolAdvisorRegistry implements ChartSymbolAdvisorRegistry {
	
	private List<ChartSymbolAdvisor> translators;
	
	public DefaultChartSymbolAdvisorRegistry(List<ChartSymbolAdvisor> translators) {
		this.translators = translators;
	}
	
	public ChartSymbolAdvisor getChartElementTranslator(String id) {
		return translators.get(0);
	}

}
