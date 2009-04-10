package com.knitml.renderer.context;

import java.io.Writer;


public interface RenderingContextFactory {

	RenderingContext createRenderingContext();
	RenderingContext createRenderingContext(Writer writer);
	void shutdown();

}
