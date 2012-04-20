package com.knitml.tools.runner.support;

import static com.knitml.tools.runner.support.PreferenceKeys.CHART_SYMBOL_PROVIDER;
import static com.knitml.tools.runner.support.PreferenceKeys.ENABLE_CHARTS;
import static com.knitml.tools.runner.support.PreferenceKeys.LANGUAGE;
import static com.knitml.tools.runner.support.PreferenceKeys.OPERATIONS_FILES;
import static com.knitml.tools.runner.support.PreferenceKeys.RENDERER;
import static com.knitml.tools.runner.support.PreferenceKeys.SQUARE_GAUGE;
import static com.knitml.tools.runner.support.PreferenceKeys.USE_GREY_NO_STITCH;
import static com.knitml.tools.runner.support.PreferenceKeys.USE_INTERNATIONAL_UNITS;
import static org.apache.commons.lang.StringUtils.trimToNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.knitml.renderer.BaseRendererFactory;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.chart.advisor.impl.TextArtSymbolAdvisor;
import com.knitml.renderer.chart.stylesheet.StylesheetProvider;
import com.knitml.renderer.chart.stylesheet.impl.TextArtStylesheetProvider;
import com.knitml.renderer.chart.symbol.SymbolProvider;
import com.knitml.renderer.chart.writer.ChartWriter;
import com.knitml.renderer.chart.writer.impl.HtmlChartWriter;
import com.knitml.renderer.chart.writer.impl.TextChartWriter;
import com.knitml.renderer.config.Configuration;
import com.knitml.renderer.context.Options;
import com.knitml.renderer.impl.basic.BasicTextRenderer;
import com.knitml.renderer.impl.charting.ChartingRenderer;
import com.knitml.renderer.impl.html.HtmlRenderer;

public class ConfigurationBuilder {

	static final Logger log = LoggerFactory
			.getLogger(ConfigurationBuilder.class);
	
	// variables are used for validating tests, nothing else
	protected Class<? extends Renderer> baseRendererClass;
	protected Class<? extends ChartWriter> chartWriterClass;
	protected Class<? extends StylesheetProvider> stylesheetProviderClass;
	protected Class<? extends SymbolProvider> symbolProviderClass;
	protected String[] messageFiles;

	public Configuration buildConfiguration(Properties props) {
		Options options = buildOptions(props);

		// prepare the renderer factory
		final Class<? extends Renderer> baseRendererClass = findBaseRendererClass(props);
		final Class<? extends SymbolProvider> symbolProviderClass = findSymbolProviderClass(props);
		final Class<? extends StylesheetProvider> stylesheetProviderClass;
		if (StylesheetProvider.class.isAssignableFrom(symbolProviderClass)) {
			stylesheetProviderClass = symbolProviderClass
					.asSubclass(StylesheetProvider.class);
		} else {
			stylesheetProviderClass = TextArtStylesheetProvider.class;
		}

		Module dynamicModule;
		if (options.isGlobalChart()) {
			final Class<? extends ChartWriter> chartWriterClass;

			// this is probably a dangerous assumption to make
			if (HtmlRenderer.class.isAssignableFrom(baseRendererClass)) {
				chartWriterClass = HtmlChartWriter.class;
			} else {
				chartWriterClass = TextChartWriter.class;
			}
			this.chartWriterClass = chartWriterClass;
			dynamicModule = new AbstractModule() {
				protected void configure() {
					install(new FactoryModuleBuilder().implement(
							Renderer.class, ChartingRenderer.class).build(
							RendererFactory.class));
					install(new FactoryModuleBuilder().implement(
							Renderer.class, baseRendererClass).build(
							BaseRendererFactory.class));
					bind(SymbolProvider.class).to(symbolProviderClass);
					bind(ChartWriter.class).to(chartWriterClass);
					bind(StylesheetProvider.class).to(stylesheetProviderClass);
				}
			};
		} else {
			dynamicModule = new AbstractModule() {
				protected void configure() {
					install(new FactoryModuleBuilder().implement(
							Renderer.class, baseRendererClass).build(
							RendererFactory.class));
					bind(StylesheetProvider.class).to(stylesheetProviderClass);
				}
			};
		}
		this.baseRendererClass = baseRendererClass;
		this.stylesheetProviderClass = stylesheetProviderClass;
		this.symbolProviderClass = symbolProviderClass;
		return new Configuration(dynamicModule, options);
	}

	protected Options buildOptions(Properties properties) {
		Options options = new Options();
		Boolean prop = getBoolean(ENABLE_CHARTS, properties);
		options.setGlobalChart(prop == null ? options.isGlobalChart() : prop);
		prop = getBoolean(SQUARE_GAUGE, properties);
		options.setSquareGauge(prop == null ? options.isSquareGauge() : prop);
		prop = getBoolean(USE_GREY_NO_STITCH, properties);
		options.setGreyNoStitches(prop == null ? options.isGreyNoStitches() : prop);
		Boolean internationalUnits = getBoolean(USE_INTERNATIONAL_UNITS,
				properties);
		if (internationalUnits != null) {
			if (internationalUnits.booleanValue()) {
				options.useInternationalUnits();
			} else {
				options.useUsCustomaryUnits();
			}
		}
		String language = trimToNull(properties.getProperty(LANGUAGE));
		if (language != null) {
			options.setLocale(new Locale(language));
		}
		String operationsFiles = trimToNull(properties.getProperty(OPERATIONS_FILES));
		if (operationsFiles != null) {
			List<String> resourceBundles = new ArrayList<String>();
			StringTokenizer files = new StringTokenizer(operationsFiles, ",");
			while (files.hasMoreTokens()) {
				resourceBundles.add(files.nextToken());
			}
			ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
			this.messageFiles = resourceBundles.toArray(new String[0]);
			messageSource.setBasenames(this.messageFiles);
			options.setProgramMessageSource(messageSource);
		}
		return options;
	}

	private Boolean getBoolean(String key, Properties properties) {
		if (!properties.containsKey(key))
			return null;
		return new Boolean(properties.getProperty(key));
	}

	protected Class<? extends SymbolProvider> findSymbolProviderClass(
			Properties props) {
		String symbolProviderName = trimToNull(props
				.getProperty(CHART_SYMBOL_PROVIDER));
		if (symbolProviderName != null) {
			try {
				return Class.forName(symbolProviderName).asSubclass(
						SymbolProvider.class);
			} catch (ClassCastException ex) {
				log.warn(
						"The chart symbol provider specified in the preferences does not resolve to a class which implements {}",
						SymbolProvider.class.getName());
			} catch (Exception ex) {
				log.warn(
						"Could not instantiate the chart symbol provider specified in the properties. Using text art",
						ex);
			}
		}
		return TextArtSymbolAdvisor.class;
	}

	protected Class<? extends Renderer> findBaseRendererClass(
			Properties properties) {
		Class<? extends Renderer> rendererClass;
		String renderer = trimToNull(properties.getProperty(RENDERER));
		if (renderer == null || renderer.toLowerCase().equals("text")) {
			rendererClass = BasicTextRenderer.class;
		} else if (renderer.toLowerCase().equals("html")) {
			rendererClass = HtmlRenderer.class;
		} else {
			try {
				rendererClass = Class.forName(renderer).asSubclass(
						Renderer.class);
			} catch (Exception ex) {
				log.error(
						"Could not create the specified renderer factory class. Will use the default",
						ex);
				rendererClass = BasicTextRenderer.class;
			}
		}
		return rendererClass;
	}

}
