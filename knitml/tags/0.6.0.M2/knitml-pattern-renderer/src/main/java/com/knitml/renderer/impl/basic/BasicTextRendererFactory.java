package com.knitml.renderer.impl.basic;

import java.io.Writer;

import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.context.RenderingContext;

public class BasicTextRendererFactory implements RendererFactory {

	public final Renderer createRenderer(RenderingContext context, Writer writer) {
		// this is the base message source built into the pattern renderer
		ReloadableResourceBundleMessageSource parent = new ReloadableResourceBundleMessageSource();
		parent.setBasename("com/knitml/renderer/impl/operations");
		// use the class loader that loaded this class
		parent.setResourceLoader(new DefaultResourceLoader(this.getClass().getClassLoader()));
		
		HierarchicalMessageSource messageSource = context.getOptions().getProgramMessageSource();
		if (messageSource != null) {
			messageSource.setParentMessageSource(parent);
		} else {
			messageSource = parent;
		}
		return newRenderer(context, writer, messageSource);
	}
	
	protected Renderer newRenderer(RenderingContext context, Writer writer, MessageSource messageSource) {
		return new BasicTextRenderer(context, writer, messageSource);
	}

}
