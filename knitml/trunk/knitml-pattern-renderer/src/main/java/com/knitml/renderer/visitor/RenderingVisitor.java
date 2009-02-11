package com.knitml.renderer.visitor;

import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;

public interface RenderingVisitor {
	void visit(Object object, RenderingContext context)
			throws RenderingException;
}
