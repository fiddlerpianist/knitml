package com.knitml.el

import org.junit.Test
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.junit.runner.JUnitCore
import org.junit.internal.runners.JUnit4ClassRunner
import org.custommonkey.xmlunit.XMLUnit

import com.knitml.core.common.Parameters
import com.knitml.el.GroovyKnitProgram

import static org.custommonkey.xmlunit.XMLAssert.*
import static com.knitml.el.KelUtils.toXml

class LanguageMPTests {
	
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
		XMLUnit.ignoreComments = true
	}
	
	@Test
	void ppso() {
		String actual = toXml ("ppso 3")
		String expected = '''
        <pass-previous-stitch-over>3</pass-previous-stitch-over>
        '''
        assertXMLEqual expected, actual
	}
	
	@Test
	void ppsoNoArgs() {
		String actual = toXml ("ppso")
		String expected = '<pass-previous-stitch-over/>'
        assertXMLEqual expected, actual
	}
	
}