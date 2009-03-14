/**
 * 
 */
package com.knitml.core.model.header

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static com.knitml.core.common.MergeType.PHYSICAL
import static com.knitml.core.common.MergePoint.ROW
import static com.knitml.core.common.KnittingShape.FLAT
import static com.knitml.core.common.KnittingShape.ROUND
import static test.support.JiBXTestUtils.unmarshalXml
import static test.support.JiBXTestUtils.marshalXmlAndCompare

import javax.measure.Measure

import org.custommonkey.xmlunit.XMLUnit

import org.jibx.runtime.JiBXException
import org.junit.Test
import org.junit.Ignore
import org.junit.BeforeClass
import org.junit.internal.runners.JUnit4ClassRunner
import org.junit.runner.JUnitCore
import org.junit.runner.RunWith

import com.knitml.core.units.Units
import com.knitml.core.model.Pattern

@RunWith(JUnit4ClassRunner)
class DirectivesTests {
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
	}
	
	@Test
	void mergedInstruction() {
		def xml = '''
		<pattern xmlns="http://www.knitml.com/schema/pattern">
			<directives>
				<instruction-definitions>
					<instruction id="thingy1" shape="flat"/>
					<instruction id="thingy2" shape="round"/>
					<instruction id="thingy3"/>
					<merged-instruction id="thingy12" merge-point="row" type="physical">
						<instruction-ref ref="thingy1"/>
						<instruction-ref ref="thingy2"/>
					</merged-instruction>
				</instruction-definitions>
			</directives>
		</pattern>
		'''
		Pattern pattern = unmarshalXml(xml)
		def instruction1 = pattern.directives.instructionDefinitions[0]
		def instruction2 = pattern.directives.instructionDefinitions[1]
		def instruction3 = pattern.directives.instructionDefinitions[2]
		assertThat instruction1.knittingShape, is (FLAT)
		assertThat instruction2.knittingShape, is (ROUND)
		assertThat instruction3.knittingShape, is (null)
		
		def mergedInstruction = pattern.directives.instructionDefinitions[3]
		mergedInstruction.with {
			assertThat id, is ('thingy12')
			assertThat mergePoint, is (ROW)
			assertThat type, is (PHYSICAL)
			assertThat instructions[0].ref, is (instruction1)
			assertThat instructions[1].ref, is (instruction2)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test(expected=JiBXException)
	void invalidMergedInstruction() {
		// invalid because required attributes are missing
		def xml = '''
		<pattern xmlns="http://www.knitml.com/schema/pattern">
			<directives>
				<instruction-definitions>
					<instruction id="thingy1"/>
					<instruction id="thingy2"/>
					<merged-instruction id="thingy12">
						<instruction-ref ref="thingy1"/>
						<instruction-ref ref="thingy2"/>
					</merged-instruction>
				</instruction-definitions>
			</directives>
		</pattern>
		'''
		unmarshalXml(xml)
	}
	
	@Test
	void messageSources() {
		def xml = '''
		<pattern xmlns="http://www.knitml.com/schema/pattern">
			<directives>
				<message-sources>
					<message-source>thing1</message-source>
					<message-source>thing2</message-source>
				</message-sources>
			</directives>
		</pattern>
		'''
		Pattern pattern = unmarshalXml(xml)
		assertThat pattern.directives.messageSources[0], is ('thing1')
		assertThat pattern.directives.messageSources[1], is ('thing2')
		marshalXmlAndCompare(pattern,xml)
	}
	

	static void main(args) {
		JUnitCore.main(DirectivesTests.name)
	}
	
}
