package com.knitml.renderer.listener;

import com.knitml.renderer.Renderer;
import com.knitml.validation.context.PatternEventListener;

public interface RenderingPatternEventListenerFactory {

	PatternEventListener create(Renderer renderer);
	
}
