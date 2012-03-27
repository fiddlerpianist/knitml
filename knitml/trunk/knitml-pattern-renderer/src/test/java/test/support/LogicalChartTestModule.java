package test.support;

import java.util.Locale;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.knitml.renderer.BaseRendererFactory;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.chart.writer.ChartWriter;
import com.knitml.renderer.chart.writer.impl.NoOpChartWriter;
import com.knitml.renderer.context.Options;
import com.knitml.renderer.impl.basic.BasicTextRenderer;
import com.knitml.renderer.impl.charting.ChartingRenderer;

public class LogicalChartTestModule extends AbstractModule {

	protected void configure() {
		install(new BaseTestModule());
		install(new FactoryModuleBuilder().implement(Renderer.class,
				ChartingRenderer.class).build(RendererFactory.class));
		install(new FactoryModuleBuilder().implement(Renderer.class,
				BasicTextRenderer.class).build(BaseRendererFactory.class));
		bind(ChartWriter.class).to(NoOpChartWriter.class);
	}

	@Provides
	Options provideOptions() {
		Options options = new Options();
		options.setLocale(new Locale("en_US"));
		options.setGlobalChart(true);
		return options;
	}

}
