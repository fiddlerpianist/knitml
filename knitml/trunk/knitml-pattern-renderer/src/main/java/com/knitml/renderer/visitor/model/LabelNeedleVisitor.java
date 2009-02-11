package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.ValidationException;
import com.knitml.core.model.directions.block.LabelNeedle;
import com.knitml.core.model.header.Needle;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class LabelNeedleVisitor extends AbstractRenderingVisitor {

	private final static Logger log = LoggerFactory
			.getLogger(LabelNeedleVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		LabelNeedle operation = (LabelNeedle) element;
		PatternRepository patternRepository = context.getPatternRepository();
		// get the new label for the message
		String newLabel = patternRepository.getPatternMessage(operation.getMessageKey(), operation.getLabel());
		
		// lookup the Needle from the pattern repository
		Needle needle = operation.getNeedle();
		if (needle == null) {
			throw new ValidationException("No needle found for label-needle element; it is required");
		}
		log.info("Changed label for [" + needle.getLabel() + "] to [" + newLabel + "]");
		// now set the new label
		needle.setLabel(newLabel);
	}

}
