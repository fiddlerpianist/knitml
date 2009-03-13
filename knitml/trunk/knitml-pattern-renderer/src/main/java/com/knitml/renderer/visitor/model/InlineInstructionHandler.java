package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

/**
 * 
 * @author Jonathan Whitall
 *
 */
public class InlineInstructionHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InlineInstructionHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		InlineInstruction instruction = (InlineInstruction) element;
		renderer.getRenderingContext().getPatternRepository().addInlineInstruction(instruction);
		// inline instructions not defined in the header do not have a label
		renderer.beginInlineInstruction(instruction);
		return true;
	}

	public void end(Object element, Renderer renderer) {
		renderer.endInlineInstruction((InlineInstruction)element);
	}

}
