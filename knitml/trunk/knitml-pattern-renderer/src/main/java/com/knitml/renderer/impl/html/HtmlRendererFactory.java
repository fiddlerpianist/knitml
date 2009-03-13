package com.knitml.renderer.impl.html;

import java.io.Writer;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.context.RenderingContext;

public class HtmlRendererFactory implements RendererFactory {

	public Renderer createRenderer(RenderingContext context, Writer writer) {
		MessageSource messageSource = context.getOptions().getProgramMessageSource();
		if (messageSource == null) {
			ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
			ms.setBasename("com.knitml.renderer.impl.operations");
			messageSource = ms;
		}
		return new HtmlRenderer(context, writer, messageSource);
	}

}
