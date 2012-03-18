package com.knitml.renderer.chart.symbol.impl;

import java.util.EnumMap
import java.util.Map

import com.knitml.renderer.chart.ChartElement
import com.knitml.renderer.chart.symbol.NoSymbolFoundException
import com.knitml.renderer.chart.symbol.Symbol
import com.knitml.renderer.chart.symbol.SymbolProvider

import static com.knitml.renderer.chart.ChartElement.*

class StitchMasterySymbolProvider implements SymbolProvider {

	public static final String DOT = "Dot";
	public static final String DOT_CABLE_EH = "DotCableEH";
	public static final String DASH = "Dash";
	public static final String DASH_CABLE_EH = "DashCableEH";
	
	protected Map<ChartElement, String> dotSymbols = new EnumMap<ChartElement, String>(ChartElement)
	protected Map<ChartElement, String> dotCableEhSymbols = new EnumMap<ChartElement, String>(ChartElement)

	public StitchMasterySymbolProvider() {
		dotSymbols[K] = 'k'
		dotSymbols[P] = 'p'
		
		dotCableEhSymbols[CBL_2_2_RC] = '>'
		dotCableEhSymbols[CBL_2_2_LC] = '?'
	}

	public Symbol getSymbol(ChartElement element) throws NoSymbolFoundException {
		def symbol = dotSymbols[element]
		if (symbol != null) {
			return new Symbol(DOT, symbol)
		}
		symbol = dotCableEhSymbols[element]
		if (symbol != null) {
			return new Symbol(DOT_CABLE_EH, symbol)
		}
		throw new NoSymbolFoundException(this, element)
	}
}
