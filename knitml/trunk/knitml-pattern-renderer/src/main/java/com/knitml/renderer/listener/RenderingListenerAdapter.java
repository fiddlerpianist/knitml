package com.knitml.renderer.listener;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;
import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.core.model.pattern.Directives;
import com.knitml.core.model.pattern.Pattern;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.event.EventHandler;
import com.knitml.renderer.event.EventHandlerFactory;
import com.knitml.renderer.nameresolver.DefinitionsNameResolver;
import com.knitml.renderer.nameresolver.DirectionsNameResolver;
import com.knitml.renderer.nameresolver.NameResolver;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternEventListener;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class RenderingListenerAdapter implements PatternEventListener {

	private NameResolver directionsNameResolver = new DirectionsNameResolver();
	private NameResolver definitionsNameResolver = new DefinitionsNameResolver();

	@Inject
	RenderingListenerAdapter(EventHandlerFactory eventHandlerFactory,
			@Assisted Renderer renderer) {
		this.renderer = renderer;
		//this.renderer.getRenderingContext().setNameResolver(directionsNameResolver);
		this.eventHandlerFactory = eventHandlerFactory;
	}

	private EventHandlerFactory eventHandlerFactory;
	private Renderer renderer;
	private int currentDepth = 0;
	private Integer ignoreBelowDepth;
	private boolean withinDirectives = false;

	public void begin(Object object, KnittingContext knittingContext) {
		if (ignoreBelowDepth != null && currentDepth > ignoreBelowDepth) {
			// ignore the children below the depth specified by ignoreBelowDepth
			currentDepth++;
			return;
		}

		if (object instanceof Pattern) {
			// set the current knitting context onto the rendering context
			// (initialization)
			renderer.getRenderingContext().setKnittingContext(knittingContext);
			currentDepth = 0;
		}

		EventHandler handler = null;
		if (withinDirectives) {
			// use the special "definitions" package for children of directives
			handler = eventHandlerFactory.findEventHandlerFromClassName(object,
					definitionsNameResolver);
		} else {
			handler = eventHandlerFactory.findEventHandlerFromClassName(object,
					directionsNameResolver);
		}

		if (object instanceof Directives) {
			// now set this variable to true, once we've retrieved the
			// directives visitor
			withinDirectives = true;
		}

		// if it's a CompositeOperation, add to the operation tree
		if (object instanceof CompositeOperation) {
			renderer.getRenderingContext().getPatternState().getOperationTree()
					.push((CompositeOperation) object);
		}
		boolean processChildren = handler.begin(object, renderer);
		if (!processChildren) {
			this.ignoreBelowDepth = currentDepth;
		}
		// add 1 to the depth (to track any children)
		currentDepth++;
	}

	public void end(Object object, KnittingContext context) {
		// remove 1 from the depth
		currentDepth--;
		if (ignoreBelowDepth != null) {
			if (currentDepth > ignoreBelowDepth) {
				// we're still ignoring this depth, so return
				return;
			} else {
				// reset back to normal (i.e. process all
				ignoreBelowDepth = null;
			}
		}

		// do this before we look for a visitor
		if (object instanceof Directives) {
			withinDirectives = false;
		}

		EventHandler handler = null;
		if (withinDirectives) {
			// use the special "definitions" package for children of directives
			handler = eventHandlerFactory.findEventHandlerFromClassName(object,
					definitionsNameResolver);
		} else {
			handler = eventHandlerFactory.findEventHandlerFromClassName(object,
					directionsNameResolver);
		}

		handler.end(object, renderer);
		// if it's a CompositeOperation, add to the operation tree
		if (object instanceof CompositeOperation) {
			renderer.getRenderingContext().getPatternState().getOperationTree()
					.pop();
		}
	}

	public boolean desiresRepeats(Object object, KnittingContext context) {
		// never desire repeats (we may want to for InstructionRef objects, but
		// we haven't gotten there yet)
		return false;
	}

}
