/**
 * 
 */
package com.knitml.renderer.config;

import com.google.inject.Module;
import com.knitml.renderer.context.Options;

public class Configuration {
	private Module module;
	private Options options;
	
	public Configuration(Module module, Options options) {
		this.module = module;
		this.options = options;
	}

	public Options getOptions() {
		return options;
	}

	public Module getModule() {
		return module;
	}
	
}