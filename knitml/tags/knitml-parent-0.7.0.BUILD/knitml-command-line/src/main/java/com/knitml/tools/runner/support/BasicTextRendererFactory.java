package com.knitml.tools.runner.support;

import java.io.Writer;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.impl.basic.BasicTextRenderer;
import com.knitml.renderer.plural.PluralRuleFactory;

public class BasicTextRendererFactory implements RendererFactory {

	@Inject
	private PluralRuleFactory pluralRuleFactory;
	@Inject
	private Injector injector;
	
	public Renderer create(RenderingContext context, Writer writer) {
		Renderer renderer = new BasicTextRenderer(pluralRuleFactory, context, writer);
		injector.injectMembers(renderer);
		return renderer;
	}

}
