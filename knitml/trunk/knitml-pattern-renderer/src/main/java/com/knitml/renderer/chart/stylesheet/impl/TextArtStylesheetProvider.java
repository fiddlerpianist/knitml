package com.knitml.renderer.chart.stylesheet.impl;



public class TextArtStylesheetProvider extends
		AbstractCssProvider  {

	public String getStyleClassPrefix() {
		return "txt";
	}
	public String getSymbolSetName(String symbolSetId) {
		return getStyleClassPrefix();
	}

}
