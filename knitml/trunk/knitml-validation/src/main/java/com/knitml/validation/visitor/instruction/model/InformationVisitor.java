package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.information.Information;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.Visitor;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class InformationVisitor extends AbstractValidationVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(InformationVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		Information information = (Information) element;
		for (Object detail : information.getDetails()) {
			Visitor visitor = getVisitorFactory().findVisitorFromClassName(detail);
			visitor.visit(detail, context);
		}
	}


}
