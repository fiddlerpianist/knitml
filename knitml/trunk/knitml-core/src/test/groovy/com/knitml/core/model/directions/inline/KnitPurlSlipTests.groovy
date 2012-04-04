/**
 * 
 */
package com.knitml.core.model.directions.inline

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static test.support.JiBXTestUtils.marshalXmlAndCompare
import static test.support.JiBXTestUtils.unmarshalXml

import org.custommonkey.xmlunit.XMLUnit
import org.junit.BeforeClass
import org.junit.Test

import com.knitml.core.common.LoopToWork
import com.knitml.core.common.SlipDirection
import com.knitml.core.common.Wise
import com.knitml.core.common.YarnPosition
import com.knitml.core.model.operations.inline.Knit
import com.knitml.core.model.operations.inline.Purl
import com.knitml.core.model.operations.inline.SlipToStitchHolder
import com.knitml.core.model.pattern.Pattern

class KnitPurlSlipTests {
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
	}
	
	void template() {
		def xml = '''
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].operations[0]
		element.with {
		}
		marshalXmlAndCompare(pattern,xml)
	}

	@Test
	void knit() {
		def xml = '''
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<knit yarn-ref="knit0" loop-to-work="leading" rows-below="1"/>
						<knit>5</knit>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = (Knit)pattern.directions.operations[0].operations[0]
		element.with {
			assertThat yarnIdRef, is ('knit0')
			assertThat loopToWork, is (LoopToWork.LEADING)
			assertThat rowsBelow, is (1)
			assertThat numberOfTimes, is (null)
		}
		element = (Knit)pattern.directions.operations[0].operations[1]
		element.with {
			assertThat yarnIdRef, is (null)
			assertThat loopToWork, is (null)
			assertThat rowsBelow, is (null)
			assertThat numberOfTimes, is (5)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void purl() {
		def xml = '''
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<purl yarn-ref="knit0" loop-to-work="leading" rows-below="1"/>
						<purl>5</purl>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = (Purl)pattern.directions.operations[0].operations[0]
		element.with {
			assertThat yarnIdRef, is ('knit0')
			assertThat loopToWork, is (LoopToWork.LEADING)
			assertThat rowsBelow, is (1)
			assertThat numberOfTimes, is (null)
		}
		element = (Purl)pattern.directions.operations[0].operations[1]
		element.with {
			assertThat yarnIdRef, is (null)
			assertThat loopToWork, is (null)
			assertThat rowsBelow, is (null)
			assertThat numberOfTimes, is (5)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void workEven() {
		def xml = '''
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<work-even yarn-ref="knit0"/>
						<work-even>5</work-even>
					</row>
			  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].operations[0]
		element.with {
			assertThat yarnIdRef, is ('knit0')
			assertThat numberOfTimes, is (null)
		}
		element = pattern.directions.operations[0].operations[1]
		element.with {
			assertThat yarnIdRef, is (null)
			assertThat numberOfTimes, is (5)
		}
		marshalXmlAndCompare(pattern,xml)
	}

	@Test
	void slip() {
		def xml = '''
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<slip type="knitwise" yarn-position="front" />
						<slip type="purlwise" yarn-position="back" direction="reverse">5</slip>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].operations[0]
		element.with {
			assertThat type, is (Wise.KNITWISE)
			assertThat yarnPosition, is (YarnPosition.FRONT)
			assertThat numberOfTimes, is (null)
			assertThat direction, is (null)
		}
		element = pattern.directions.operations[0].operations[1]
		element.with {
			assertThat type, is (Wise.PURLWISE)
			assertThat yarnPosition, is (YarnPosition.BACK)
			assertThat numberOfTimes, is (5)
			assertThat direction, is (SlipDirection.REVERSE)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void slipToStitchHolder() {
		def xml = '''
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:supplies>
			    <pattern:yarn-types/>
			    <pattern:needle-types/>
                <pattern:accessories>
              	  <common:stitch-holder id="stitch-holder-1" label="Stitch Holder 1"/>
                </pattern:accessories>
              </pattern:supplies>
			  <pattern:directions>
					<row>
						<slip-to-stitch-holder ref="stitch-holder-1">25</slip-to-stitch-holder>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].operations[0]
		assertThat element instanceof SlipToStitchHolder, is (true)
		element.with {
			assertThat stitchHolder, is (pattern.supplies.stitchHolders[0])
			assertThat numberOfStitches, is (25)
		}
		marshalXmlAndCompare(pattern,xml)
	}

	@Test
	void noStitch() {
		def xml = '''
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<no-stitch/>
						<no-stitch>5</no-stitch>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].operations[0]
		element.with {
			assertThat numberOfStitches, is (null)
		}
		element = pattern.directions.operations[0].operations[1]
		element.with {
			assertThat numberOfStitches, is (5)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
}
