package com.knitml.renderer.event;

import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;

public interface RenderingEvent {
	boolean begin(Object object, RenderingContext context)
			throws RenderingException;

	void end(Object object, RenderingContext context) throws RenderingException;
}
