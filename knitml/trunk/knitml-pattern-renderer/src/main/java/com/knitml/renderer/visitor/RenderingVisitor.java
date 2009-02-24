package com.knitml.renderer.visitor;

import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;

public interface RenderingVisitor {
	boolean begin(Object object, RenderingContext context)
			throws RenderingException;

	void end(Object object, RenderingContext context) throws RenderingException;
}
