package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.engine.common.CannotPutMarkerOnEndOfNeedleException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class PlaceMarkerVisitor extends AbstractValidationVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(PlaceMarkerVisitor.class);

	public void visit(Object element, KnittingContext context) throws CannotPutMarkerOnEndOfNeedleException {
		context.getEngine().placeMarker();
	}


}
