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

class LanguageEHTests {
	
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
		XMLUnit.ignoreComments = true
	}
	
	@Test
	void fromHolder() {
		String actual = toXml ("fromHolder 'sh1' { p2, k to end }")
		String expected = '''
			<from-stitch-holder ref="sh1">
				<purl>2</purl>
				<repeat until="end">
					<knit/>
				</repeat>
			</from-stitch-holder>'''
		assertXMLEqual expected, actual
	}
}