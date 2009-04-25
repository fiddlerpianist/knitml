package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.resetLastExpressedRowNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Section;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class SectionHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(SectionHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		Section section = (Section) element;
		renderer.beginSection(section);
		if (section.getResetRowCount()) {
			resetLastExpressedRowNumber(renderer.getRenderingContext());
		}
		return true;
	}

	@Override
	public void end(Object element, Renderer renderer) {
		renderer.endSection();
	}

}
