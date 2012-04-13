package com.knitml.renderer.config;


import java.util.ArrayList;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.knitml.renderer.chart.legend.LegendOperationRenderer;
import com.knitml.renderer.chart.legend.impl.DefaultLegendOperationRenderer;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.EventHandlerFactory;
import com.knitml.renderer.event.impl.DefaultEventHandlerFactory;
import com.knitml.renderer.listener.RenderingListenerAdapter;
import com.knitml.renderer.listener.RenderingPatternEventListenerFactory;
import com.knitml.renderer.plural.PluralRuleFactory;
import com.knitml.renderer.plural.impl.DefaultPluralRuleFactory;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.ListenerManager;
import com.knitml.validation.context.PatternEventListener;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;
import com.knitml.validation.context.impl.DefaultListenerManager;
import com.knitml.validation.visitor.instruction.VisitorFactory;
import com.knitml.validation.visitor.instruction.impl.DefaultVisitorFactory;

public class DefaultModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PluralRuleFactory.class).to(DefaultPluralRuleFactory.class);
		bind(EventHandlerFactory.class).to(DefaultEventHandlerFactory.class);
		bind(LegendOperationRenderer.class).to(DefaultLegendOperationRenderer.class);
		bind(KnittingContextFactory.class).to(DefaultKnittingContextFactory.class);
		bind(ListenerManager.class).to(DefaultListenerManager.class);
		bind(VisitorFactory.class).to(DefaultVisitorFactory.class);
//		bind(VisitorFactory.class).to(SimpleVisitorFactory.class);
		bind(RenderingContext.class).toProvider(RenderingContextProvider.class);
		install(new FactoryModuleBuilder().implement(PatternEventListener.class,
				RenderingListenerAdapter.class).build(RenderingPatternEventListenerFactory.class));
	}
	
	@Provides
	protected List<PatternEventListener> provideListenerList() {
		return new ArrayList<PatternEventListener>();
	}
	

}
