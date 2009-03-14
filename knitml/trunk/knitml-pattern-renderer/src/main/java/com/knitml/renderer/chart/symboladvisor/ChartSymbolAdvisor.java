package com.knitml.renderer.chart.symboladvisor;

import com.knitml.renderer.chart.ChartElement;

public interface ChartSymbolAdvisor {

	String getSymbol(ChartElement element) throws NoSymbolFoundException;
	
	
}
