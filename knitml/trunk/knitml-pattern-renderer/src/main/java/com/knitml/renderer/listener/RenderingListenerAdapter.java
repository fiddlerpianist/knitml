package com.knitml.renderer.listener;

import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.core.model.pattern.Directives;
import com.knitml.core.model.pattern.Pattern;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.event.EventHandler;
import com.knitml.renderer.event.EventHandlerFactory;
import com.knitml.renderer.event.impl.DefaultEventHandlerFactory;
import com.knitml.renderer.event.impl.DefaultNameResolver;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternEventListener;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class RenderingListenerAdapter implements PatternEventListener {

	public RenderingListenerAdapter(Renderer renderer) {
		this.renderer = renderer;
	}

	private EventHandlerFactory eventHandlerFactory = new DefaultEventHandlerFactory();
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
			renderer.getRenderingContext().setKnittingContext(knittingContext);
			currentDepth = 0;
		}

		EventHandler visitor = null;
		if (withinDirectives) {
			// use the special "definitions" package for children of directives
			eventHandlerFactory.pushNameResolver(new DefaultNameResolver(
					"com.knitml.renderer.visitor.definition.model"));
			visitor = eventHandlerFactory.findEventHandlerFromClassName(object);
			eventHandlerFactory.popNameResolver();
		} else {
			visitor = eventHandlerFactory.findEventHandlerFromClassName(object);
		}
		
		if (object instanceof Directives) {
			// now set this variable to true, once we've retrieved the
			// directives visitor
			withinDirectives = true;
		}

		// if it's a CompositeOperation, add to the operation tree
		if (object instanceof CompositeOperation) {
			renderer.getRenderingContext().getPatternState().getOperationTree().push((CompositeOperation)object);
		}
		boolean processChildren = visitor.begin(object, renderer);
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

		EventHandler visitor = null;
		if (withinDirectives) {
			// use the special "definitions" package for children of directives
			eventHandlerFactory.pushNameResolver(new DefaultNameResolver(
					"com.knitml.renderer.visitor.definition.model"));
			visitor = eventHandlerFactory.findEventHandlerFromClassName(object);
			eventHandlerFactory.popNameResolver();
		} else {
			visitor = eventHandlerFactory.findEventHandlerFromClassName(object);
		}

		visitor.end(object, renderer);
		// if it's a CompositeOperation, add to the operation tree
		if (object instanceof CompositeOperation) {
			 renderer.getRenderingContext().getPatternState().getOperationTree().pop();
		}
	}

	public boolean desiresRepeats(Object object, KnittingContext context) {
		// never desire repeats (we may want to for InstructionRef objects, but
		// we haven't gotten there yet)
		return false;
	}

	public void setEventHandlerFactory(EventHandlerFactory eventHandlerFactory) {
		this.eventHandlerFactory = eventHandlerFactory;
	}

}
