package com.knitml.validation.visitor.instruction.model;

import java.util.List;

import com.knitml.core.model.common.Needle;
import com.knitml.core.model.operations.expression.Evaluator;
import com.knitml.core.model.operations.expression.Expression;
import com.knitml.core.model.operations.expression.StitchCount;
import com.knitml.core.model.operations.expression.Value;
import com.knitml.validation.context.KnittingContext;

public class EqualsEvaluator implements Evaluator {
	
	private KnittingContext knittingContext;
	
	public EqualsEvaluator(KnittingContext knittingContext) {
		this.knittingContext = knittingContext;
	}
	
	public boolean areEqual(List<Expression> expressions) {
		Object[] results = new Object[expressions.size()];
		for (int i=0; i < expressions.size(); i++) {
			results[i] = expressions.get(i).accept(this);
		}
		for (int i=0; i < results.length - 1; i++) {
			boolean intermediateEquals = results[i].equals(results[i+1]);
			if (!intermediateEquals) {
				return false;
			}
		}
		return true;
	}

	public Object evaluate(StitchCount stitchCount) {
		int stitchCounter = 0;
		for (Needle needle : stitchCount.getNeedles()) {
			com.knitml.engine.Needle engineNeedle = knittingContext.getPatternRepository().getNeedle(needle.getId());
			stitchCounter += engineNeedle.getTotalStitches();
		}
		return stitchCounter;
	}

	public Object evaluate(Value value) {
		return value.getValue();
	}

}
