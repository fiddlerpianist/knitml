package com.knitml.renderer.context;

import org.apache.commons.lang.NullArgumentException;

import com.knitml.engine.KnittingEngine;
import com.knitml.validation.context.KnittingContext;

public class RenderingContext {

	// fields with defaults
	private Options options;
	private PatternState patternState;
	
	// internally calculated fields created in constructor
	private PatternRepository patternRepository;
	// dependency set later when available
	private KnittingContext knittingContext;

	public RenderingContext(Options options) {
		if (options == null) {
			throw new NullArgumentException("options");
		}
		this.options = options;
		this.patternRepository = new PatternRepository(options);
		this.patternState = new PatternState();
	}
	
	public Options getOptions() {
		return options;
	}

	public PatternState getPatternState() {
		return patternState;
	}

	public PatternRepository getPatternRepository() {
		return patternRepository;
	}

	public KnittingEngine getEngine() {
		return knittingContext.getEngine();
	}

	public KnittingContext getKnittingContext() {
		return knittingContext;
	}

	public void setKnittingContext(KnittingContext knittingContext) {
		this.knittingContext = knittingContext;
	}
}
