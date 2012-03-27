package com.knitml.renderer.chart.legend.impl;

import java.util.Map;

import com.knitml.core.common.Side;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.legend.LegendOperationRenderer;

public class SimpleLegendOperationRenderer implements LegendOperationRenderer {
	
	public String resolveLegendFor(ChartElement element, Map<Side,DiscreteInlineOperation> operations) {
		return element.toString().toLowerCase();
	}
	
}
