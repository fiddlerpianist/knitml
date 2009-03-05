package com.knitml.renderer.chart.impl;

import static com.knitml.renderer.chart.ChartElement.K;
import static com.knitml.renderer.chart.ChartElement.K2TOG;
import static com.knitml.renderer.chart.ChartElement.NS;
import static com.knitml.renderer.chart.ChartElement.P;
import static com.knitml.renderer.chart.ChartElement.SL;
import static com.knitml.renderer.chart.ChartElement.SSK;
import static com.knitml.renderer.chart.ChartElement.YO;

import java.util.HashMap;
import java.util.Map;

import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.ChartElementTranslator;

public class LaceTextArtChartElementTranslator implements ChartElementTranslator {

	private static Map<ChartElement, String> symbols = new HashMap<ChartElement, String>();

	static {
		symbols.put(K, "-");
		symbols.put(P, "=");
		symbols.put(K2TOG, "/");
		symbols.put(SSK, "\\");
		symbols.put(YO, "o");
		symbols.put(NS, "x");
		symbols.put(SL, "S");
	}

	public String getSymbol(ChartElement element) {
		return symbols.get(element);
	}

}
