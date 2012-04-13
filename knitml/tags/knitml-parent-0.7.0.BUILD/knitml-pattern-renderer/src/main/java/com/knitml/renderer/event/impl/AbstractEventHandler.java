package com.knitml.renderer.event.impl;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.EventHandler;
import com.knitml.renderer.event.EventHandlerFactory;

public abstract class AbstractEventHandler implements EventHandler {

	private EventHandlerFactory eventHandlerFactory;
	
	public EventHandlerFactory getEventHandlerFactory() {
		return eventHandlerFactory;
	}

	public void setEventHandlerFactory(EventHandlerFactory eventFactory) {
		this.eventHandlerFactory = eventFactory;
	}

	public void end(Object event, Renderer renderer) throws RenderingException {
		// by default this is not defined
	}

}
