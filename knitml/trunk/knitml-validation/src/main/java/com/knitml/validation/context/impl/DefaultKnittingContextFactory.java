package com.knitml.validation.context.impl;

import com.knitml.engine.KnittingEngine;
import com.knitml.engine.KnittingFactory;
import com.knitml.engine.impl.DefaultKnittingEngine;
import com.knitml.engine.impl.DefaultKnittingFactory;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.KnittingContextFactory;

public class DefaultKnittingContextFactory implements KnittingContextFactory {

	public KnittingContext createKnittingContext() {
		KnittingContext context = new KnittingContext();
		KnittingFactory knittingFactory = new DefaultKnittingFactory();
		KnittingEngine engine = new DefaultKnittingEngine(knittingFactory);
		context.setKnittingFactory(knittingFactory);
		context.setPatternRepository(new DefaultPatternRepository());
		context.setPatternState(new DefaultPatternState());
		context.setEngine(engine);
		return context;
	}

	public void shutdown() {
		// nothing to do
	}

}
