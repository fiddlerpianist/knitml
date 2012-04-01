package com.knitml.transform.gauge.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternEventListener;

public class TransformPatternEventListener implements PatternEventListener {
	
	final Logger logger = LoggerFactory.getLogger(TransformPatternEventListener.class);
	private boolean disabled = false;
	
	private TransformHandler handler;
	
	public TransformPatternEventListener(TransformHandler handler) {
		this.handler = handler;
	}

	public void begin(Object object, KnittingContext knittingContext) {
		if (disabled)
			return;
		
		Method m = null;
		try {
			m = handler.getClass().getDeclaredMethod("begin",
					object.getClass(), knittingContext.getClass());
			m.setAccessible(true);
			m.invoke(handler, object, knittingContext);
		} catch (NoSuchMethodException ex) {
			// not interested
			logger.info("No handler found for {}", object.getClass().getName());
		} catch (InvocationTargetException ex) {
			if (ex.getTargetException() instanceof CannotTransformGaugeException) {
				logger.warn("Unable to transform the gauge of this pattern. Likely no gauge was specified in the pattern.");
				disabled = true;
			}
		} catch (Exception ex) {
			throw new RuntimeException("An internal error occurred", ex);
		}
	}

	public void end(Object object, KnittingContext knittingContext) {
		if (disabled)
			return;
		
		Method m = null;
		try {
			m = handler.getClass().getDeclaredMethod("end",
					object.getClass(), knittingContext.getClass());
			m.setAccessible(true);
			m.invoke(handler, object, knittingContext);
		} catch (NoSuchMethodException ex) {
			// not interested
			logger.info("No handler found for {}", object.getClass().getName());
		} catch (Exception ex) {
			throw new RuntimeException("An internal error occurred", ex);
		}
	}

	public boolean desiresRepeats(Object object, KnittingContext context) {
		return false;
	}

}
