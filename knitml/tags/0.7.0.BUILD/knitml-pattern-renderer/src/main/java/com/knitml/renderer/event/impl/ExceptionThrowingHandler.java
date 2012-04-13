package com.knitml.renderer.event.impl;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.event.EventHandler;

public class ExceptionThrowingHandler implements EventHandler {

	public boolean begin(Object object, Renderer renderer) {
		 throw new RuntimeException("No event handler for [" + object.getClass().getName() + "]");
	}

	public void end(Object object, Renderer renderer) {
		 throw new RuntimeException("No event handler for [" + object.getClass().getName() + "]");
	}
	
}
