package com.knitml.renderer.listener;

import com.knitml.core.model.Pattern;
import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.header.Directives;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.RenderingVisitor;
import com.knitml.renderer.visitor.VisitorFactory;
import com.knitml.renderer.visitor.impl.DefaultNameResolver;
import com.knitml.renderer.visitor.impl.DefaultVisitorFactory;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.Listener;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class RenderingListenerAdapter implements Listener {

	public RenderingListenerAdapter(RenderingContext renderingContext) {
		this.renderingContext = renderingContext;
	}

	private VisitorFactory visitorFactory = new DefaultVisitorFactory();
	private RenderingContext renderingContext;
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
			renderingContext.setKnittingContext(knittingContext);
			currentDepth = 0;
		}

		RenderingVisitor visitor = null;
		if (withinDirectives) {
			// use the special "definitions" package for children of directives
			visitorFactory.pushNameResolver(new DefaultNameResolver(
					"com.knitml.renderer.visitor.definition.model"));
			visitor = visitorFactory.findVisitorFromClassName(object);
			visitorFactory.popNameResolver();
		} else {
			visitor = visitorFactory.findVisitorFromClassName(object);
		}
		
		if (object instanceof Directives) {
			// now set this variable to true, once we've retrieved the
			// directives visitor
			withinDirectives = true;
		}

		// if it's a CompositeOperation, add to the operation tree
		if (object instanceof CompositeOperation) {
			 renderingContext.getPatternState().getOperationTree().push((CompositeOperation)object);
		}
		boolean processChildren = visitor.begin(object, renderingContext);
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

		RenderingVisitor visitor = null;
		if (withinDirectives) {
			// use the special "definitions" package for children of directives
			visitorFactory.pushNameResolver(new DefaultNameResolver(
					"com.knitml.renderer.visitor.definition.model"));
			visitor = visitorFactory.findVisitorFromClassName(object);
			visitorFactory.popNameResolver();
		} else {
			visitor = visitorFactory.findVisitorFromClassName(object);
		}

		visitor.end(object, renderingContext);
		// if it's a CompositeOperation, add to the operation tree
		if (object instanceof CompositeOperation) {
			 renderingContext.getPatternState().getOperationTree().pop();
		}
	}

	public boolean desiresRepeats(Object object, KnittingContext context) {
		// never desire repeats (we may want to for InstructionRef objects, but
		// we haven't gotten there yet)
		return false;
	}

	public void setVisitorFactory(VisitorFactory visitorFactory) {
		this.visitorFactory = visitorFactory;
	}

}
