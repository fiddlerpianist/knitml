package com.knitml.renderer.chart.stylesheet.impl;

import com.knitml.renderer.chart.stylesheet.UnsupportedSymbolSetException;



public class StitchMasteryStylesheetProvider extends
		AbstractCssProvider {
	
	public static final String DOT = "Dot";
	public static final String DOT_CABLE_EH = "DotCableEH";
	public static final String DASH = "Dash";
	public static final String DASH_CABLE_EH = "DashCableEH";

	public String getStyleClassPrefix() {
		return "sm";
	}
	public String getStyleClassPrefix(String symbolSetId) {
		if (symbolSetId.equals(DOT))
			return "sm-dot";
		if (symbolSetId.equals(DASH))
			return "sm-dash";
		if (symbolSetId.equals(DOT_CABLE_EH))
			return "sm-dot-cable-eh";
		if (symbolSetId.equals(DASH_CABLE_EH))
			return "sm-dash-cable-eh";
		throw new UnsupportedSymbolSetException(symbolSetId);
	}
	

}
