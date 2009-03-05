package com.knitml.renderer.impl.charting;

import com.knitml.renderer.chart.ChartElementTranslator;
import com.knitml.renderer.chart.impl.LaceTextArtChartElementTranslator;

public class ChartElementTranslatorRegistry {
	
	public ChartElementTranslator getChartElementTranslator(String id) {
		return new LaceTextArtChartElementTranslator();
	}

}
