package com.knitml.tools.runner


import javax.xml.transform.*
import javax.xml.validation.*

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.google.inject.Guice
import com.google.inject.Injector
import com.knitml.core.model.pattern.Parameters
import com.knitml.core.model.pattern.Pattern
import com.knitml.el.KelProgram
import com.knitml.renderer.config.Configuration
import com.knitml.renderer.config.DefaultModule
import com.knitml.renderer.program.RendererProgram
import com.knitml.tools.runner.support.RunnerUtils
import com.knitml.tools.runner.support.SpringConfigurationBuilder

class ConvertAndRenderPattern {

	private static final options = new Options()
	private static final Logger log = LoggerFactory.getLogger(ConvertPattern)
	
	static {
		def checksyntax = new Option("checksyntax", false,"check the intermediate validated XML's syntax against the KnitML schema")
		def output = new Option("output", true,"File name to output the results")
		output.setType("file")
		def configFiles = new Option("config", true,"Spring application context file(s) used to configure the renderer")
		options.addOption(checksyntax)
		options.addOption(output)
		options.addOption(configFiles)
	}
	
	static void main(args) {

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
			String[] applicationContextFiles = line.getOptionValues("config")
			if (applicationContextFiles == null) {
				applicationContextFiles = ["applicationContext-patternRenderer.xml"]
			}

		SpringConfigurationBuilder builder = new SpringConfigurationBuilder()

		Configuration configuration = builder.getConfiguration(applicationContextFiles)
		Injector injector = Guice.createInjector(configuration.getModule(), new DefaultModule())
		RendererProgram renderer = injector.getInstance(RendererProgram)
		renderer.setOptions(configuration.options)
		renderer.render(parameters)
	}

}
