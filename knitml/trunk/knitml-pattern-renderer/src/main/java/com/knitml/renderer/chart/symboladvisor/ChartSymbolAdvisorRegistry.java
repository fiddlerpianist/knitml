package com.knitml.renderer.chart.symboladvisor;

public interface ChartSymbolAdvisorRegistry {

	ChartSymbolAdvisor getChartElementTranslator(String id);

}