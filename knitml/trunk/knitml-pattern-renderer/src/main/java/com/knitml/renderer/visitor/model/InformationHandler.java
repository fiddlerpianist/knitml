package com.knitml.renderer.visitor.model;

import com.knitml.core.model.directions.information.Information;
import com.knitml.core.model.directions.information.Message;
import com.knitml.core.model.directions.information.NumberOfStitches;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class InformationHandler extends AbstractEventHandler {

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		RenderingContext context = renderer.getRenderingContext();
		Information information = (Information) element;
		renderer.beginInformation();
		information.getDetails();
		for (Object child : information.getDetails()) {
			if (child instanceof Message) {
				Message message = (Message) child;
				String messageToRender = context.getPatternRepository().getPatternMessage(message.getMessageKey(), message.getLabel());
				renderer.renderMessage(messageToRender);
			} else if (child instanceof NumberOfStitches) {
				renderer.renderNumberOfStitchesInRow((NumberOfStitches) child);
			}
		}
		// 
		return false;
	}
	
	public void end(Object element, Renderer renderer) {
		renderer.endInformation();
	}
}
