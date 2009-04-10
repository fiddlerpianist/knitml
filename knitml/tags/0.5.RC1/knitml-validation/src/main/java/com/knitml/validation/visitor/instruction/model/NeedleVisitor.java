package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.header.Needle;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class NeedleVisitor extends AbstractPatternVisitor {

	private final static Logger log = LoggerFactory
			.getLogger(NeedleVisitor.class);

	public void visit(Object element, KnittingContext context) {
		Needle needle = (Needle)element;
		com.knitml.engine.Needle engineNeedle = context.getKnittingFactory().createNeedle(needle.getId(), needle.getType().getStyle());
		context.getPatternRepository().addNeedle(engineNeedle);
		log.debug("Added needle [" + engineNeedle + "] to the pattern repository");
	}

}
