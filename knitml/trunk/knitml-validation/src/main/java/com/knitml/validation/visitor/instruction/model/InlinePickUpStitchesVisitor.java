package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.InlinePickUpStitches;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class InlinePickUpStitchesVisitor extends AbstractValidationVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(InlinePickUpStitchesVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		InlinePickUpStitches operation = (InlinePickUpStitches) element;
		context.getEngine().pickUpStitches(operation);
	}


}
