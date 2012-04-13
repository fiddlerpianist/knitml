package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.MultipleDecrease;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class MultipleDecreaseVisitor extends AbstractPatternVisitor {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(MultipleDecreaseVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		context.getEngine().decrease((MultipleDecrease)element);
	}
	
}
