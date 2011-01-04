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
import org.junit.BeforeClass

import com.knitml.core.model.Pattern
import com.knitml.core.common.CrossType
import com.knitml.core.model.directions.inline.FromStitchHolder

class MiscellaneousOperationTests {
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
	}
	
	@Test
	void crossStitches() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
					<row>
						<cross-stitches first="2" next="3" type="front"/>
					</row>
				  </directions>
			</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].operations[0]
		assertThat (element instanceof CrossStitches, is (true))
		element.with {
			assertThat type, is (CrossType.FRONT)
			assertThat first, is (2)
			assertThat next, is (3)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void passPreviousStitchOver() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
					<row>
						<pass-previous-stitch-over>2</pass-previous-stitch-over>
					</row>
				  </directions>
			</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].operations[0]
		assertThat (element instanceof PassPreviousStitchOver, is (true))
		element.with {
			assertThat numberOfTimes, is (2)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void markers() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
					<row>
						<place-marker/>
						<remove-marker/>
					</row>
				  </directions>
			</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].operations[0]
		assertThat (element instanceof PlaceMarker, is (true))
		element = pattern.directions.operations[0].operations[1]
		assertThat (element instanceof RemoveMarker, is (true))
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void repeatUntilEnd() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
					<row>
						<repeat until="end">
							<knit/>
							<purl/>
						</repeat>
					</row>
				  </directions>
			</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def repeat = pattern.directions.operations[0].operations[0]
		assertThat (repeat instanceof Repeat, is(true))
		repeat.with {
			assertThat until, is (Repeat.Until.END)
			assertThat value, is (null)
			assertThat (operations[0] instanceof Knit, is (true))
			assertThat (operations[1] instanceof Purl, is (true))
		}
		marshalXmlAndCompare(pattern,xml)
	}

	@Test
	void repeatUntilBeforeEnd() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
					<row>
						<repeat until="before-end" value="3">
							<knit/>
							<purl/>
						</repeat>
					</row>
				  </directions>
			</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def repeat = pattern.directions.operations[0].operations[0]
		assertThat (repeat instanceof Repeat, is(true))
		repeat.with {
			assertThat until, is (Repeat.Until.BEFORE_END)
			assertThat value, is (3)
			assertThat (operations[0] instanceof Knit, is (true))
			assertThat (operations[1] instanceof Purl, is (true))
		}
		marshalXmlAndCompare(pattern,xml)
	}
	@Test
	void repeatUntilMarker() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
					<row>
						<repeat until="marker">
							<knit/>
							<purl/>
						</repeat>
					</row>
				  </directions>
			</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def repeat = pattern.directions.operations[0].operations[0]
		assertThat (repeat instanceof Repeat, is(true))
		repeat.with {
			assertThat until, is (Repeat.Until.MARKER)
			assertThat value, is (null)
			assertThat (operations[0] instanceof Knit, is (true))
			assertThat (operations[1] instanceof Purl, is (true))
		}
		marshalXmlAndCompare(pattern,xml)
	}

	@Test
	void repeatUntilBeforeMarker() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
					<row>
						<repeat until="before-marker" value="3">
							<knit/>
							<purl/>
						</repeat>
					</row>
				  </directions>
			</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def repeat = pattern.directions.operations[0].operations[0]
		assertThat (repeat instanceof Repeat, is(true))
		repeat.with {
			assertThat until, is (Repeat.Until.BEFORE_MARKER)
			assertThat value, is (3)
			assertThat (operations[0] instanceof Knit, is (true))
			assertThat (operations[1] instanceof Purl, is (true))
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void repeatUntilBeforeGap() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
					<row>
						<repeat until="before-gap" value="3">
							<knit/>
							<purl/>
						</repeat>
					</row>
				  </directions>
			</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def repeat = pattern.directions.operations[0].operations[0]
		assertThat (repeat instanceof Repeat, is(true))
		repeat.with {
			assertThat until, is (Repeat.Until.BEFORE_GAP)
			assertThat value, is (3)
			assertThat (operations[0] instanceof Knit, is (true))
			assertThat (operations[1] instanceof Purl, is (true))
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void repeatUntilTimes() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
			  <directions>
					<row>
						<repeat until="times" value="3">
							<knit/>
							<purl/>
						</repeat>
					</row>
				  </directions>
			</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def repeat = pattern.directions.operations[0].operations[0]
		assertThat (repeat instanceof Repeat, is(true))
		repeat.with {
			assertThat until, is (Repeat.Until.TIMES)
			assertThat value, is (3)
			assertThat (operations[0] instanceof Knit, is (true))
			assertThat (operations[1] instanceof Purl, is (true))
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void anInlineInstructionWithReference() {
		def xml = '''
		<pattern xmlns="http://www.knitml.com/schema/pattern">
			<directions>
				<row>
					<inline-instruction id="thingy"/>
					<inline-instruction-ref ref="thingy"/>
				</row>
			</directions>
		</pattern>'''
		def pattern = unmarshalXml(xml)
		def instruction = pattern.directions.operations[0].operations[0]
		assertThat (instruction instanceof InlineInstruction, is (true))
		instruction.with {
			assertThat id, is('thingy')
		}
		def instructionRef = pattern.directions.operations[0].operations[1]
		assertThat instructionRef.referencedInstruction, is (instruction)
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void usingNeedles() {
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
				  	<row>
				  		<using-needle ref="needle1">
				  			<knit>5</knit>
				  		</using-needle>
				  		<using-needle ref="needle2">
				  			<purl>10</purl>
				  		</using-needle>
				  	</row>
				  </directions>
				</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def row = pattern.directions.operations[0]
		def needle1 = pattern.supplies.needles[0]
		def needle2 = pattern.supplies.needles[1]
				
		row.operations[0].with {
			assertThat needle, is (needle1)
			assertThat operations[0].numberOfTimes, is (5) 
		}
		row.operations[1].with {
			assertThat needle, is (needle2)
			assertThat operations[0].numberOfTimes, is (10) 
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void fromStitchHolder() {
		def xml = '''
				<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <supplies>
					<yarns/>
					<needles/>
					<accessories>
						<stitch-holder id="stitch-holder-1" label="Stitch Holder A"/>
					</accessories>
				  </supplies>
				  <directions>
				  	<row>
				  		<from-stitch-holder ref="stitch-holder-1">
				  			<knit>5</knit>
				  		</from-stitch-holder>
				  	</row>
				  </directions>
				</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def stitchHolder = pattern.supplies.stitchHolders[0]
		def row = pattern.directions.operations[0]
		assertThat ((row.operations[0] instanceof FromStitchHolder), is (true))
		row.operations[0].with {
			assertThat stitchHolder, is (stitchHolder)
			assertThat operations[0].numberOfTimes, is (5) 
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void applyNextRow() {
		def xml = '''
		<pattern xmlns="http://www.knitml.com/schema/pattern">
			<directions>
				<instruction-group id="ig1">
					<instruction id="instruction1"/>
					<row>
						<apply-next-row instruction-ref="instruction1"/>
					</row>
				</instruction-group>
			</directions>
		</pattern>'''
		def pattern = unmarshalXml(xml)
		def instruction = pattern.directions.operations[0].operations[0]
		def row = pattern.directions.operations[0].operations[1]
		row.with {
			assertThat (operations[0] instanceof ApplyNextRow, is (true))
			assertThat operations[0].instructionRef, is (instruction)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
}
