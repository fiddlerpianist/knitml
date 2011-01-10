/**
 * 
 */
package com.knitml.core.model.directions.block

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.core.IsNot.not
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertNotNull
import static test.support.JiBXTestUtils.unmarshalXml
import static test.support.JiBXTestUtils.marshalXmlAndCompare

import static com.knitml.core.common.KnittingShape.ROUND;

import javax.measure.Measure

import org.custommonkey.xmlunit.XMLUnit

import org.junit.Test
import org.junit.BeforeClass
import org.junit.internal.runners.JUnit4ClassRunner
import org.junit.runner.JUnitCore
import org.junit.runner.RunWith

import com.knitml.core.model.directions.block.InstructionGroup
import com.knitml.core.model.directions.block.Instruction
import com.knitml.core.model.directions.block.Row
import com.knitml.core.model.directions.inline.Knit
import com.knitml.core.model.directions.inline.Purl
import com.knitml.core.model.Pattern
import com.knitml.core.model.directions.expression.StitchCount

import com.knitml.core.common.KnittingShape
import com.knitml.core.common.RowDefinitionScope 
import com.knitml.core.common.Side
import com.knitml.core.units.Units
import com.knitml.core.common.ValidationException

@RunWith(JUnit4ClassRunner)
class RowsAndInstructionsTests {
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
	}
	
	@Test
	void directionsAndInstructionGroup() {
		def xml = '''
		<pattern xmlns="http://www.knitml.com/schema/pattern">
				  <directions>
				    <instruction-group id="main" message-key="mk1" label="Do it" reset-row-count="true" />
				  </directions>
				</pattern>'''
		Pattern pattern = unmarshalXml(xml)
		assertNotNull pattern.directions
		InstructionGroup group = pattern.directions.operations[0]
		assertThat group.id, is('main')
		assertThat group.messageKey, is('mk1')
		assertThat group.label, is('Do it')
		assertTrue group.resetRowCount
		marshalXmlAndCompare(pattern,xml)
	}

	
	@Test(expected=ValidationException)
	void anInstructionWithRowsAndAnEachRowOf() {
		def xml = '''
		<pattern xmlns="http://www.knitml.com/schema/pattern">
			<directions>
				<instruction id="thingy"/>
				<instruction id="test">
					<row/>
					<for-each-row-in-instruction ref="thingy"/>
				</instruction>
			</directions>
		</pattern>'''
		def pattern = unmarshalXml(xml)
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void anInstruction() {
		def xml = '''
		<pattern xmlns="http://www.knitml.com/schema/pattern">
			<directions>
				<instruction id="thingy1" shape="round" label="Label 1" message-key="label1" row-count="25"/>
				<instruction id="thingy2"/>
			</directions>
		</pattern>'''
		def pattern = unmarshalXml(xml)
		def instruction = pattern.directions.operations[0]
		assertThat (instruction instanceof Instruction, is (true))
		instruction.with {
			assertThat id, is('thingy1')
			assertThat knittingShape, is(ROUND)
			assertThat label, is('Label 1')
			assertThat messageKey, is('label1')
			assertThat rowCount, is(25)
		}
		instruction = pattern.directions.operations[1]
		instruction.with {
			assertThat id, is('thingy2')
			assertThat knittingShape, is(null)
			assertThat label, is(null)
			assertThat messageKey, is(null)
			assertThat rowCount, is(null)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void anInstructionWithReference() {
		def xml = '''
		<pattern xmlns="http://www.knitml.com/schema/pattern">
			<directions>
				<instruction id="thingy"/>
				<instruction-ref ref="thingy"/>
			</directions>
		</pattern>'''
		def pattern = unmarshalXml(xml)
		def instruction = pattern.directions.operations[0]
		assertThat (instruction instanceof Instruction, is (true))
		instruction.with {
			assertThat id, is('thingy')
		}
		def instructionRef = pattern.directions.operations[1]
		assertThat instructionRef.ref, is (instruction)
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void twoRows() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directions>
					<row type="round" number="1 3 4 5"
						assign-row-number="false" short="true"
                        long="true"
						inform-side="true" yarn-ref="yarn1"
						reset-row-count="true" side="right" subsequent="even" />
					<row number="2"/>
				</directions>
			</pattern>'''
		def pattern = unmarshalXml(xml)
		
		List<Row> rows = pattern.directions.operations
		rows[0].with {
			assertThat type, is (KnittingShape.ROUND)
			int[] expected = [1,3,4,5]
			assertThat numbers, is (expected)
			assertThat assignRowNumber, is (false)
			assertThat shortRow, is (true)
			assertThat longRow, is (true)
			assertThat informSide, is (true)
			assertThat yarnIdRef, is ('yarn1')
			assertThat resetRowCount, is (true)
			assertThat side, is (Side.RIGHT)
			assertThat subsequent, is (RowDefinitionScope.EVEN)
		}
		rows[1].with {
			assertThat type, is (null)
			int[] expected = [2]
			assertThat numbers, is (expected)
			assertThat assignRowNumber, is (true)
			assertThat shortRow, is (false)
			assertThat longRow, is (false)
			assertThat informSide, is (false)
			assertThat yarnIdRef, is (null)
			assertThat resetRowCount, is (false)
			assertThat side, is (null)
			assertThat subsequent, is (null)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void rowWithInformation() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directions>
					<row>
					  <information>
						<message label="Test"/>
					  </information>
					  <knit>5</knit>
					  <followup-information>
					  	<number-of-stitches number="5"/>
					  </followup-information>
					</row>
				</directions>
			</pattern>'''
		def pattern = unmarshalXml(xml)
		
		List<Row> rows = pattern.directions.operations
		rows[0].with {
			assertThat information, not (null)
			assertThat information.details[0].label, is ('Test')
			assertThat ((operations[0] instanceof Knit), is (true))
			assertThat followupInformation.details[0].number, is (5)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void repeatInstructionAdditionalTimes() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directions>
					<instruction id="thingy"/>
					<repeat-instruction ref="thingy">
						<additional-times>3</additional-times>
					</repeat-instruction>
				</directions>
			</pattern>'''
		def pattern = unmarshalXml(xml)
		
		def repeatInstruction = pattern.directions.operations[1]
		repeatInstruction.with {
			assertThat ref, is (pattern.directions.operations[0])
			assertThat until, is (RepeatInstruction.Until.ADDITIONAL_TIMES)
			assertThat value, is (3)
		}
		marshalXmlAndCompare(pattern,xml)
	}

	@Test
	void repeatInstructionUntilDesiredLength() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directions>
					<instruction id="thingy"/>
					<repeat-instruction ref="thingy">
						<until-desired-length/>
					</repeat-instruction>
				</directions>
			</pattern>'''
		def pattern = unmarshalXml(xml)
		
		def repeatInstruction = pattern.directions.operations[1]
		repeatInstruction.with {
			assertThat ref, is (pattern.directions.operations[0])
			assertThat until, is (RepeatInstruction.Until.UNTIL_DESIRED_LENGTH)
			assertThat value, is (null)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void repeatInstructionUntilStitchesRemain() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directions>
					<instruction id="thingy"/>
					<repeat-instruction ref="thingy">
						<until-stitches-remain>10</until-stitches-remain>
					</repeat-instruction>
				</directions>
			</pattern>'''
		def pattern = unmarshalXml(xml)
		
		def repeatInstruction = pattern.directions.operations[1]
		repeatInstruction.with {
			assertThat ref, is (pattern.directions.operations[0])
			assertThat until, is (RepeatInstruction.Until.UNTIL_STITCHES_REMAIN)
			assertThat value, is (10)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void repeatInstructionUntilStitchesRemainOnNeedles() {
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
					<instruction id="thingy"/>
					<repeat-instruction ref="thingy">
						<until-stitches-remain-on-needles>
							<needle ref="needle1">30</needle>
							<needle ref="needle2">10</needle>
						</until-stitches-remain-on-needles>
					</repeat-instruction>
				</directions>
			</pattern>'''
		def pattern = unmarshalXml(xml)
		def needle1 = pattern.supplies.needles[0]
		def needle2 = pattern.supplies.needles[1]
		
		def repeatInstruction = pattern.directions.operations[1]
		repeatInstruction.with {
			assertThat ref, is (pattern.directions.operations[0])
			assertThat until, is (RepeatInstruction.Until.UNTIL_STITCHES_REMAIN_ON_NEEDLES)
			assertThat value[0].needle, is (needle1)
			assertThat value[0].numberOfStitches, is (30)
			assertThat value[1].needle, is (needle2)
			assertThat value[1].numberOfStitches, is (10)
		}
		marshalXmlAndCompare(pattern,xml)
	}

	@Test
	void repeatInstructionUntilMeasures() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directions>
					<instruction id="thingy"/>
					<repeat-instruction ref="thingy">
						<until-measures unit="in">4</until-measures>
					</repeat-instruction>
				</directions>
			</pattern>'''
		def pattern = unmarshalXml(xml)
		
		def repeatInstruction = pattern.directions.operations[1]
		repeatInstruction.with {
			assertThat ref, is (pattern.directions.operations[0])
			assertThat until, is (RepeatInstruction.Until.UNTIL_MEASURES)
			assertThat (value instanceof Measure, is(true))
			assertThat value.unit, is (Units.INCH)
			assertThat Integer.valueOf(value.value), is (4)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void repeatInstructionUntilEquals() {
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
					<instruction id="thingy"/>
					<repeat-instruction ref="thingy">
						<until-equals>
                          <stitch-count>
                            <needle ref="needle1"/>
                          </stitch-count>
                          <value>50</value>
                        </until-equals>
					</repeat-instruction>
				</directions>
			</pattern>'''
		def pattern = unmarshalXml(xml)
		def needle1 = pattern.supplies.needles[0]
		
		def repeatInstruction = pattern.directions.operations[1]
		repeatInstruction.with {
			assertThat ref, is (pattern.directions.operations[0])
			assertThat until, is (RepeatInstruction.Until.UNTIL_EQUALS)
			assertThat (value instanceof List, is(true))
			assertThat (value[0] instanceof StitchCount, is(true))
			assertThat value[0].needles[0], is (needle1)
			assertThat value[1].value, is (50)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void section() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directions>
					<section>
						<row number="1"/>
					</section>
					<section>
						<row number="2"/>
						<row number="3"/>
					</section>
					<section reset-row-count="true">
						<row number="1"/>
						<row number="2"/>
					</section>
				</directions>
			</pattern>'''
		def pattern = unmarshalXml(xml)
		
		def sections = pattern.directions.operations
		assertThat sections[0].resetRowCount, is (false)
		assertThat sections[0].operations[0].numbers[0], is (1)
		assertThat sections[1].operations[0].numbers[0], is (2)
		assertThat sections[1].operations[1].numbers[0], is (3)
		assertThat sections[2].resetRowCount, is (true)
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void eachRow() {
		def xml = '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directions>
					<instruction-group id="ig1">
						<instruction id="instruction1"/>
						<instruction id="combined-instruction">
							<for-each-row-in-instruction ref="instruction1">
								<knit/>
								<purl/>
							</for-each-row-in-instruction>
						</instruction>
					</instruction-group>
				</directions>
			</pattern>
		'''
		Pattern pattern = unmarshalXml(xml)
		assertNotNull pattern.directions
		def instruction1 = pattern.directions.operations[0].operations[0]
		def combinedInstruction = pattern.directions.operations[0].operations[1]
		def eachRowInInstruction = combinedInstruction.forEachRowInInstruction
		eachRowInInstruction.with {
			assertThat ref, is (instruction1)
			assertThat ((operations[0] instanceof Knit), is (true))
			assertThat ((operations[1] instanceof Purl), is (true))
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	static void main(args) {
		JUnitCore.main(RowsAndInstructionsTests.name)
	}
	
}
