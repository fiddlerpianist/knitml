package com.knitml.renderer.impl.basic;

import java.io.Writer;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.context.RenderingContext;

public class BasicTextRendererFactory implements RendererFactory {

	public final Renderer createRenderer(RenderingContext context, Writer writer) {
		MessageSource messageSource = context.getOptions().getProgramMessageSource();
		if (messageSource == null) {
			ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
			ms.setBasename("com/knitml/renderer/impl/operations");
			messageSource = ms;
		}
		return newRenderer(context, writer, messageSource);
	}
	
	protected Renderer newRenderer(RenderingContext context, Writer writer, MessageSource messageSource) {
		return new BasicTextRenderer(context, writer, messageSource);
	}

}
