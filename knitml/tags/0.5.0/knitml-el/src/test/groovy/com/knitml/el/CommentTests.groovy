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

@RunWith(JUnit4ClassRunner)
class CommentTests {
	
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
		XMLUnit.ignoreComments = true
	}
	
	@Test
	void commentedPattern() {
		String actual = toXml( '''
    // comment starting at front of line
    Directions {      // comment after open curly brace
    	// comment before function...    Row: k10
        Row: k5    // comment after a "colonized closure"
        state sts // comment after function with attributes
    }
		''')

		String expected = '''
			<directions>
				<row>
					<knit>5</knit>
				</row>
				<information>
					<number-of-stitches/>
				</information>
			</directions>
'''
        assertXMLEqual expected, actual
	}
	
	static void main(args) {
		JUnitCore.main(CommentTests.name)
	}
}