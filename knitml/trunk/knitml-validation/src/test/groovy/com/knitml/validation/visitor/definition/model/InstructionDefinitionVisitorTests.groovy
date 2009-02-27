package com.knitml.validation.visitor.definition.model

import java.util.ArrayList
import java.util.List

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertThat

import org.junit.Test

import test.support.AbstractKnittingContextTests

import com.knitml.core.common.KnittingShape
import com.knitml.validation.common.InvalidStructureException
import com.knitml.engine.common.UnexpectedRowNumberException
import com.knitml.engine.common.KnittingEngineException
import com.knitml.engine.KnittingEngine
import com.knitml.engine.Needle

public class InstructionDefinitionVisitorTests extends AbstractKnittingContextTests {
	
	@Test
	void examineAssignedRowNumbersInHeaderInstruction() {
		def pattern = processXml ('''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directives>
					<instruction-definitions>
						<instruction id="knit-purl" label="Knit / Purl Pattern" shape="flat">
							<row><knit>5</knit></row>
							<row><purl>15</purl></row>
						</instruction>
					</instruction-definitions>
				</directives>
				<directions/>
			</pattern>
			''')
		def instruction = pattern.directives.instructionDefinitions[0]
		assertThat instruction.rows[0].numbers, not (null)
		assertThat instruction.rows[0].numbers[0], is (1)
		assertThat instruction.rows[1].numbers, not (null)
		assertThat instruction.rows[1].numbers[0], is (2)
	}

	@Test(expected=InvalidStructureException)
	void useInvalidGlobalInstruction() {
		processXml '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directives>
					<instruction-definitions>
						<instruction id="knit-purl" shape="flat"/>
					</instruction-definitions>
				</directives>
				<directions/>
			</pattern>
			'''
	}

	@Test
	void useGlobalMergedInstruction() {
		processXml '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directives>
					<instruction-definitions>
						<instruction id="knit-purl" label="Knit / purl" shape="flat">
							<row/>
						</instruction>
						<instruction id="purl-knit" label="Purl / knit" shape="flat">
							<row/>
						</instruction>
						<merged-instruction id="merged1" merge-point="end" type="physical" label="Merged Instruction">
							<instruction-ref ref="knit-purl"/>
							<instruction-ref ref="purl-knit"/>
						</merged-instruction>
					</instruction-definitions>
				</directives>
				<directions/>
			</pattern>
			'''
	}

	@Test(expected=InvalidStructureException)
	void useInvalidGlobalMergedInstruction() {
		processXml '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directives>
					<instruction-definitions>
						<instruction id="knit-purl" label="Knit / purl" shape="flat">
							<row/>
						</instruction>
						<instruction id="purl-knit" label="Purl / knit" shape="flat">
							<row/>
						</instruction>
						<merged-instruction id="merged1" merge-point="end" type="physical">
							<instruction-ref ref="knit-purl"/>
							<instruction-ref ref="purl-knit"/>
						</merged-instruction>
					</instruction-definitions>
				</directives>
				<directions/>
			</pattern>
			'''
	}

	@Test(expected=UnexpectedRowNumberException)
	void useInvalidRowNumbersInHeaderInstruction() {
		def pattern = processXml ('''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directives>
					<instruction-definitions>
						<instruction id="knit-purl" label="Test Pattern" shape="flat">
							<row number="2"><knit>5</knit></row>
							<row number="3"><knit>5</knit></row>
						</instruction>
					</instruction-definitions>
				</directives>
				<directions/>
			</pattern>
			''')
	}

	@Test(expected=InvalidStructureException)
	void instructionDefinitionWithNoShapeAttribute() {
		processXml '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directives>
					<instruction-definitions>
						<instruction id="knit-purl" label="Knit / purl">
							<row/>
						</instruction>
					</instruction-definitions>
				</directives>
				<directions/>
			</pattern>
			'''
	}

	@Test(expected=InvalidStructureException)
	void mergedInstructionWithTwoDifferentShapes() {
		processXml '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directives>
					<instruction-definitions>
						<instruction id="knit-purl" label="Knit / purl" shape="flat">
							<row/>
						</instruction>
						<instruction id="purl-knit" label="Purl / knit" shape="round">
							<row/>
						</instruction>
						<merged-instruction id="merged1" merge-point="end" type="physical" label="Merged Instruction">
							<instruction-ref ref="knit-purl"/>
							<instruction-ref ref="purl-knit"/>
						</merged-instruction>
					</instruction-definitions>
				</directives>
				<directions/>
			</pattern>
			'''
	}
}
