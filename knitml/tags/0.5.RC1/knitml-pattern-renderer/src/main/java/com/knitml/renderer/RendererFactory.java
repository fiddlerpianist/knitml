package com.knitml.renderer;

import java.io.Writer;

import com.knitml.renderer.context.RenderingContext;

public interface RendererFactory {
	Renderer createRenderer(RenderingContext context, Writer writer);
}
