package com.knitml.el

import static org.custommonkey.xmlunit.XMLAssert.*

import org.custommonkey.xmlunit.XMLUnit
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.core.io.ClassPathResource

import com.knitml.core.common.Parameters

class SampleConversionTests {
	
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
		XMLUnit.ignoreComments = true
	}

	private void compareConvertedKelToXml(String pattern) {
		def parameters = new Parameters()
		parameters.reader = acquireClassPathReader(pattern + ".kel")
		parameters.writer = new StringWriter()
		KelProgram converter = new KelProgram()
		converter.convertToXml(parameters)
		parameters.reader.close()
		assertXMLEqual(acquireClassPathReader(pattern + ".xml"), new StringReader(parameters.writer.toString()))
	}
	
	@Test
	void basicSock() {
		compareConvertedKelToXml 'basic-sock'
	}

	@Test
	void samplerSwatch() {
		compareConvertedKelToXml 'sampler-swatch'
	}

	@Test
	void nutkin2() {
		compareConvertedKelToXml 'nutkin2'
	}
	
	private Reader acquireClassPathReader(String fileName) {
		InputStream patternResourceStream = new ClassPathResource(fileName).getInputStream();
        return new BufferedReader(new InputStreamReader(patternResourceStream))
	}
	
}