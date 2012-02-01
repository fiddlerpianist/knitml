/**
 * 
 */
package com.knitml.renderer.util;

import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.context.Options;

public class Configuration {
	private RendererFactory rendererFactory;
	private Options options;
	
	public Configuration(RendererFactory rendererFactory, Options options) {
		this.rendererFactory = rendererFactory;
		this.options = options;
	}

	public RendererFactory getRendererFactory() {
		return rendererFactory;
	}

	public Options getOptions() {
		return options;
	}
	
}