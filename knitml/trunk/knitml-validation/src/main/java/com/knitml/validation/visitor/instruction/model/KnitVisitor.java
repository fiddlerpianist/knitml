package com.knitml.validation.visitor.instruction.model;

import com.knitml.core.model.directions.inline.Knit;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class KnitVisitor extends AbstractValidationVisitor {

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		Knit knit = (Knit)element;
		int count = knit.getNumberOfTimes() == null ? 1 : knit.getNumberOfTimes();
		context.getEngine().knit(count);
	}

}
