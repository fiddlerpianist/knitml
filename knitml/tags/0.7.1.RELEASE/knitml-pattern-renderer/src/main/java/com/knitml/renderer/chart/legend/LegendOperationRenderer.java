package com.knitml.renderer.chart.legend;

import java.util.Map;

import com.knitml.core.common.Side;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.renderer.chart.ChartElement;

public interface LegendOperationRenderer {

	String resolveLegendFor(ChartElement element, Map<Side, DiscreteInlineOperation> operations);

}
