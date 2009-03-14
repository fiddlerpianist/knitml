package com.knitml.renderer.impl.charting;

import java.io.Writer;
import java.util.List;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.chart.translator.ChartElementTranslator;
import com.knitml.renderer.chart.translator.ChartElementTranslatorRegistry;
import com.knitml.renderer.chart.translator.impl.DefaultChartElementTranslatorRegistry;
import com.knitml.renderer.chart.writer.ChartWriterFactory;
import com.knitml.renderer.context.RenderingContext;

public class ChartingRendererFactory implements RendererFactory {
	
	private RendererFactory fallbackRendererFactory;
	private ChartWriterFactory chartWriterFactory;
	private ChartElementTranslatorRegistry registry;

	public ChartingRendererFactory(RendererFactory fallbackRendererFactory,
			ChartWriterFactory chartWriterFactory,
			List<ChartElementTranslator> translators) {
		this.fallbackRendererFactory = fallbackRendererFactory;
		this.chartWriterFactory = chartWriterFactory;
		this.registry = new DefaultChartElementTranslatorRegistry(translators);
	}

	public ChartingRendererFactory(RendererFactory fallbackRendererFactory,
			ChartWriterFactory chartWriterFactory,
			ChartElementTranslatorRegistry registry) {
		this.fallbackRendererFactory = fallbackRendererFactory;
		this.chartWriterFactory = chartWriterFactory;
		this.registry = registry;
	}
	

	public Renderer createRenderer(RenderingContext context, Writer writer) {
		Renderer fallbackRenderer = fallbackRendererFactory.createRenderer(context, writer);
		return new ChartingRenderer(fallbackRenderer, context, chartWriterFactory, registry);
	}

}
