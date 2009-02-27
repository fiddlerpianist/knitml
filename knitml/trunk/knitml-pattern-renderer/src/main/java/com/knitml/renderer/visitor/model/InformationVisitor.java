package com.knitml.renderer.visitor.model;

import com.knitml.core.model.directions.information.Information;
import com.knitml.core.model.directions.information.Message;
import com.knitml.core.model.directions.information.NumberOfStitches;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class InformationVisitor extends AbstractRenderingEvent {

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		Information information = (Information) element;
		context.getRenderer().beginInformation();
		information.getDetails();
		for (Object child : information.getDetails()) {
			if (child instanceof Message) {
				Message message = (Message) child;
				String messageToRender = context.getPatternRepository().getPatternMessage(message.getMessageKey(), message.getLabel());
				context.getRenderer().renderMessage(messageToRender);
			} else if (child instanceof NumberOfStitches) {
				context.getRenderer().renderNumberOfStitchesInRow((NumberOfStitches) child);
			}
		}
		// 
		return false;
	}
	
	public void end(Object element, RenderingContext context) {
		context.getRenderer().endInformation();
	}
}
