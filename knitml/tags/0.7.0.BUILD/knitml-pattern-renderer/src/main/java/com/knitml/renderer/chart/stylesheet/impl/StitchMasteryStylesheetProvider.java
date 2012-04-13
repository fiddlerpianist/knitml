package com.knitml.renderer.chart.stylesheet.impl;

import com.knitml.renderer.chart.stylesheet.UnsupportedSymbolSetException;



public class StitchMasteryStylesheetProvider extends
		AbstractCssProvider {
	
	public static final String DOT = "Dot";
	public static final String DOT_CABLE = "DotCable";
	public static final String DASH = "Dash";
	public static final String DASH_CABLE = "DashCable";

	public String getStyleClassPrefix() {
		return "sm";
	}
	public String getSymbolSetName(String symbolSetId) {
		if (symbolSetId.equals(DOT))
			return "sm-font-dot";
		if (symbolSetId.equals(DASH))
			return "sm-font-dash";
		if (symbolSetId.equals(DOT_CABLE))
			return "sm-font-dotcable";
		if (symbolSetId.equals(DASH_CABLE))
			return "sm-font-dashcable";
		throw new UnsupportedSymbolSetException(symbolSetId);
	}
	

}
