package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.engine.common.CannotPutMarkerOnEndOfNeedleException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class PlaceMarkerVisitor extends AbstractPatternVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(PlaceMarkerVisitor.class);

	public void visit(Object element, KnittingContext context) throws CannotPutMarkerOnEndOfNeedleException {
		context.getEngine().placeMarker();
	}


}
