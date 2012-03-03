package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.deriveLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.ValidationException;
import com.knitml.core.model.operations.inline.ApplyNextRow;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class ApplyNextRowHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(ApplyNextRowHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		ApplyNextRow applyNextRow = (ApplyNextRow) element;
		String label = deriveLabel(applyNextRow.getInstructionRef(), renderer.getRenderingContext()
				.getPatternRepository());
		if (label == null) {
			throw new ValidationException(
					"An apply-next-row may only be applied to a global instruction");
		}
		renderer.renderApplyNextRow(applyNextRow, label);
		return true;
	}

}
