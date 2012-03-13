package com.knitml.renderer.chart.advisor.impl;

import com.knitml.renderer.chart.advisor.ChartSymbolAdvisor;
import com.knitml.renderer.chart.stylesheet.impl.StitchMasteryStylesheetProvider;
import com.knitml.renderer.chart.symbol.impl.StitchMasterySymbolProvider;

public class StitchMasterySymbolAdvisor extends ChartSymbolAdvisor {

	public StitchMasterySymbolAdvisor() {
		super(new StitchMasterySymbolProvider(), new StitchMasteryStylesheetProvider());
	}

}
