package com.knitml.renderer.impl.charting;

import java.io.Writer;
import java.util.List;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.chart.symboladvisor.ChartSymbolAdvisor;
import com.knitml.renderer.chart.symboladvisor.ChartSymbolAdvisorRegistry;
import com.knitml.renderer.chart.symboladvisor.impl.DefaultChartSymbolAdvisorRegistry;
import com.knitml.renderer.chart.writer.ChartWriterFactory;
import com.knitml.renderer.context.RenderingContext;

public class ChartingRendererFactory implements RendererFactory {
	
	private RendererFactory fallbackRendererFactory;
	private ChartWriterFactory chartWriterFactory;
	private ChartSymbolAdvisorRegistry registry;

	public ChartingRendererFactory(RendererFactory fallbackRendererFactory,
			ChartWriterFactory chartWriterFactory,
			List<ChartSymbolAdvisor> translators) {
		this.fallbackRendererFactory = fallbackRendererFactory;
		this.chartWriterFactory = chartWriterFactory;
		this.registry = new DefaultChartSymbolAdvisorRegistry(translators);
	}

	public ChartingRendererFactory(RendererFactory fallbackRendererFactory,
			ChartWriterFactory chartWriterFactory,
			ChartSymbolAdvisorRegistry registry) {
		this.fallbackRendererFactory = fallbackRendererFactory;
		this.chartWriterFactory = chartWriterFactory;
		this.registry = registry;
	}
	

	public Renderer createRenderer(RenderingContext context, Writer writer) {
		Renderer fallbackRenderer = fallbackRendererFactory.createRenderer(context, writer);
		return new ChartingRenderer(fallbackRenderer, context, chartWriterFactory, registry);
	}

}
