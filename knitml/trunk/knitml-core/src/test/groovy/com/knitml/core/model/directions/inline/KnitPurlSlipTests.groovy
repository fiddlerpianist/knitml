/**
 * 
 */
package com.knitml.core.model.directions.inline

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static test.support.JiBXTestUtils.unmarshalXml
import static test.support.JiBXTestUtils.marshalXmlAndCompare

import org.custommonkey.xmlunit.XMLUnit

import org.junit.Test
import org.junit.Ignore
import org.junit.BeforeClass
import org.junit.internal.runners.JUnit4ClassRunner
import org.junit.runner.JUnitCore
import org.junit.runner.RunWith

import com.knitml.core.model.Pattern
import com.knitml.core.common.Side
import com.knitml.core.common.Wise
import com.knitml.core.common.LoopToWork
import com.knitml.core.common.YarnPosition

@RunWith(JUnit4ClassRunner)
class KnitPurlSlipTests {
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
	}
	
	void template() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
					<row>
					</row>
				  </directions>
			</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].operations[0]
		element.with {
		}
		marshalXmlAndCompare(pattern,xml)
	}

	@Test
	void knit() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
					<row>
						<knit yarn-ref="knit0" loop-to-work="leading"/>
						<knit>5</knit>
					</row>
				  </directions>
			</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].operations[0]
		element.with {
			assertThat yarnIdRef, is ('knit0')
			assertThat loopToWork, is (LoopToWork.LEADING)
			assertThat numberOfTimes, is (null)
		}
		element = pattern.directions.operations[0].operations[1]
		element.with {
			assertThat yarnIdRef, is (null)
			assertThat loopToWork, is (null)
			assertThat numberOfTimes, is (5)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	@Test
	void purl() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
					<row>
						<purl yarn-ref="knit0" loop-to-work="leading"/>
						<purl>5</purl>
					</row>
				  </directions>
			</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].operations[0]
		element.with {
			assertThat yarnIdRef, is ('knit0')
			assertThat loopToWork, is (LoopToWork.LEADING)
			assertThat numberOfTimes, is (null)
		}
		element = pattern.directions.operations[0].operations[1]
		element.with {
			assertThat yarnIdRef, is (null)
			assertThat loopToWork, is (null)
			assertThat numberOfTimes, is (5)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void slip() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
					<row>
						<slip type="knitwise" yarn-position="front" />
						<slip type="purlwise" yarn-position="back">5</slip>
					</row>
				  </directions>
			</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].operations[0]
		element.with {
			assertThat type, is (Wise.KNITWISE)
			assertThat yarnPosition, is (YarnPosition.FRONT)
			assertThat numberOfTimes, is (null)
		}
		element = pattern.directions.operations[0].operations[1]
		element.with {
			assertThat type, is (Wise.PURLWISE)
			assertThat yarnPosition, is (YarnPosition.BACK)
			assertThat numberOfTimes, is (5)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	static void main(args) {
		JUnitCore.main(KnitPurlSlipTests.name)
	}
	
}
