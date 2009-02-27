package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.resetLastExpressedRowNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Section;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class SectionVisitor extends AbstractRenderingEvent {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(SectionVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		Section section = (Section) element;
		context.getRenderer().beginSection(section);
		if (section.getResetRowCount()) {
			resetLastExpressedRowNumber(context);
		}
		return true;
	}

	public void end(Object element, RenderingContext context) {
		context.getRenderer().endSection();
	}

}
