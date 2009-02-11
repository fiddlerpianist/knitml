package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.engine.common.NotBetweenRowsException;
import com.knitml.engine.common.WrongNeedleTypeException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class JoinInRoundVisitor extends AbstractValidationVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(JoinInRoundVisitor.class);

	public void visit(Object element, KnittingContext context) throws WrongNeedleTypeException, NotBetweenRowsException, IllegalArgumentException {
		context.getEngine().declareRoundKnitting();
	}


}
