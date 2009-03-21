package com.knitml.renderer.chart.symbol;

import com.knitml.renderer.chart.ChartElement;

public interface SymbolProvider {

	String getSymbol(ChartElement element) throws NoSymbolFoundException;
	
	
}
