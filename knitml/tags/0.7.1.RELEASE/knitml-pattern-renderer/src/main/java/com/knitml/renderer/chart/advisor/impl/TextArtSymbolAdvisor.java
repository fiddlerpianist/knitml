package com.knitml.renderer.chart.advisor.impl;

import com.knitml.renderer.chart.advisor.ChartSymbolAdvisor;
import com.knitml.renderer.chart.stylesheet.impl.TextArtStylesheetProvider;
import com.knitml.renderer.chart.symbol.impl.TextArtSymbolProvider;

public class TextArtSymbolAdvisor extends ChartSymbolAdvisor {

	public TextArtSymbolAdvisor() {
		super(new TextArtSymbolProvider(), new TextArtStylesheetProvider());
	}

}
