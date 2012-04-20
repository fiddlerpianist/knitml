package com.knitml.tools.runner;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.knitml.core.model.pattern.Parameters;
import com.knitml.renderer.config.Configuration;
import com.knitml.renderer.config.DefaultModule;
import com.knitml.renderer.program.RendererProgram;
import com.knitml.tools.runner.support.ConfigurationBuilder;
import com.knitml.tools.runner.support.RelativeFileSystemResourceLoader;
import com.knitml.tools.runner.support.RunnerUtils;

public class RenderPattern {

	private final static Logger log = LoggerFactory
			.getLogger(RenderPattern.class);

	private static final Options options = new Options();

	static {
		Option checksyntax = new Option("checksyntax", false,
				"check the syntax against the KnitML schema");
		Option output = new Option("output", true,
				"File name to output the results");
		output.setType("file");
		Option configFile = new Option("config", true,
				"Property file used to configure the renderer");
		output.setType("file");
		options.addOption(checksyntax);
		options.addOption(output);
		options.addOption(configFile);
	}

	public static void main(String[] args) {
		main(args, null);
	}

	public static void main(String[] args, String fromKnitmlCommandName) {
		String helpString = (fromKnitmlCommandName != null ? ("knitml " + fromKnitmlCommandName)
				: RenderPattern.class.getName())
				+ " [options] filename";
		// create the parser
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			// find and load the properties file
			String rendererPropertiesFile = line.getOptionValue("config");
			if (rendererPropertiesFile == null) {
				rendererPropertiesFile = "renderer.properties";
			}
			InputStream is = new DefaultResourceLoader().getResource(
					rendererPropertiesFile).getInputStream();
			Properties rendererProperties = new Properties();
			rendererProperties.load(is);
			is.close();

			// build the configuration and create Modules and Injector
			final Configuration configuration = new ConfigurationBuilder()
					.buildConfiguration(rendererProperties);
			File readerFile = RunnerUtils.getReaderFile(line);
			if (readerFile.exists()) {
				configuration.getOptions().setPatternMessageResourceLoader(
						new RelativeFileSystemResourceLoader(readerFile));
			}

			Module optionsModule = new AbstractModule() {
				protected void configure() {
					bind(com.knitml.renderer.context.Options.class).toInstance(
							configuration.getOptions());
				}
			};
			Injector injector = Guice.createInjector(optionsModule,
					configuration.getModule(), new DefaultModule());

			// run the program
			RendererProgram program = injector
					.getInstance(RendererProgram.class);
			Parameters parameters = RunnerUtils.toParameters(line);
			program.render(parameters);
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			HelpFormatter help = new HelpFormatter();
			help.printHelp(helpString, options);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			System.err.println("Couldn't process the pattern. "
					+ ex.getMessage());
			HelpFormatter help = new HelpFormatter();
			help.printHelp(helpString, options);
		}
	}
}
