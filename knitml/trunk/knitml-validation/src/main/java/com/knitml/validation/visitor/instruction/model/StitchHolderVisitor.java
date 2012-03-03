package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.NeedleStyle;
import com.knitml.core.model.common.StitchHolder;
import com.knitml.engine.Needle;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class StitchHolderVisitor extends AbstractPatternVisitor {

	private final static Logger log = LoggerFactory
			.getLogger(StitchHolderVisitor.class);

	public void visit(Object element, KnittingContext context) {
		StitchHolder stitchHolder = (StitchHolder)element;
		Needle stitchHolderNeedle = context.getKnittingFactory().createNeedle(stitchHolder.getId(), NeedleStyle.CIRCULAR);
		context.getPatternRepository().addNeedle(stitchHolderNeedle);
		log.debug("Added stitch holder [{}] to the pattern repository", stitchHolderNeedle); //$NON-NLS-1$
	}

}
