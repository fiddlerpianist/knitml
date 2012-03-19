package com.knitml.renderer.chart.writer.impl;

import java.io.Writer;

import com.knitml.renderer.chart.Chart;
import com.knitml.renderer.chart.symbol.NoSymbolFoundException;
import com.knitml.renderer.chart.writer.ChartWriter;

public class NoOpChartWriter implements ChartWriter {

	public void writeChart(Chart chart, Writer writer)
			throws NoSymbolFoundException {
		// no-op!
	}

}