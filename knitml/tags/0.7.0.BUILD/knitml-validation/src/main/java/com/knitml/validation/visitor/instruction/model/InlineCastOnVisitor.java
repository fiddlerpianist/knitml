package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.InlineCastOn;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class InlineCastOnVisitor extends AbstractPatternVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(InlineCastOnVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		InlineCastOn operation = (InlineCastOn) element;
		context.getEngine().castOn(operation);
	}


}
