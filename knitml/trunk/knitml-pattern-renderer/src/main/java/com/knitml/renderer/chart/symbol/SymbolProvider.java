package com.knitml.renderer.chart.symbol;

import com.knitml.renderer.chart.ChartElement;

public interface SymbolProvider {

	Symbol getSymbol(ChartElement element) throws NoSymbolFoundException;
	
}
