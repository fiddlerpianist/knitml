package com.knitml.renderer.chart.symbol.impl;

import java.util.EnumMap;
import java.util.Map;

import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.symbol.NoSymbolFoundException;
import com.knitml.renderer.chart.symbol.SymbolProvider;

public abstract class AbstractSymbolProvider implements SymbolProvider {

	protected Map<ChartElement, String> symbols = new EnumMap<ChartElement, String>(ChartElement.class);

	public AbstractSymbolProvider() {
		initializeSymbols();
	}
	
	public final String getSymbol(ChartElement element) throws NoSymbolFoundException {
		String symbol = symbols.get(element);
		if (symbol == null) {
			throw new NoSymbolFoundException(this, element);
		}
		return symbol;
	}	

	protected abstract void initializeSymbols();
	
}
