package com.knitml.renderer.event;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;

public interface EventHandler {
	/**
	 * @param event
	 * @param renderer
	 * @return true if children of this event should be processed, false otherwise
	 * @throws RenderingException
	 */
	boolean begin(Object event, Renderer renderer)
			throws RenderingException;

	void end(Object event, Renderer renderer) throws RenderingException;
}
