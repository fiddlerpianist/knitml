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
class LanguageILTests {
	
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
		XMLUnit.ignoreComments = true
	}
	
	@Test
	void informationalMessageNotInRow() {
		String actual = toXml ("section { co 10 sts, informationalMessage withLabel 'Hello World' }")
		String expected = '''
        <section>
        <cast-on>10</cast-on>
        <information>
          <message label="Hello World"/>
        </information>
        </section>
        '''
        assertXMLEqual expected, actual
	}

	@Test
	void informationalMessageInRow() {
		String actual = toXml ("Row 1: BO all sts, informationalMessage withLabel 'Hello World'")
		String expected = '''
		<row number="1">
          <bind-off-all/>
          <followup-information>
            <message label="Hello World"/>
          </followup-information>
        </row>
        '''
        assertXMLEqual expected, actual
	}
	
	@Test
	void kFromHolder() {
		String actual = toXml ("k 2 sts from holder 'sh1'")
		String expected = '''
			<from-stitch-holder ref="sh1">
				<knit>2</knit>
			</from-stitch-holder>'''
        assertXMLEqual expected, actual
	}

	@Test
	void kWithYarnFromHolder() {
		String actual = toXml ("k 2 sts with 'A' from holder 'sh1'")
		String expected = '''
			<from-stitch-holder ref="sh1">
				<knit yarn-ref="A">2</knit>
			</from-stitch-holder>'''
        assertXMLEqual expected, actual
	}

	@Test
	void k2tblFromHolder() {
		String actual = toXml ("k2 tbl from holder 'sh1'")
		String expected = '''
			<from-stitch-holder ref="sh1">
				<knit loop-to-work="trailing">2</knit>
			</from-stitch-holder>'''
        assertXMLEqual expected, actual
	}
	
	static void main(args) {
		JUnitCore.main(LanguageILTests.name)
	}
}