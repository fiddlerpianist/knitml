package com.knitml.renderer.handler.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.ValidationException;
import com.knitml.core.model.common.Needle;
import com.knitml.core.model.operations.block.LabelNeedle;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class LabelNeedleHandler extends AbstractEventHandler {

	private final static Logger log = LoggerFactory
			.getLogger(LabelNeedleHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		LabelNeedle operation = (LabelNeedle) element;
		PatternRepository patternRepository = renderer.getRenderingContext().getPatternRepository();
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
		return true;
	}

}
