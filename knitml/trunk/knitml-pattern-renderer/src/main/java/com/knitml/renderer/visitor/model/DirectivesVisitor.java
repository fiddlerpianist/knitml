package com.knitml.renderer.visitor.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.header.Directives;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class DirectivesVisitor extends AbstractRenderingEvent {

	private final static Logger log = LoggerFactory
			.getLogger(DirectivesVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		Directives directives = (Directives) element;
		List<String> messageSources = directives.getMessageSources();
		if (messageSources != null && !messageSources.isEmpty()) {
			log
					.debug("Setting pattern message sources in the pattern repository to: "
							+ directives.getMessageSources());
			context.getPatternRepository().setPatternMessageSources(
					directives.getMessageSources());
		}
		context.getRenderer().beginInstructionDefinitions();
		return true;
	}

	public void end(Object element, RenderingContext context)
			throws RenderingException {
		context.getRenderer().endInstructionDefinitions();
	}
}
