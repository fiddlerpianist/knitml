package com.knitml.renderer.chart.legend;

import java.util.List;

import com.knitml.core.model.operations.DiscreteInlineOperation;

public interface LegendOperationResolver {

	String resolveLegendFor(List<DiscreteInlineOperation> operations);

}
