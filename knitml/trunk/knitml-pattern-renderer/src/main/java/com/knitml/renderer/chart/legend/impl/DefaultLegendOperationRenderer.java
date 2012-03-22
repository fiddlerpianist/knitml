package com.knitml.renderer.chart.legend.impl;

import java.io.StringWriter;

import javax.inject.Inject;
import javax.inject.Provider;

import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.chart.legend.LegendOperationRenderer;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.EmbeddedVisitor;

public class DefaultLegendOperationRenderer implements LegendOperationRenderer {
	
	private Provider<EmbeddedVisitor> embeddedVisitorProvider;
	private Provider<RenderingContext> renderingContextProvider;
	private RendererFactory rendererFactory;
	
	@Inject
	public DefaultLegendOperationRenderer(Provider<EmbeddedVisitor> embeddedVisitorProvider,
			Provider<RenderingContext> renderingContextProvider,
			RendererFactory rendererFactory) {
		this.embeddedVisitorProvider = embeddedVisitorProvider;
		this.renderingContextProvider = renderingContextProvider;
		this.rendererFactory = rendererFactory;
	}

	public String resolveLegendFor(DiscreteInlineOperation operation) {
		StringWriter writer = new StringWriter(32);
		EmbeddedVisitor visitor = embeddedVisitorProvider.get();
		RenderingContext context = renderingContextProvider.get();
		Renderer renderer = rendererFactory.create(context, writer);
		visitor.visitDiscreteInlineOperation(operation, renderer);
		return writer.toString();
	}
	
}
