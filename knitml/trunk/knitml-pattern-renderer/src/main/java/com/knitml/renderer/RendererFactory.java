package com.knitml.renderer;

import java.io.Writer;

import com.knitml.renderer.context.RenderingContext;

public interface RendererFactory {
	Renderer create(RenderingContext context, Writer writer);
}
