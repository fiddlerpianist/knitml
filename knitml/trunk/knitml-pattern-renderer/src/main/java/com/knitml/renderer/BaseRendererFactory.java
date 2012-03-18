package com.knitml.renderer;

import java.io.Writer;

import com.knitml.renderer.context.RenderingContext;

public interface BaseRendererFactory {
	Renderer create(RenderingContext context, Writer writer);
}
