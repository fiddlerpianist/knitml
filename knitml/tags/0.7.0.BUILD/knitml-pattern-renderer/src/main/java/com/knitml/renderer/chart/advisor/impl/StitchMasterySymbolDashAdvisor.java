package com.knitml.renderer.chart.advisor.impl;

import com.knitml.renderer.chart.advisor.ChartSymbolAdvisor;
import com.knitml.renderer.chart.stylesheet.impl.StitchMasteryStylesheetProvider;
import com.knitml.renderer.chart.symbol.impl.StitchMasterySymbolProvider;
import com.knitml.renderer.chart.symbol.impl.StitchMasterySymbolProvider.Mode;

public class StitchMasterySymbolDashAdvisor extends ChartSymbolAdvisor {

	public StitchMasterySymbolDashAdvisor() {
		super(new StitchMasterySymbolProvider(Mode.DASH), new StitchMasteryStylesheetProvider());
	}

}
