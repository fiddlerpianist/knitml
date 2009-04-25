package com.knitml.tools.runner

import groovy.xml.MarkupBuilder

import javax.xml.validation.*
import javax.xml.transform.*
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource

import org.xml.sax.EntityResolver
import org.w3c.dom.ls.LSResourceResolver

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.GnuParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.knitml.core.common.Parameters
import com.knitml.core.model.Pattern
import com.knitml.core.xml.PluggableSchemaResolver
import com.knitml.core.xml.EntityResolverWrapper
import com.knitml.renderer.program.RendererProgram
import com.knitml.renderer.util.SpringConfigurationBuilder
import com.knitml.renderer.util.Configuration
import com.knitml.el.GroovyKnitProgram
import com.knitml.tools.runner.support.RunnerUtils
import com.knitml.validation.ValidationProgram
import com.knitml.validation.context.KnittingContextFactory
import com.knitml.validation.context.impl.DefaultKnittingContextFactory
import com.knitml.validation.visitor.instruction.impl.SpringVisitorFactory

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
		GroovyKnitProgram converter = new GroovyKnitProgram()
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
		RendererProgram renderer = new RendererProgram(configuration.rendererFactory)
		renderer.setOptions(configuration.options)
		renderer.render(parameters)
	}

}
