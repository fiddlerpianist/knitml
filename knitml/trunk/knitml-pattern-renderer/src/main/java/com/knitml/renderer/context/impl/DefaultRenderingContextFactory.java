package com.knitml.renderer.context.impl;

import java.io.Writer;

import org.springframework.context.support.ResourceBundleMessageSource;

import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.context.RenderingContextFactory;
import com.knitml.renderer.context.Options;
import com.knitml.renderer.context.PatternState;
import com.knitml.renderer.impl.basic.BasicTextRenderer;

public class DefaultRenderingContextFactory implements RenderingContextFactory {

	public RenderingContext createRenderingContext() {
		RenderingContext context = new RenderingContext();
		BasicTextRenderer renderer = new BasicTextRenderer();
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("com.knitml.renderer.impl.operations");
		renderer.setMessageSource(messageSource);
		context.setRenderer(renderer);
		context.setOptions(new Options());
		context.setPatternState(new PatternState());
		return context;
	}

	public RenderingContext createRenderingContext(Writer writer) {
		RenderingContext context = createRenderingContext();
		context.getRenderer().setWriter(writer);
		return context;
	}
	
	public void shutdown() {
		// nothing to do
	}

}
