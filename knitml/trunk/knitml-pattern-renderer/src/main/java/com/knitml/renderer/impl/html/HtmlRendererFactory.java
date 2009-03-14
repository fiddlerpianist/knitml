package com.knitml.renderer.impl.html;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.chart.writer.HtmlStylesheetProvider;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.impl.basic.BasicTextRendererFactory;

public class HtmlRendererFactory extends BasicTextRendererFactory {

	private List<HtmlStylesheetProvider> stylesheetProviders = new ArrayList<HtmlStylesheetProvider>();
	
	public HtmlRendererFactory() {
	}
	
	public HtmlRendererFactory(HtmlStylesheetProvider provider) {
		stylesheetProviders.add(provider);
	}
	
	public HtmlRendererFactory(List<HtmlStylesheetProvider> providers) {
		stylesheetProviders.addAll(providers);
	}
	
	@Override
	protected Renderer newRenderer(RenderingContext context, Writer writer,
			MessageSource messageSource) {
		return new HtmlRenderer(context, writer, messageSource, stylesheetProviders);
	}

}
