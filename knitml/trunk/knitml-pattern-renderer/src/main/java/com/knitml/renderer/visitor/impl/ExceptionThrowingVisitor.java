package com.knitml.renderer.visitor.impl;

import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.RenderingVisitor;

public class ExceptionThrowingVisitor implements RenderingVisitor {

	public void visit(Object object, RenderingContext context) {
		 throw new RuntimeException("No visitor for [" + object.getClass().getName() + "]");
	}

}
