package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.PickUpStitches;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class PickUpStitchesVisitor extends AbstractPatternVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(PickUpStitchesVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		PickUpStitches spec = (PickUpStitches) element;
		// treat pick ups as a cast on that don't count as a row
		context.getEngine().castOn(spec.getNumberOfTimes(), false); 
	}


}
