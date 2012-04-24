package com.knitml.renderer.chart.stylesheet.impl;


public class KnittersSymbolsWStylesheetProvider extends
		AbstractCssProvider {

	public String getStyleClassPrefix() {
		return "ks";
	}
	public String getSymbolSetName(String symbolSetId) {
		return "ks-font";
	}

}
