/**
 * 
 */
package com.knitml.core.model.directions.block

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static test.support.JiBXTestUtils.marshalXmlAndCompare
import static test.support.JiBXTestUtils.unmarshalXml

import org.custommonkey.xmlunit.XMLUnit
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.JUnitCore
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.knitml.core.common.Side
import com.knitml.core.common.Wise
import com.knitml.core.model.Pattern

class AtomicBlockOperationsTests {
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
	}
	
	void template() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations">
				  <pattern:directions>
				  </pattern:directions>
				</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0]
		element.with {
		}
		marshalXmlAndCompare(pattern,xml)
	}

	@Test
	void castOn() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" version="0.6">
				  <pattern:directions>
				  	<cast-on yarn-ref="yarn1" style="long-tail" count-as-row="true">10</cast-on>
				  	<cast-on>5</cast-on>
				  </pattern:directions>
				</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0]
		element.with {
			assertThat yarnIdRef, is ('yarn1')
			assertThat style, is ('long-tail')
			assertThat countAsRow, is (true)
			assertThat numberOfStitches, is (10)
		}
		element = pattern.directions.operations[1]
		element.with {
			assertThat yarnIdRef, is (null)
			assertThat style, is (null)
			assertThat countAsRow, is (false)
			assertThat numberOfStitches, is (5)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void pickUpStitches() {
		def xml = '''
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" version="0.6">
			  <pattern:directions>
				<pick-up-stitches>10</pick-up-stitches>
				<pick-up-stitches type="purlwise" yarn-ref="yarn0">15</pick-up-stitches>
              </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def directions = pattern.directions
		directions.operations[0].with {
			assertThat (it instanceof PickUpStitches, is (true))
			assertThat numberOfTimes, is (10)
		}
		directions.operations[1].with {
			assertThat (it instanceof PickUpStitches, is (true))
			assertThat type, is (Wise.PURLWISE)
			assertThat yarnIdRef, is ('yarn0')
			assertThat numberOfTimes, is (15)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void arrangeStitchesOnNeedles() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
				  <pattern:supplies>
					<pattern:yarn-types/>
					<pattern:needle-types>
						<pattern:needle-type id="size1circ" type="circular">
							<pattern:needles>
								<common:needle id="needle1"/>
								<common:needle id="needle2"/>
							</pattern:needles>
						</pattern:needle-type>
					</pattern:needle-types>
					<pattern:accessories/>
				  </pattern:supplies>
				  <pattern:directions>
				  	 <arrange-stitches-on-needles>
				  	 	<needle ref="needle1">10</needle>
				  	 	<needle ref="needle2">20</needle>
				  	 </arrange-stitches-on-needles>
				  </pattern:directions>
				</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def needle1 = pattern.supplies.needles[0]
		def needle2 = pattern.supplies.needles[1]
		def element = pattern.directions.operations[0]
		element.with {
			assertThat needles[0].needle, is (needle1)
			assertThat needles[0].numberOfStitches, is (10)
			assertThat needles[1].needle, is (needle2)
			assertThat needles[1].numberOfStitches, is (20)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void graftTogether() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
				  <pattern:supplies>
					<pattern:yarn-types/>
					<pattern:needle-types>
						<pattern:needle-type id="size1circ" type="circular">
							<pattern:needles>
								<common:needle id="needle1"/>
								<common:needle id="needle2"/>
							</pattern:needles>
						</pattern:needle-type>
					</pattern:needle-types>
					<pattern:accessories/>
				  </pattern:supplies>
				  <pattern:directions>
				  	 <graft-together>
				  	 	<needle ref="needle1"/>
				  	 	<needle ref="needle2"/>
				  	 </graft-together>
				  </pattern:directions>
				</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def needle1 = pattern.supplies.needles[0]
		def needle2 = pattern.supplies.needles[1]
		def element = pattern.directions.operations[0]
		element.with {
			assertThat needles[0], is (needle1)
			assertThat needles[1], is (needle2)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void labelNeedle() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
				  <pattern:supplies>
					<pattern:yarn-types/>
					<pattern:needle-types>
						<pattern:needle-type id="size1circ" type="circular">
							<pattern:needles>
								<common:needle id="needle1"/>
							</pattern:needles>
						</pattern:needle-type>
					</pattern:needle-types>
					<pattern:accessories/>
				  </pattern:supplies>
				  <pattern:directions>
				  	 <label-needle ref="needle1" message-key="key1"/>
				  </pattern:directions>
				</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0]
		def needle1 = pattern.supplies.needles[0]
		element.with {
			assertThat needle, is (needle1)
			assertThat messageKey, is ('key1')
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void useNeedles() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
				  <pattern:supplies>
					<pattern:yarn-types/>
					<pattern:needle-types>
						<pattern:needle-type id="size1circ" type="circular">
							<pattern:needles>
								<common:needle id="needle1"/>
								<common:needle id="needle2"/>
							</pattern:needles>
						</pattern:needle-type>
					</pattern:needle-types>
					<pattern:accessories/>
				  </pattern:supplies>
				  <pattern:directions>
				  	 <use-needles silent="true">
				  	 	<needle ref="needle1"/>
				  	 	<needle ref="needle2"/>
				  	 </use-needles>
				  </pattern:directions>
				</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0]
		def needle1 = pattern.supplies.needles[0]
		def needle2 = pattern.supplies.needles[1]
		element.with {
			assertThat silentRendering, is (true)
			assertThat needles[0], is (needle1)
			assertThat needles[1], is (needle2)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void declareRoundKnitting() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations">
				  <pattern:directions>
				  	<declare-round-knitting/>
				  </pattern:directions>
				</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0]
		assertThat (element instanceof DeclareRoundKnitting, is (true))
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void declareFlatKnitting() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations">
				  <pattern:directions>
				  	<declare-flat-knitting next-row-side="wrong"/>
				  </pattern:directions>
				</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0]
		assertThat (element instanceof DeclareFlatKnitting, is (true))
		assertThat element.nextRowSide, is (Side.WRONG)
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void joinInRound() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" version="0.6">
				  <pattern:directions>
				  	<join-in-round/>
				  </pattern:directions>
				</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0]
		assertThat (element instanceof JoinInRound, is (true))
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void informationInformStitchesAsTrue() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations">
				  <pattern:directions>
				  	<information>
				  		<number-of-stitches number="20"/>
				  	</information>
				  </pattern:directions>
				</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].details[0]
		element.with {
			assertThat inform, is (true)
			assertThat number, is (20)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void informationInformStitchesAsFalse() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations">
				  <pattern:directions>
				  	<information>
				  		<number-of-stitches inform="false" number="20"/>
				  	</information>
				  </pattern:directions>
				</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].details[0]
		element.with {
			assertThat inform, is (false)
			assertThat number, is (20)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void informationMessage() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations">
				  <pattern:directions>
				  	<information>
				  		<message message-key="key1"/>
				  	</information>
				  </pattern:directions>
				</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].details[0]
		element.with {
			assertThat messageKey, is ('key1')
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
}
