package com.knitml.renderer.config;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.knitml.renderer.context.Options;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.ListenerManager;

public class RenderingContextProvider implements Provider<RenderingContext> {

	private Options options;
	private KnittingContextFactory knittingContextFactory;
	private Provider<ListenerManager> listenerManagerProvider;

	@Inject
	public RenderingContextProvider(Options options,
			KnittingContextFactory knittingContextFactory,
			Provider<ListenerManager> listenerManagerProvider) {
		super();
		this.options = options;
		this.knittingContextFactory = knittingContextFactory;
		this.listenerManagerProvider = listenerManagerProvider;
	}

	public RenderingContext get() {
		KnittingContext knittingContext = knittingContextFactory.create();
		knittingContext.setListenerManager(listenerManagerProvider.get());
		RenderingContext renderingContext = new RenderingContext(options);
		renderingContext.setKnittingContext(knittingContext);
		return renderingContext;
	}

}
