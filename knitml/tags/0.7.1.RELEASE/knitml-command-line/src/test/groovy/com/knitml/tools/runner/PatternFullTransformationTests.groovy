package com.knitml.tools.runner;

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.core.io.ClassPathResource

import com.google.inject.Guice;
import com.google.inject.Inject
import com.knitml.core.model.pattern.Parameters
import com.knitml.el.KelProgram
import com.knitml.validation.context.KnittingContextFactory
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;

class PatternFullTransformationTests extends RunnerTests {

	protected static KelProgram converter
	
	protected InputStream patternResourceStream

	@BeforeClass
	static void setUpClass() {
		converter = new KelProgram()
	}

	@After
	void tearDown() throws IOException {
		if (patternResourceStream != null) {
			patternResourceStream.close();
		}
	}
	
	protected void convertValidateAndRender(String patternName) {
		Parameters parameters = new Parameters()
		patternResourceStream = new ClassPathResource(patternName + ".kel").inputStream
		// convert
		parameters.reader = new BufferedReader(new InputStreamReader(patternResourceStream))
		parameters.checkSyntax = true
		String xml = converter.convertToXml(parameters)
		parameters.reader.close()
		
		// validate
		parameters = new Parameters()
		parameters.reader = new StringReader(xml)
		renderer.render parameters
	}

	protected void validateAndRender(String patternName) {
		Parameters parameters = new Parameters()
		def resource = new ClassPathResource(patternName + ".xml").inputStream
		String xml = resource.text
		resource.close()
		parameters.reader = new StringReader(xml)
		parameters.checkSyntax = true
		renderer.render parameters
	}
	
	@Test
	void samplerSwatch() {
		convertValidateAndRender 'sampler-swatch'
	}
	
	@Test
	void basicSock() {
		convertValidateAndRender 'basic-sock'
	}
	
	@Test
	void nutkin2() {
		convertValidateAndRender 'nutkin2'
	}

	@Test
	void banff() {
		convertValidateAndRender 'banff-l-xl'
	}

}
