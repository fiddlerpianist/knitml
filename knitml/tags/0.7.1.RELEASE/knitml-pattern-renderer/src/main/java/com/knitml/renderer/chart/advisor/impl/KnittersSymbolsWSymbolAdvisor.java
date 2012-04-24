package com.knitml.renderer.chart.advisor.impl;

import com.knitml.renderer.chart.advisor.ChartSymbolAdvisor;
import com.knitml.renderer.chart.stylesheet.impl.KnittersSymbolsWStylesheetProvider;
import com.knitml.renderer.chart.symbol.impl.KnittersSymbolsWSymbolProvider;

public class KnittersSymbolsWSymbolAdvisor extends ChartSymbolAdvisor {

	public KnittersSymbolsWSymbolAdvisor() {
		super(new KnittersSymbolsWSymbolProvider(), new KnittersSymbolsWStylesheetProvider());
	}

}
