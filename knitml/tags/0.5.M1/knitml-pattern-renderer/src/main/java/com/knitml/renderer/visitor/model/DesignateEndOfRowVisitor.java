package com.knitml.renderer.visitor.model;

import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class DesignateEndOfRowVisitor extends AbstractRenderingVisitor {

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		context.getRenderer().renderDesignateEndOfRow(context.getPatternState().getCurrentKnittingShape());
		return true;
	}
}
