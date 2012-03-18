package com.knitml.renderer.chart.stylesheet.impl;


public class KnittersSymbolsWStylesheetProvider extends
		AbstractCssProvider {

	public String getStyleClassPrefix() {
		return "ks";
	}
	public String getStyleClassPrefix(String symbolSetId) {
		return getStyleClassPrefix();
	}

}
