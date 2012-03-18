package test.support;

import java.util.Locale;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.config.DefaultModule;
import com.knitml.renderer.context.Options;
import com.knitml.renderer.impl.basic.BasicTextRenderer;

public class BasicRendererTestModule extends AbstractModule {

	protected void configure() {
		install(new DefaultModule());
		install(new FactoryModuleBuilder().implement(Renderer.class,
				BasicTextRenderer.class).build(RendererFactory.class));
	}

	@Provides
	Options provideOptions() {
		Options options = new Options();
		options.setLocale(new Locale("en_US"));
		return options;
	}

}
