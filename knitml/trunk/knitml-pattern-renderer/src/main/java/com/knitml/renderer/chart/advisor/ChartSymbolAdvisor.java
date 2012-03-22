package com.knitml.renderer.chart.advisor;

import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.stylesheet.StylesheetProvider;
import com.knitml.renderer.chart.symbol.SymbolResolutionException;
import com.knitml.renderer.chart.symbol.Symbol;
import com.knitml.renderer.chart.symbol.SymbolProvider;


public class ChartSymbolAdvisor implements SymbolProvider, StylesheetProvider {

	private SymbolProvider symbolProvider;
	private StylesheetProvider stylesheetProvider;

	public ChartSymbolAdvisor(SymbolProvider symbolProvider,
			StylesheetProvider stylesheetProvider) {
		this.symbolProvider = symbolProvider;
		this.stylesheetProvider = stylesheetProvider;
	}

	public Symbol getSymbol(ChartElement element) throws SymbolResolutionException {
		return symbolProvider.getSymbol(element);
	}

	public String getMimeType() {
		return stylesheetProvider.getMimeType();
	}

	public String getStyleClassPrefix() {
		return stylesheetProvider.getStyleClassPrefix();
	}

	public String getStyleClassPrefix(String symbolSetId) {
		return stylesheetProvider.getStyleClassPrefix(symbolSetId);
	}
	
	public String getStylesheet() {
		return stylesheetProvider.getStylesheet();
	}
	
	
	
}
