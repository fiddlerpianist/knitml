package com.knitml.tools

import org.springframework.core.io.ClassPathResource

import org.junit.Test
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.junit.runner.JUnitCore
import org.junit.internal.runners.JUnit4ClassRunner
import org.custommonkey.xmlunit.XMLUnit

import com.knitml.core.common.Parameters
import com.knitml.el.GroovyKnitProgram

import static org.custommonkey.xmlunit.XMLAssert.*

@RunWith(JUnit4ClassRunner)
class KelToXmlComparisonTests {
	
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
		XMLUnit.ignoreComments = true
	}

	private void compareConvertedKelToXml(String pattern) {
		def parameters = new Parameters()
		parameters.reader = acquireClassPathReader(pattern + ".kel")
		parameters.writer = new StringWriter()
		GroovyKnitProgram converter = new GroovyKnitProgram()
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
	
	static void main(args) {
		JUnitCore.main(KelToXmlComparisonTests.name)
	}
}