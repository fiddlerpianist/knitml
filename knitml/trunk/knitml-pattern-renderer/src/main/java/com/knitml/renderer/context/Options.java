package com.knitml.renderer.context;

import java.util.HashSet;
import java.util.Set;

public class Options {
	
	private boolean chartWheneverPossible = true;
	private Set<String> instructionsToChart = new HashSet<String>();
	
	public boolean isChartWheneverPossible() {
		return chartWheneverPossible;
	}
	public void setChartWheneverPossible(boolean chartWheneverPossible) {
		this.chartWheneverPossible = chartWheneverPossible;
	}
	public Set<String> getInstructionsToChart() {
		return instructionsToChart;
	}
	public void setInstructionsToChart(Set<String> instructionsToChart) {
		this.instructionsToChart = instructionsToChart;
	}
	
}
