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

import com.knitml.core.common.CrossType
import com.knitml.core.common.ValidationException
import com.knitml.core.model.operations.inline.ApplyNextRow
import com.knitml.core.model.operations.inline.CrossStitches
import com.knitml.core.model.operations.inline.FromStitchHolder
import com.knitml.core.model.operations.inline.InlineInstruction
import com.knitml.core.model.operations.inline.Knit
import com.knitml.core.model.operations.inline.PassPreviousStitchOver
import com.knitml.core.model.operations.inline.PlaceMarker
import com.knitml.core.model.operations.inline.Purl
import com.knitml.core.model.operations.inline.RemoveMarker
import com.knitml.core.model.operations.inline.Repeat
import com.knitml.core.model.operations.inline.Slip
import com.knitml.core.model.pattern.Pattern

class MiscellaneousOperationTests {
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
	}
	
	@Test
	void crossStitches() {
		def xml = '''
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<cross-stitches first="2" next="3" skip="5" type="front" skip-type="back"/>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def element = pattern.directions.operations[0].operations[0]
		((CrossStitches)element).with {
			assertThat type, is (CrossType.FRONT)
			assertThat first, is (2)
			assertThat next, is (3)
			assertThat skip, is (5)
			assertThat skipType, is (CrossType.BACK)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test(expected=ValidationException)
	void invalidCrossStitches() {
		def xml = '''
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<cross-stitches first="2" next="3" type="front" skip-type="back"/>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
	}
	
	@Test
	void passPreviousStitchOver() {
		def xml = '''
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<pass-previous-stitch-over>2</pass-previous-stitch-over>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
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
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<place-marker/>
						<remove-marker/>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
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
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<repeat until="end">
							<knit/>
							<purl/>
						</repeat>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
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
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<repeat until="before-end" value="3">
							<knit/>
							<purl/>
						</repeat>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
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
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<repeat until="marker">
							<knit/>
							<purl/>
						</repeat>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
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
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<repeat until="before-marker" value="3">
							<knit/>
							<purl/>
						</repeat>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
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
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<repeat until="before-gap" value="3">
							<knit/>
							<purl/>
						</repeat>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
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
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:directions>
					<row>
						<repeat until="times" value="3">
							<knit/>
							<purl/>
						</repeat>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
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
		<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			<pattern:directions>
				<row>
					<inline-instruction id="thingy"/>
					<inline-instruction-ref ref="thingy"/>
				</row>
			</pattern:directions>
		</pattern:pattern>'''
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
				  	<row>
				  		<using-needle ref="needle1">
				  			<knit>5</knit>
				  		</using-needle>
				  		<using-needle ref="needle2">
				  			<purl>10</purl>
				  		</using-needle>
				  	</row>
				  </pattern:directions>
				</pattern:pattern>'''
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
	void stitchGroup() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
				  <pattern:directions>
				  	<row>
				  		<group size="5">
				  			<slip>1</slip>
							<knit>1</knit>
						</group>
				  	</row>
				  </pattern:directions>
				</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def row = pattern.directions.operations[0]
				
		row.operations[0].with {
			assertThat size, is (5) 
			assertThat ((operations[0] instanceof Slip), is (true))
			assertThat ((operations[1] instanceof Knit), is (true))
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void fromStitchHolder() {
		def xml = '''
				<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
				  <pattern:supplies>
					<pattern:yarn-types/>
					<pattern:needle-types/>
					<pattern:accessories>
						<common:stitch-holder id="stitch-holder-1" label="Stitch Holder A"/>
					</pattern:accessories>
				  </pattern:supplies>
				  <pattern:directions>
				  	<row>
				  		<from-stitch-holder ref="stitch-holder-1">
				  			<knit>5</knit>
				  		</from-stitch-holder>
				  	</row>
				  </pattern:directions>
				</pattern:pattern>'''
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
		<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			<pattern:directions>
				<pattern:instruction-group id="ig1">
					<instruction id="instruction1"/>
					<row>
						<apply-next-row instruction-ref="instruction1"/>
					</row>
				</pattern:instruction-group>
			</pattern:directions>
		</pattern:pattern>'''
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
