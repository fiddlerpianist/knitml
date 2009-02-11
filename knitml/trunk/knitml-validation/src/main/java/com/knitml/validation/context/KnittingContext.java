package com.knitml.validation.context;

import java.util.List;

import com.knitml.engine.KnittingEngine;
import com.knitml.engine.KnittingFactory;
import com.knitml.engine.Needle;

public class KnittingContext {
	
	private KnittingFactory knittingFactory;
	private PatternRepository patternRepository;
	private PatternState patternState;
	
	private KnittingEngine engine;
	private List<Needle> needlesInUse;
	
	private boolean needleInstructions = false;
	
	public KnittingEngine getEngine() {
		return engine;
	}
	public void setEngine(KnittingEngine engine) {
		this.engine = engine;
	}
	public boolean isNeedleInstructions() {
		return needleInstructions;
	}
	public void setNeedleInstructions(boolean needleInstructions) {
		this.needleInstructions = needleInstructions;
	}
	public KnittingFactory getKnittingFactory() {
		return knittingFactory;
	}
	public void setKnittingFactory(KnittingFactory knittingFactory) {
		this.knittingFactory = knittingFactory;
	}
	public PatternRepository getPatternRepository() {
		return patternRepository;
	}
	public void setPatternRepository(PatternRepository patternRepository) {
		this.patternRepository = patternRepository;
	}
	public List<Needle> getNeedlesInUse() {
		return needlesInUse;
	}
	public void setNeedlesInUse(List<Needle> needlesInUse) {
		this.needlesInUse = needlesInUse;
	}
	public PatternState getPatternState() {
		return patternState;
	}
	public void setPatternState(PatternState patternState) {
		this.patternState = patternState;
	}

}
