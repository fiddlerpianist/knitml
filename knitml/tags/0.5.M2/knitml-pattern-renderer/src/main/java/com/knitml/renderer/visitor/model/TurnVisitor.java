package com.knitml.renderer.visitor.model;

import com.knitml.core.model.directions.inline.Turn;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class TurnVisitor extends AbstractRenderingEvent {

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		Turn turn = (Turn) element;
		context.getRenderer().renderTurn();
		if (turn.getStitchesLeft() != null) {
			context.getRenderer().renderUnworkedStitches(turn.getStitchesLeft());
		}
		return true;
	}
}
