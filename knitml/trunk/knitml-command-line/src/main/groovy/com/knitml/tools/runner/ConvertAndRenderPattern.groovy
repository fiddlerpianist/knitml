package com.knitml.tools.runner


import java.io.File
import java.io.InputStream
import java.util.Properties

import javax.xml.transform.*
import javax.xml.validation.*

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.DefaultResourceLoader

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import com.knitml.core.model.pattern.Parameters
import com.knitml.core.model.pattern.Pattern
import com.knitml.el.KelProgram
import com.knitml.renderer.config.Configuration
import com.knitml.renderer.config.DefaultModule
import com.knitml.renderer.program.RendererProgram
import com.knitml.tools.runner.support.ConfigurationBuilder
import com.knitml.tools.runner.support.RelativeFileSystemResourceLoader
import com.knitml.tools.runner.support.RunnerUtils

class ConvertAndRenderPattern {

	private static final Options options = new Options()
	private static final Logger log = LoggerFactory.getLogger(ConvertPattern)
	
	static {
		def checksyntax = new Option("checksyntax", false,"check the intermediate validated XML's syntax against the KnitML schema")
		def output = new Option("output", true,"File name to output the results")
		output.setType("file")
		def configFile = new Option("config", true,"Property file used to configure the renderer")
		options.addOption(checksyntax)
		options.addOption(output)
		options.addOption(configFile)
	}
	
	static void main(String[] args) {
		main(args, null)
	}
	
	static void main(String[] args, String fromKnitmlCommandName) {
		// create the parser
		CommandLineParser parser = new GnuParser()
		// parse the command line arguments
		CommandLine line = parser.parse(options, args)
		
		// convert the document
		def parameters = new Parameters()
		parameters.reader = RunnerUtils.getReader(line) 
		parameters.checkSyntax = RunnerUtils.getCheckSyntax(line)
		KelProgram converter = new KelProgram()
		String xml = converter.convertToXml(parameters)

		// now validate / render the document
		runRenderer(xml, line)
	}

	
	def static Pattern runRenderer(String xml, CommandLine line) {
		Parameters parameters = new Parameters()
		parameters.reader = new StringReader(xml)
		parameters.writer = RunnerUtils.getWriter(line)
		
		String rendererPropertiesFile = line.getOptionValue("config")
		if (rendererPropertiesFile == null) {
			rendererPropertiesFile = "renderer.properties"
		}
		InputStream is = new DefaultResourceLoader().getResource(rendererPropertiesFile).getInputStream()
		Properties rendererProperties = new Properties()
		rendererProperties.load(is)
		is.close()
		final Configuration configuration = new ConfigurationBuilder().buildConfiguration(rendererProperties)
		File readerFile = RunnerUtils.getReaderFile(line)
		if (readerFile.exists()) {
			configuration.options.patternMessageResourceLoader = new RelativeFileSystemResourceLoader(readerFile)
		}

		Module optionsModule = new AbstractModule() {
			protected void configure() {
				bind(com.knitml.renderer.context.Options.class).toInstance(configuration.options)
			}
		}
		Injector injector = Guice.createInjector(optionsModule,	configuration.module, new DefaultModule())
		RendererProgram program = injector.getInstance(RendererProgram)
		program.render(parameters)
	}

}
