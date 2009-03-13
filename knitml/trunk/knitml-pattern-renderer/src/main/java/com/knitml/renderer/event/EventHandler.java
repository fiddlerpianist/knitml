package com.knitml.renderer.event;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;

public interface EventHandler {
	boolean begin(Object event, Renderer renderer)
			throws RenderingException;

	void end(Object event, Renderer renderer) throws RenderingException;
}
