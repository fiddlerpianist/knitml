package com.knitml.renderer.handler.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.pattern.Directives;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class DirectivesHandler extends AbstractEventHandler {

	private final static Logger log = LoggerFactory
			.getLogger(DirectivesHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		Directives directives = (Directives) element;
		List<String> messageSources = directives.getMessageSources();
		if (messageSources != null && !messageSources.isEmpty()) {
			log
					.debug("Setting pattern message sources in the pattern repository to: "
							+ directives.getMessageSources());
			renderer.getRenderingContext().getPatternRepository().setPatternMessageSources(
					directives.getMessageSources());
		}
		renderer.beginInstructionDefinitions();
		return true;
	}

	@Override
	public void end(Object element, Renderer renderer)
			throws RenderingException {
		renderer.endInstructionDefinitions();
	}
}
