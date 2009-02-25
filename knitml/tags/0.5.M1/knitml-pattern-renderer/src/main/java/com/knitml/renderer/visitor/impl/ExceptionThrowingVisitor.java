package com.knitml.renderer.visitor.impl;

import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.RenderingVisitor;

public class ExceptionThrowingVisitor implements RenderingVisitor {

	public boolean begin(Object object, RenderingContext context) {
		 throw new RuntimeException("No visitor for [" + object.getClass().getName() + "]");
	}

	public void end(Object object, RenderingContext context) {
		 throw new RuntimeException("No visitor for [" + object.getClass().getName() + "]");
	}
	
}
