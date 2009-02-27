package com.knitml.renderer.visitor.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.Operation;
import com.knitml.core.model.directions.block.CastOn;
import com.knitml.core.model.directions.block.UseNeedles;
import com.knitml.core.model.header.Needle;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class CastOnVisitor extends AbstractRenderingEvent {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(CastOnVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		CastOn castOn = (CastOn) element;
		
		if (!context.getPatternState().getOperationTree().empty()) {
			CompositeOperation parent = context.getPatternState()
					.getOperationTree().peek();
			List<? extends Operation> siblings = parent.getOperations();
			int position = siblings.indexOf(castOn);
			if (position > 0) {
				Operation previousOperation = siblings.get(position - 1);
				if (previousOperation instanceof UseNeedles
						&& ((UseNeedles) previousOperation).isSilentRendering()) {
					List<Needle> needles = ((UseNeedles) previousOperation)
							.getNeedles();
					context.getRenderer().renderUsingNeedlesCastOn(needles,
							castOn);
				} else {
					context.getRenderer().renderCastOn(castOn);
				}
			} else {
				context.getRenderer().renderCastOn(castOn);
			}
		} else {
			context.getRenderer().renderCastOn(castOn);
		}
		return true;
	}
}
