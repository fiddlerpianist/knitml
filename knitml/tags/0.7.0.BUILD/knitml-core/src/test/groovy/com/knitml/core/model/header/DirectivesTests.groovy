/**
 * 
 */
package com.knitml.core.model.header

import static com.knitml.core.common.KnittingShape.FLAT
import static com.knitml.core.common.KnittingShape.ROUND
import static com.knitml.core.common.MergePoint.ROW
import static com.knitml.core.common.MergeType.PHYSICAL
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.instanceOf
import static org.junit.Assert.assertThat
import static test.support.JiBXTestUtils.marshalXmlAndCompare
import static test.support.JiBXTestUtils.unmarshalXml

import org.custommonkey.xmlunit.XMLUnit
import org.jibx.runtime.JiBXException
import org.junit.BeforeClass
import org.junit.Test

import com.knitml.core.model.pattern.AssignInstruction
import com.knitml.core.model.pattern.AssignYarn
import com.knitml.core.model.pattern.Pattern

class DirectivesTests {
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
	}
	
	@Test
	void mergedInstruction() {
		def xml = '''
		<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			<pattern:directives>
				<pattern:instruction-definitions>
					<instruction id="thingy1" shape="flat"/>
					<instruction id="thingy2" shape="round"/>
					<instruction id="thingy3"/>
					<merged-instruction id="thingy12" merge-point="row" type="physical">
						<instruction-ref ref="thingy1"/>
						<instruction-ref ref="thingy2"/>
					</merged-instruction>
				</pattern:instruction-definitions>
			</pattern:directives>
		</pattern:pattern>
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
		<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
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
		</pattern:pattern>
		'''
		unmarshalXml(xml)
	}
	
	@Test
	void messageSources() {
		def xml = '''
		<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			<pattern:directives>
				<pattern:message-sources>
					<pattern:message-source>thing1</pattern:message-source>
					<pattern:message-source>thing2</pattern:message-source>
				</pattern:message-sources>
			</pattern:directives>
		</pattern:pattern>
		'''
		Pattern pattern = unmarshalXml(xml)
		assertThat pattern.directives.messageSources[0], is ('thing1')
		assertThat pattern.directives.messageSources[1], is ('thing2')
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void imports() {
		def xml = '''
		<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			<pattern:directives>
				<pattern:imports>
					<pattern:import location="file1">
						<pattern:assign-yarn from="dark" to="dark1" />
						<pattern:assign-instruction from="stockinette" to="stockinette-local" />
					</pattern:import>
				</pattern:imports>
			</pattern:directives>
		</pattern:pattern>
		'''
		Pattern pattern = unmarshalXml(xml)
		assertThat pattern.directives.imports.size(), is (1)
		pattern.directives.imports[0].with {
			assertThat location, is ('file1')
			assertThat assignments.size(), is (2)
			assertThat assignments[0], instanceOf (AssignYarn)
			assertThat assignments[0].from, is ('dark')
			assertThat assignments[0].to, is ('dark1')
			assertThat assignments[1], instanceOf (AssignInstruction)
			assertThat assignments[1].from, is ('stockinette')
			assertThat assignments[1].to, is ('stockinette-local')
		}
		marshalXmlAndCompare(pattern,xml)
	}

}
