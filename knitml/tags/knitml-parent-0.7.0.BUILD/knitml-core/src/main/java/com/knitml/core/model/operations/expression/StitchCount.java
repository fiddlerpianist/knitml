package com.knitml.core.model.operations.expression;

import java.util.List;

import com.knitml.core.model.common.Needle;

public class StitchCount implements Expression {

	protected List<Needle> needles;

	public Object accept(Evaluator evaluator) {
		return evaluator.evaluate(this);
	}

	public List<Needle> getNeedles() {
		return needles;
	}
	
}
