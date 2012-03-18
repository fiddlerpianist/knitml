package com.knitml.renderer.chart.stylesheet.impl;



public class AireRiverStylesheetProvider extends
		AbstractCssProvider  {

	public String getStyleClassPrefix() {
		return "ar";
	}
	public String getStyleClassPrefix(String symbolSetId) {
		return getStyleClassPrefix();
	}

}
