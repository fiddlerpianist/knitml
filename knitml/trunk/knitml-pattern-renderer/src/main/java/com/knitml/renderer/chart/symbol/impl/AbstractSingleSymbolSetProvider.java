package com.knitml.renderer.chart.symbol.impl;

import java.util.EnumMap;
import java.util.Map;

import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.symbol.NoSymbolFoundException;
import com.knitml.renderer.chart.symbol.Symbol;
import com.knitml.renderer.chart.symbol.SymbolProvider;

public abstract class AbstractSingleSymbolSetProvider implements SymbolProvider {

	protected Map<ChartElement, String> symbols = new EnumMap<ChartElement, String>(ChartElement.class);
	private static final String DEFAULT_SET_ID = "default";

	public AbstractSingleSymbolSetProvider() {
		initializeSymbols();
	}
	
	public final Symbol getSymbol(ChartElement element) throws NoSymbolFoundException {
		String symbol = symbols.get(element);
		if (symbol == null) {
			throw new NoSymbolFoundException(this, element);
		}
		return new Symbol(DEFAULT_SET_ID, symbol);
	}	

	protected abstract void initializeSymbols();
	
}
