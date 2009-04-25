/**
 * 
 */
package com.knitml.core.model.directions.block

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

@RunWith(JUnit4ClassRunner)
class AtomicBlockOperationsTests {
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
	}
	
	void template() {
		def xml = '''
				<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <directions>
				  </directions>
				</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0]
		element.with {
		}
		marshalXmlAndCompare(pattern,xml)
	}

	@Test
	void castOn() {
		def xml = '''
				<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <directions>
				  	<cast-on yarn-ref="yarn1" style="long-tail" count-as-row="true">10</cast-on>
				  	<cast-on>5</cast-on>
				  </directions>
				</pattern>'''
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
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
				<pick-up-stitches>10</pick-up-stitches>
				<pick-up-stitches type="purlwise" yarn-ref="yarn0">15</pick-up-stitches>
              </directions>
			</pattern>'''
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
				<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <supplies>
					<yarns/>
					<needles>
						<needle-type id="size1circ" type="circular"/>
						<needle id="needle1" typeref="size1circ"/>
						<needle id="needle2" typeref="size1circ"/>
					</needles>
					<accessories/>
				  </supplies>
				  <directions>
				  	 <arrange-stitches-on-needles>
				  	 	<needle ref="needle1">10</needle>
				  	 	<needle ref="needle2">20</needle>
				  	 </arrange-stitches-on-needles>
				  </directions>
				</pattern>'''
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
				<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <supplies>
					<yarns/>
					<needles>
						<needle-type id="size1circ" type="circular"/>
						<needle id="needle1" typeref="size1circ"/>
						<needle id="needle2" typeref="size1circ"/>
					</needles>
					<accessories/>
				  </supplies>
				  <directions>
				  	 <graft-together>
				  	 	<needle ref="needle1"/>
				  	 	<needle ref="needle2"/>
				  	 </graft-together>
				  </directions>
				</pattern>'''
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
				<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <supplies>
					<yarns/>
					<needles>
						<needle-type id="size1circ" type="circular"/>
						<needle id="needle1" typeref="size1circ"/>
					</needles>
					<accessories/>
				  </supplies>
				  <directions>
				  	 <label-needle ref="needle1" message-key="key1"/>
				  </directions>
				</pattern>'''
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
				<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <supplies>
					<yarns/>
					<needles>
						<needle-type id="size1circ" type="circular"/>
						<needle id="needle1" typeref="size1circ"/>
						<needle id="needle2" typeref="size1circ"/>
					</needles>
					<accessories/>
				  </supplies>
				  <directions>
				  	 <use-needles silent="true">
				  	 	<needle ref="needle1"/>
				  	 	<needle ref="needle2"/>
				  	 </use-needles>
				  </directions>
				</pattern>'''
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
				<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <directions>
				  	<declare-round-knitting/>
				  </directions>
				</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0]
		assertThat (element instanceof DeclareRoundKnitting, is (true))
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void declareFlatKnitting() {
		def xml = '''
				<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <directions>
				  	<declare-flat-knitting next-row-side="wrong"/>
				  </directions>
				</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0]
		assertThat (element instanceof DeclareFlatKnitting, is (true))
		assertThat element.nextRowSide, is (Side.WRONG)
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void joinInRound() {
		def xml = '''
				<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <directions>
				  	<join-in-round/>
				  </directions>
				</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0]
		assertThat (element instanceof JoinInRound, is (true))
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void informationInformStitchesAsTrue() {
		def xml = '''
				<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <directions>
				  	<information>
				  		<number-of-stitches number="20"/>
				  	</information>
				  </directions>
				</pattern>'''
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
				<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <directions>
				  	<information>
				  		<number-of-stitches inform="false" number="20"/>
				  	</information>
				  </directions>
				</pattern>'''
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
				<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <directions>
				  	<information>
				  		<message message-key="key1"/>
				  	</information>
				  </directions>
				</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].details[0]
		element.with {
			assertThat messageKey, is ('key1')
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	static void main(args) {
		JUnitCore.main(AtomicBlockOperationsTests.name)
	}
	
}
