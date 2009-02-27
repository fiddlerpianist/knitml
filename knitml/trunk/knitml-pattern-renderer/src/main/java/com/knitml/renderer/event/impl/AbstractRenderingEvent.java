package com.knitml.renderer.event.impl;

import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.EventFactory;
import com.knitml.renderer.event.RenderingEvent;

public abstract class AbstractRenderingEvent implements RenderingEvent {

	private EventFactory visitorFactory;

	public EventFactory getVisitorFactory() {
		return visitorFactory;
	}

	public void setVisitorFactory(EventFactory visitorFactory) {
		this.visitorFactory = visitorFactory;
	}

	public void end(Object object, RenderingContext context)
			throws RenderingException {
		// by default this is not defined
	}

}
