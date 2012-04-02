package com.knitml.tools.runner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.knitml.core.model.pattern.Parameters;
import com.knitml.renderer.config.Configuration;
import com.knitml.renderer.config.DefaultModule;
import com.knitml.renderer.program.RendererProgram;
import com.knitml.tools.runner.support.RunnerUtils;
import com.knitml.tools.runner.support.SpringConfigurationBuilder;

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
		Option configFiles = new Option("config", true,
				"Spring application context file(s) used to configure the renderer");
		output.setType("file");
		options.addOption(checksyntax);
		options.addOption(output);
		options.addOption(configFiles);
	}

	public static void main(String[] args) {
		// create the parser
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			String[] applicationContextFiles = line.getOptionValues("config");
			if (applicationContextFiles == null) {
				applicationContextFiles = new String[] { "applicationContext-patternRenderer.xml" };
			}
			SpringConfigurationBuilder builder = new SpringConfigurationBuilder();
			Configuration configuration = builder
					.getConfiguration(applicationContextFiles);
			Module optionsModule = new AbstractModule() {
				protected void configure() {
					bind(Options.class).toInstance(options);
				}
			};
			Injector injector = Guice.createInjector(optionsModule, configuration.getModule(),
					new DefaultModule());
			RendererProgram program = injector
					.getInstance(RendererProgram.class);
			Parameters parameters = RunnerUtils.toParameters(line);
			program.render(parameters);
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			HelpFormatter help = new HelpFormatter();
			help.printHelp("renderPattern [options] filename", options);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			ex.printStackTrace();
		}
	}
}
