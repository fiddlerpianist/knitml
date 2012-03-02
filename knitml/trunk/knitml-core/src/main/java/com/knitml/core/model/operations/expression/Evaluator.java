package com.knitml.core.model.operations.expression;

public interface Evaluator {
	Object evaluate(StitchCount stitchCount);
	Object evaluate(Value value);
}
