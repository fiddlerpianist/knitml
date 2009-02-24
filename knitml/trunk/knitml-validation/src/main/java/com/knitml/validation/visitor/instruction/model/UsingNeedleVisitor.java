package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.UsingNeedle;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.Needle;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.NeedleNotFoundException;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;
import com.knitml.validation.visitor.util.NeedleUtils;

public class UsingNeedleVisitor extends AbstractPatternVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(UsingNeedleVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		UsingNeedle operation = (UsingNeedle)element; 
		Needle engineNeedle = NeedleUtils.lookupNeedle(operation.getNeedle().getId(), context);
		if (engineNeedle == null) {
			throw new NeedleNotFoundException(operation.getNeedle().getId());
		}
		context.setNeedleInstructions(true);
		visitChildren(operation, context);
		KnittingEngine engine = context.getEngine();
		if (!engine.isEndOfRow() && engine.getStitchesRemainingOnCurrentNeedle() != 0) {
			throw new KnittingEngineException("<using-needle> instruction either does not take work to end of that needle or takes it past the needle");
		}
		
		if (!engine.isEndOfRow()) {
			// there are no more stitches on this needle, but there is another needle with more stitches on it
			engine.advanceNeedle();
		}
		context.setNeedleInstructions(false);
	}


}
