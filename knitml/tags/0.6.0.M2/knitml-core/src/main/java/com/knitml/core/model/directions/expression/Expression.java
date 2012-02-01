package com.knitml.core.model.directions.expression;

public interface Expression {
	Object accept(Evaluator evaluator);
}
