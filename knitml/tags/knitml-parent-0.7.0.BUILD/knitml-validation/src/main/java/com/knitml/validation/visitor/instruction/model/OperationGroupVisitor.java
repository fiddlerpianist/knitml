package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.OperationGroup;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class OperationGroupVisitor extends AbstractPatternVisitor {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(OperationGroupVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		visitChildren((OperationGroup)element, context);
	}

}
