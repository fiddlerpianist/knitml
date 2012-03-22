package com.knitml.renderer.chart.writer;

import java.io.Writer;

import com.knitml.renderer.chart.Chart;
import com.knitml.renderer.chart.symbol.SymbolResolutionException;

public interface ChartWriter {

	void writeChart(Chart chart, Writer writer) throws SymbolResolutionException;
	
}