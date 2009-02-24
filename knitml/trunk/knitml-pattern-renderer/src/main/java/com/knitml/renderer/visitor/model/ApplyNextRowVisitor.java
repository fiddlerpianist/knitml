package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.deriveLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.ValidationException;
import com.knitml.core.model.directions.inline.ApplyNextRow;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class ApplyNextRowVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(ApplyNextRowVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		ApplyNextRow applyNextRow = (ApplyNextRow) element;
		String label = deriveLabel(applyNextRow.getInstructionRef(), context
				.getPatternRepository());
		if (label == null) {
			throw new ValidationException(
					"An apply-next-row may only be applied to a global instruction");
		}
		context.getRenderer().renderApplyNextRow(applyNextRow, label);
		return true;
	}

}
