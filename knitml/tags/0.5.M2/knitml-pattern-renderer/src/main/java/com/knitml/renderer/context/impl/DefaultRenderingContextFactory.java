package com.knitml.renderer.context.impl;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;

import com.knitml.renderer.chart.translator.ChartElementTranslator;
import com.knitml.renderer.chart.translator.ChartElementTranslatorRegistry;
import com.knitml.renderer.chart.translator.impl.DefaultChartElementTranslatorRegistry;
import com.knitml.renderer.chart.translator.impl.TextArtLaceTranslator;
import com.knitml.renderer.chart.writer.impl.TextArtChartWriterFactory;
import com.knitml.renderer.context.Options;
import com.knitml.renderer.context.PatternState;
import com.knitml.renderer.context.Renderer;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.context.RenderingContextFactory;
import com.knitml.renderer.impl.basic.BasicTextRenderer;
import com.knitml.renderer.impl.charting.ChartingRenderer;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;

public class DefaultRenderingContextFactory implements RenderingContextFactory {

	public RenderingContext createRenderingContext() {
		RenderingContext context = new RenderingContext();
		BasicTextRenderer basicTextRenderer = new BasicTextRenderer();
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("com.knitml.renderer.impl.operations");
		basicTextRenderer.setMessageSource(messageSource);
		basicTextRenderer.setLocale(Locale.US);
		
		List<ChartElementTranslator> translators = new ArrayList<ChartElementTranslator>();
		translators.add(new TextArtLaceTranslator());
		ChartElementTranslatorRegistry registry = new DefaultChartElementTranslatorRegistry(translators);
		
		Renderer chartingRenderer = new ChartingRenderer(basicTextRenderer, new TextArtChartWriterFactory(), registry);
		// a logical circular reference? yes, but the circular dependency is within one package
		chartingRenderer.setRenderingContext(context);

		context.setRenderer(chartingRenderer);
		context.setOptions(new Options());
		context.setPatternState(new PatternState());
		context.setPatternRepository(new DefaultPatternRepository());
		context.setKnittingContext(new DefaultKnittingContextFactory().createKnittingContext());
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
