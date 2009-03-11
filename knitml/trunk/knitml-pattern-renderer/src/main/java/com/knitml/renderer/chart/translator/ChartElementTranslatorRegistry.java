package com.knitml.renderer.chart.translator;

public interface ChartElementTranslatorRegistry {

	ChartElementTranslator getChartElementTranslator(String id);

}