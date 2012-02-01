package com.knitml.core.model.directions.expression;

public interface Evaluator {
	Object evaluate(StitchCount stitchCount);
	Object evaluate(Value value);
}
