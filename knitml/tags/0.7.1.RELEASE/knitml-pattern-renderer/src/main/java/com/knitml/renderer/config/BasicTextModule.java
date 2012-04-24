package com.knitml.renderer.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.impl.basic.BasicTextRenderer;

public class BasicTextModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(Renderer.class,
				BasicTextRenderer.class).build(RendererFactory.class));
	}

}
