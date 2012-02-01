package com.knitml.renderer.chart.advisor;

import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.stylesheet.StylesheetProvider;
import com.knitml.renderer.chart.symbol.SymbolProvider;
import com.knitml.renderer.chart.symbol.NoSymbolFoundException;


public class ChartSymbolAdvisor implements SymbolProvider, StylesheetProvider {

	private SymbolProvider symbolProvider;
	private StylesheetProvider stylesheetProvider;

	public ChartSymbolAdvisor(SymbolProvider symbolProvider,
			StylesheetProvider stylesheetProvider) {
		this.symbolProvider = symbolProvider;
		this.stylesheetProvider = stylesheetProvider;
	}

	public String getSymbol(ChartElement element) throws NoSymbolFoundException {
		return symbolProvider.getSymbol(element);
	}

	public String getMimeType() {
		return stylesheetProvider.getMimeType();
	}

	public String getStyleClassPrefix() {
		return stylesheetProvider.getStyleClassPrefix();
	}

	public String getStylesheet() {
		return stylesheetProvider.getStylesheet();
	}
	
	
	
}
