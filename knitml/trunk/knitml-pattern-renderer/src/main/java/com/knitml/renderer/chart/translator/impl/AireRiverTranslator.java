package com.knitml.renderer.chart.translator.impl;

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
import com.knitml.renderer.chart.translator.ChartElementTranslator;

public class AireRiverTranslator extends
		AbstractHtmlStylesheetProvider implements ChartElementTranslator {

	private static Map<ChartElement, String> symbols = new HashMap<ChartElement, String>();

	static {
		symbols.put(K, "g");
		symbols.put(P, "h");
		symbols.put(K2TOG, "|");
		symbols.put(SSK, "\\");
		symbols.put(YO, "j");
		symbols.put(NS, "z");
		symbols.put(SL, ",");
	}

	public String getSymbol(ChartElement element) {
		return symbols.get(element);
	}

	public String getStyleClassPrefix() {
		return "ar";
	}

	protected String getRelativePathToCssFile() {
		return "ar.css";
	}
}
