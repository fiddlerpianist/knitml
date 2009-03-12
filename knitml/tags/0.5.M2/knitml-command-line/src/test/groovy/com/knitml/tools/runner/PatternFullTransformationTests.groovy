package com.knitml.tools.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore
import org.springframework.core.io.ClassPathResource;

import com.knitml.core.common.Parameters
import com.knitml.core.model.Pattern
import com.knitml.el.GroovyKnitProgram

class PatternFullTransformationTests extends RunnerTests {

	protected static GroovyKnitProgram converter
	
	protected InputStream patternResourceStream

	@BeforeClass
	static void setUpClass() {
		converter = new GroovyKnitProgram()
	}

	@After
	void tearDown() throws IOException {
		if (patternResourceStream != null) {
			patternResourceStream.close();
		}
	}
	
	protected void convertValidateAndRender(String patternName) {
		Parameters options = new Parameters()
		patternResourceStream = new ClassPathResource(patternName + ".kel").inputStream
		// convert
		options.reader = new BufferedReader(new InputStreamReader(patternResourceStream))
		options.checkSyntax = true
		String xml = converter.convertToXml(options)
		
		// validate
		options = new Parameters()
		options.reader = new StringReader(xml)
		Pattern pattern = validator.validate (options)

		// revalidate
		options = new Parameters()
		options.pattern = pattern
		pattern = validator.validate (options)

		// render
		options = new Parameters()
		options.pattern = pattern
		renderer.render options
	}

	protected void validateAndRender(String patternName) {
		Parameters options = new Parameters()
		def resource = new ClassPathResource(patternName + ".xml").inputStream
		String xml = resource.text
		options.reader = new StringReader(xml)
		options.checkSyntax = true
		Pattern pattern = validator.validate (options)
		
		options = new Parameters()
		options.pattern = pattern
		renderer.render options
	}
	
	@Test
	void basicSock() {
		convertValidateAndRender 'basic-sock'
	}

	@Test
	void samplerSwatch() {
		convertValidateAndRender 'sampler-swatch'
	}
	
	@Test
	void nutkin2() {
		convertValidateAndRender 'nutkin2'
	}

	static void main(args) {
		JUnitCore.main(PatternFullTransformationTests.name)
	}
}
