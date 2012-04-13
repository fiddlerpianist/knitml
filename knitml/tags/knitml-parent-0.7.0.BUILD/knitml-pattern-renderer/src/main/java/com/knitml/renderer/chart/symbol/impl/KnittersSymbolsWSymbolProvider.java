package com.knitml.renderer.chart.symbol.impl;

import static com.knitml.renderer.chart.ChartElement.K;
import static com.knitml.renderer.chart.ChartElement.K2TOG;
import static com.knitml.renderer.chart.ChartElement.NS;
import static com.knitml.renderer.chart.ChartElement.P;
import static com.knitml.renderer.chart.ChartElement.SL;
import static com.knitml.renderer.chart.ChartElement.SSK;
import static com.knitml.renderer.chart.ChartElement.YO;

public class KnittersSymbolsWSymbolProvider extends
		AbstractSingleSymbolSetProvider {

	@Override
	protected void initializeSymbols() {
		symbols.put(K, "a");
		symbols.put(P, "P");
		symbols.put(K2TOG, "/");
		symbols.put(SSK, "\\");
		symbols.put(YO, "o");
		symbols.put(NS, "C");
		symbols.put(SL, "S");
	}

}
