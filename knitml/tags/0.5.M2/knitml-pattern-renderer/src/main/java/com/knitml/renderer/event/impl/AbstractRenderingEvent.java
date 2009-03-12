package com.knitml.renderer.event.impl;

import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.EventFactory;
import com.knitml.renderer.event.RenderingEvent;

public abstract class AbstractRenderingEvent implements RenderingEvent {

	private EventFactory eventFactory;

	public EventFactory getEventFactory() {
		return eventFactory;
	}

	public void setEventFactory(EventFactory eventFactory) {
		this.eventFactory = eventFactory;
	}

	public void end(Object object, RenderingContext context)
			throws RenderingException {
		// by default this is not defined
	}

}
