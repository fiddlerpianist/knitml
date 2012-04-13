package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.SlipToStitchHolder;
import com.knitml.engine.Needle;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class SlipToStitchHolderVisitor extends AbstractPatternVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(SlipToStitchHolderVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		SlipToStitchHolder spec = (SlipToStitchHolder) element;
		Needle stitchHolder = context.getPatternRepository().getNeedle(spec.getStitchHolder().getId());
		context.getEngine().transferStitchesToNeedle(stitchHolder, spec.getNumberOfStitches());
	}


}
