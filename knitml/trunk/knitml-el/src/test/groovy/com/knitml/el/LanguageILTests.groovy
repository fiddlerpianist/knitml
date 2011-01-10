package com.knitml.el

import org.junit.Test
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.junit.runner.JUnitCore
import org.junit.internal.runners.JUnit4ClassRunner
import org.custommonkey.xmlunit.XMLUnit

import com.knitml.core.common.Parameters
import com.knitml.el.KelProgram

import static org.custommonkey.xmlunit.XMLAssert.*
import static com.knitml.el.KelUtils.toXml

class LanguageILTests {
	
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
		XMLUnit.ignoreComments = true
	}
	
	@Test
	void increaseIntoNextStitch() {
		String actual = toXml ("increaseIntoNextStitch { p1, k1, p1 }")
		String expected = '''
        <increase-into-next-stitch>
          <purl>1</purl>
          <knit>1</knit>
          <purl>1</purl>
        </increase-into-next-stitch>
        '''
        assertXMLEqual expected, actual
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
	void kTo2BeforeEnd() {
		String actual = toXml ("k to 2 sts before end")
		String expected = '''
			<repeat until="before-end" value="2">
				<knit/>
			</repeat>'''
        assertXMLEqual expected, actual
	}

	@Test
	void kTo2FromEnd() {
		String actual = toXml ("k to 2 sts from end")
		String expected = '''
			<repeat until="before-end" value="2">
				<knit/>
			</repeat>'''
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
	
	@Test
	void k2tog() {
		String actual = toXml ("k2tog 3")
		String expected = '''<decrease type="k2tog">3</decrease>'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void k3tog() {
		String actual = toXml ("k3tog 3")
		String expected = '''<double-decrease type="k3tog">3</double-decrease>'''
        assertXMLEqual expected, actual
	}

	@Test
	void kfb() {
		String actual = toXml ("kfb 3 times")
		String expected = '''<increase type="kfb">3</increase>'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void longRound() {
		String actual = toXml ("longRound")
		String expected = '''<row type="round" long="true"/>'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void longRow() {
		String actual = toXml ("longRow")
		String expected = '''<row long="true"/>'''
        assertXMLEqual expected, actual
	}
	
}