package com.knitml.core.model.directions.expression;


public class Value implements Expression {

	protected Integer value;
	
	public Object accept(Evaluator evaluator) {
		return evaluator.evaluate(this);
	}

	public Integer getValue() {
		return value;
	}
	
}
