package com.knitml.renderer.impl.charting;

import java.io.Writer;
import java.util.List;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.chart.symbol.SymbolProvider;
import com.knitml.renderer.chart.symbol.SymbolProviderRegistry;
import com.knitml.renderer.chart.symbol.impl.DefaultSymbolProviderRegistry;
import com.knitml.renderer.chart.writer.ChartWriterFactory;
import com.knitml.renderer.context.RenderingContext;

public class ChartingRendererFactory implements RendererFactory {
	
	private RendererFactory fallbackRendererFactory;
	private ChartWriterFactory chartWriterFactory;
	private SymbolProviderRegistry registry;

	public ChartingRendererFactory(RendererFactory fallbackRendererFactory,
			ChartWriterFactory chartWriterFactory,
			List<SymbolProvider> symbolProviders) {
		this.fallbackRendererFactory = fallbackRendererFactory;
		this.chartWriterFactory = chartWriterFactory;
		this.registry = new DefaultSymbolProviderRegistry(symbolProviders);
	}

	public ChartingRendererFactory(RendererFactory fallbackRendererFactory,
			ChartWriterFactory chartWriterFactory,
			SymbolProviderRegistry registry) {
		this.fallbackRendererFactory = fallbackRendererFactory;
		this.chartWriterFactory = chartWriterFactory;
		this.registry = registry;
	}
	

	public Renderer createRenderer(RenderingContext context, Writer writer) {
		Renderer fallbackRenderer = fallbackRendererFactory.createRenderer(context, writer);
		return new ChartingRenderer(fallbackRenderer, context, chartWriterFactory, registry);
	}

}
