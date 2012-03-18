package com.knitml.renderer.chart.stylesheet.impl;



public class TextArtStylesheetProvider extends
		AbstractCssProvider  {

	public String getStyleClassPrefix() {
		return "txt";
	}
	public String getStyleClassPrefix(String symbolSetId) {
		return getStyleClassPrefix();
	}

}
