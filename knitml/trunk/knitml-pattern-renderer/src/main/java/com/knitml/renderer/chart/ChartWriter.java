package com.knitml.renderer.chart;

import java.io.Writer;

public interface ChartWriter {

	void writeChart(Chart chart, Writer writer);
	
}