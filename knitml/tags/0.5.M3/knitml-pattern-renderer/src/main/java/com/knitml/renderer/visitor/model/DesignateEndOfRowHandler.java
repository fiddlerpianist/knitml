package com.knitml.renderer.visitor.model;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class DesignateEndOfRowHandler extends AbstractEventHandler {

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		renderer.renderDesignateEndOfRow(renderer.getRenderingContext().getEngine().getKnittingShape());
		return true;
	}
}
