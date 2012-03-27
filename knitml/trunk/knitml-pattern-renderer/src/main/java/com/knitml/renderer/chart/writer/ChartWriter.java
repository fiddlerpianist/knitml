package com.knitml.renderer.chart.writer;

import java.io.Writer;

import com.knitml.renderer.chart.Chart;
import com.knitml.renderer.chart.symbol.SymbolResolutionException;
import com.knitml.renderer.context.RenderingContext;

public interface ChartWriter {

	void writeChart(Chart chart, Writer writer, RenderingContext context) throws SymbolResolutionException;
	
}