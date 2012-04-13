package com.knitml.renderer.chart.legend.impl;

import java.io.StringWriter;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import com.knitml.core.common.Side;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.legend.LegendOperationRenderer;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.EmbeddedVisitor;

public class DefaultLegendOperationRenderer implements LegendOperationRenderer {

	private Provider<EmbeddedVisitor> embeddedVisitorProvider;
	private Provider<RenderingContext> renderingContextProvider;
	private RendererFactory rendererFactory;

	@Inject
	public DefaultLegendOperationRenderer(
			Provider<EmbeddedVisitor> embeddedVisitorProvider,
			Provider<RenderingContext> renderingContextProvider,
			RendererFactory rendererFactory) {
		this.embeddedVisitorProvider = embeddedVisitorProvider;
		this.renderingContextProvider = renderingContextProvider;
		this.rendererFactory = rendererFactory;
	}

	public String resolveLegendFor(ChartElement element,
			Map<Side, DiscreteInlineOperation> operations) {
		if (element == ChartElement.NS) {
			// not usually rendered by a Renderer
			return "No stitch";
		}
		StringWriter writer = new StringWriter(32);
		EmbeddedVisitor visitor = embeddedVisitorProvider.get();
		RenderingContext context = renderingContextProvider.get();
		Renderer renderer = rendererFactory.create(context, writer);
		boolean hasRightSide = operations.containsKey(Side.RIGHT);
		boolean hasWrongSide = operations.containsKey(Side.WRONG);
		if (hasRightSide && !hasWrongSide) {
			visitor.visitDiscreteInlineOperation(operations.get(Side.RIGHT),
					renderer);
		} else if (!hasRightSide && hasWrongSide) {
			visitor.visitDiscreteInlineOperation(operations.get(Side.WRONG),
					renderer);
			writer.write(" on WS");
		} else { // has both
			visitor.visitDiscreteInlineOperation(operations.get(Side.RIGHT),
					renderer);
			writer.write(" on RS; ");
			visitor.visitDiscreteInlineOperation(operations.get(Side.WRONG),
					renderer);
			writer.write(" on WS");
		}
		return writer.toString();
	}

}
