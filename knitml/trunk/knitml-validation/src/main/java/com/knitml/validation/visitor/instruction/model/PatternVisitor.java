package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.Pattern;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class PatternVisitor extends AbstractValidationVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(PatternVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		Pattern pattern = (Pattern) element;
		visitChild(pattern.getDirectives(), context);
		visitChild(pattern.getGeneralInformation(), context);
		visitChild(pattern.getSupplies(), context);
		visitChild(pattern.getDirections(), context);
	}
	
}
