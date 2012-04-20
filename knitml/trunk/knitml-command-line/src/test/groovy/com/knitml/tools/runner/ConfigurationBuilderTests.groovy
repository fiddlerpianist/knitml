package com.knitml.tools.runner

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.*

import org.junit.Test
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.ResourceLoader

import com.knitml.core.units.Units
import com.knitml.renderer.chart.advisor.impl.AireRiverSymbolAdvisor;
import com.knitml.renderer.chart.advisor.impl.TextArtSymbolAdvisor
import com.knitml.renderer.chart.writer.impl.HtmlChartWriter;
import com.knitml.renderer.config.Configuration
import com.knitml.renderer.impl.basic.BasicTextRenderer
import com.knitml.renderer.impl.html.HtmlRenderer;
import com.knitml.tools.runner.support.ConfigurationBuilder

public class ConfigurationBuilderTests {

	private ResourceLoader loader = new DefaultResourceLoader()

	protected Configuration load(String file, ConfigurationBuilder builder) {
		def props = new Properties()
		def is = loader.getResource('conf/' + file + '.properties').inputStream
		props.load(is)
		is.close()
		builder.buildConfiguration(props)
	}

	@Test
	public void rendererDefaults() {
		def builder = new ConfigurationBuilder()
		def config = load 'renderer-defaults', builder
		config.options.with {
			assertThat globalChart, is (false)
			assertThat squareGauge, is (false)
			assertThat greyNoStitches, is (true)
			assertThat stitchGaugeUnit, is (null)
			assertThat rowGaugeUnit, is (null)
			assertThat fabricMeasurementUnit, is (null)
			assertThat locale, is (Locale.default)
		}
		builder.with {
			assertEquals baseRendererClass, BasicTextRenderer
			assertThat chartWriterClass, is (null)
			assertEquals symbolProviderClass, TextArtSymbolAdvisor
			assertEquals stylesheetProviderClass, TextArtSymbolAdvisor
			assertThat messageFiles, is (null)
		}
	}
	@Test
	public void textRenderer() {
		def builder = new ConfigurationBuilder()
		def config = load 'renderer', builder
		config.options.with {
			assertThat globalChart, is (false)
			assertThat squareGauge, is (false)
			assertThat greyNoStitches, is (true)
			assertThat stitchGaugeUnit, is (Units.STITCHES_PER_INCH)
			assertThat rowGaugeUnit, is (Units.ROWS_PER_INCH)
			assertThat fabricMeasurementUnit, is (Units.INCH)
			assertThat locale, is (new Locale('en'))
		}
		builder.with {
			assertEquals baseRendererClass, BasicTextRenderer
			assertThat chartWriterClass, is (null)
			assertEquals symbolProviderClass, TextArtSymbolAdvisor
			assertEquals stylesheetProviderClass, TextArtSymbolAdvisor
			assertThat messageFiles, is (['custom-operations','local-operations'].toArray())
		}
	}
	@Test
	public void chartRenderer() {
		def builder = new ConfigurationBuilder()
		def config = load 'chart-renderer', builder
		config.options.with {
			assertThat globalChart, is (true)
			assertThat squareGauge, is (true)
			assertThat greyNoStitches, is (true)
			assertThat stitchGaugeUnit, is (Units.STITCHES_PER_CENTIMETER)
			assertThat rowGaugeUnit, is (Units.ROWS_PER_CENTIMETER)
			assertThat fabricMeasurementUnit, is (Units.CENTIMETER)
			assertThat locale, is (new Locale('fr'))
		}
		builder.with {
			assertEquals baseRendererClass, HtmlRenderer
			assertEquals chartWriterClass, HtmlChartWriter
			assertEquals symbolProviderClass, AireRiverSymbolAdvisor
			assertEquals stylesheetProviderClass, AireRiverSymbolAdvisor
		}
	}
}
