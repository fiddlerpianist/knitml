package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.FromStitchHolder;
import com.knitml.engine.Needle;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class FromStitchHolderVisitor extends AbstractPatternVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(FromStitchHolderVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		FromStitchHolder spec = (FromStitchHolder) element;
		Needle stitchHolder = context.getPatternRepository().getNeedle(spec.getStitchHolder().getId());
		stitchHolder.setDirection(context.getEngine().getDirection());
		stitchHolder.startAtBeginning();
		context.getEngine().imposeNeedle(stitchHolder);
		visitChildren(spec, context);
		context.getEngine().unimposeNeedle();
	}


}
