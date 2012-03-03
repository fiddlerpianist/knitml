package com.knitml.renderer.visitor.model;

import com.knitml.core.model.operations.block.Information;
import com.knitml.core.model.operations.information.Message;
import com.knitml.core.model.operations.information.NumberOfStitches;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class InformationHandler extends AbstractEventHandler {

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		// we can gather all of the information we need without getting the
		// child events
		return false;
	}

	@Override
	public void end(Object element, Renderer renderer) {
		RenderingContext context = renderer.getRenderingContext();
		Information information = (Information) element;
		renderer.beginInformation();
		for (Object child : information.getDetails()) {
			if (child instanceof Message) {
				Message message = (Message) child;
				String messageToRender = context.getPatternRepository()
						.getPatternMessage(message.getMessageKey(),
								message.getLabel());
				if (messageToRender != null) {
					renderer.renderMessage(messageToRender);
				}
			} else if (child instanceof NumberOfStitches) {
				renderer.renderNumberOfStitchesInRow((NumberOfStitches) child);
			}
		}
		renderer.endInformation();
	}
}
