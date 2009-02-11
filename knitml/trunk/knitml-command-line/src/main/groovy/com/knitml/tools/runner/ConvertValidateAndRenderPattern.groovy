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
import com.knitml.renderer.context.RenderingContextFactory
import com.knitml.renderer.context.impl.SpringRenderingContextFactory
import com.knitml.renderer.visitor.impl.DefaultVisitorFactory
import com.knitml.renderer.RendererProgram
import com.knitml.el.GroovyKnitProgram
import com.knitml.tools.runner.support.RunnerUtils
import com.knitml.validation.ValidationProgram
import com.knitml.validation.context.KnittingContextFactory
import com.knitml.validation.context.impl.DefaultKnittingContextFactory
import com.knitml.validation.visitor.instruction.impl.SpringVisitorFactory

class ConvertValidateAndRenderPattern {

	private static final options = new Options()
	private static final Logger log = LoggerFactory.getLogger(ConvertPattern)
	
	static {
		def checksyntax = new Option("checksyntax", false,"check the intermediate validated XML's syntax against the KnitML schema")
		def output = new Option("output", true,"File name to output the results")
		def wv = new Option("wv", false, "create the intermediate validated XML")
		output.setType("file")
		options.addOption(checksyntax)
		options.addOption(output)
		options.addOption(wv)
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

		// now validate the document
		Pattern validatedPattern = runValidation(xml, line)

		// now render the document
		runRenderer(validatedPattern, line)
	}

	
	def static Pattern runValidation(String xml, CommandLine line) {
		Parameters parameters = new Parameters()
		parameters.reader = new StringReader(xml)
		// If -wv is specified, write out the validated KnitML file to a
		// temp file
		if (line.hasOption("wv")) {
			File tempFile = obtainTemporaryFile()
			parameters.writer = new BufferedWriter(new FileWriter(tempFile))
		}
		KnittingContextFactory contextFactory = new DefaultKnittingContextFactory()
		ValidationProgram validator = new ValidationProgram(contextFactory, new SpringVisitorFactory())
		return validator.validate(parameters)
	}
	
	def static Pattern runRenderer(Pattern pattern, CommandLine line) {
		Parameters parameters = new Parameters()
		parameters.pattern = pattern
		String[] applicationContextFiles = line.getOptionValues("rendererContextFiles")
		if (applicationContextFiles == null) {
			applicationContextFiles = ["applicationContext-patternRenderer.xml"]
		}
		parameters.writer = RunnerUtils.getWriter(line)
		RenderingContextFactory contextFactory = new SpringRenderingContextFactory(applicationContextFiles)
		RendererProgram renderer = new RendererProgram(contextFactory, new DefaultVisitorFactory())
		renderer.render(parameters)
	}

	def static File obtainTemporaryFile() {
		return new File("pattern-validated.xml")
	}
	
}
