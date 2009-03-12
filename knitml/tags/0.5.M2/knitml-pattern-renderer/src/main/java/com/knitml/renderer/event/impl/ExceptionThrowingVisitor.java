package com.knitml.renderer.event.impl;

import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.RenderingEvent;

public class ExceptionThrowingVisitor implements RenderingEvent {

	public boolean begin(Object object, RenderingContext context) {
		 throw new RuntimeException("No visitor for [" + object.getClass().getName() + "]");
	}

	public void end(Object object, RenderingContext context) {
		 throw new RuntimeException("No visitor for [" + object.getClass().getName() + "]");
	}
	
}
