package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.IncreaseIntoNextStitch;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class IncreaseIntoNextStitchVisitor extends AbstractPatternVisitor {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(IncreaseIntoNextStitchVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		IncreaseIntoNextStitch increase = (IncreaseIntoNextStitch) element;
		context.getEngine().startWorkingIntoNextStitch();
		visitChildren(increase, context);
		context.getEngine().endWorkingIntoNextStitch();
	}

}
