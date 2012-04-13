package com.knitml.core.model.operations.expression;

public interface Expression {
	Object accept(Evaluator evaluator);
}
