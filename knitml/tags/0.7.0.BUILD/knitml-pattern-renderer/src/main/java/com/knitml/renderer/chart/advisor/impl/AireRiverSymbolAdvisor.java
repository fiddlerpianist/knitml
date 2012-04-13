package com.knitml.renderer.chart.advisor.impl;

import com.knitml.renderer.chart.advisor.ChartSymbolAdvisor;
import com.knitml.renderer.chart.stylesheet.impl.AireRiverStylesheetProvider;
import com.knitml.renderer.chart.symbol.impl.AireRiverSymbolProvider;

public class AireRiverSymbolAdvisor extends ChartSymbolAdvisor {

	public AireRiverSymbolAdvisor() {
		super(new AireRiverSymbolProvider(), new AireRiverStylesheetProvider());
	}

}
