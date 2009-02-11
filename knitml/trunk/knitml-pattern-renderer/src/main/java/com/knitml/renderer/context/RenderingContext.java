package com.knitml.renderer.context;

import com.knitml.engine.KnittingEngine;

public class RenderingContext {

	private PatternRepository patternRepository;
	private Renderer renderer;
	private Options options;
	private PatternState patternState;
	private KnittingEngine engine;

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	public PatternState getPatternState() {
		return patternState;
	}

	public void setPatternState(PatternState patternState) {
		this.patternState = patternState;
	}

	public PatternRepository getPatternRepository() {
		return patternRepository;
	}

	public void setPatternRepository(PatternRepository patternRepository) {
		this.patternRepository = patternRepository;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	public void setEngine(KnittingEngine engine) {
		this.engine = engine;
	}

	public KnittingEngine getEngine() {
		return engine;
	}

}
