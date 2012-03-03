package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.hamcrest.text.StringEndsWith.endsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.operations.block.Row;
import com.knitml.core.common.ValidationException

import test.support.AbstractRenderingContextTests

class ForEachRowInInstructionVisitorTests extends AbstractRenderingContextTests {

	@Test
	void twoRowsFromLocalInstruction() {
		processXml PATTERN_START_TAG + '''
				<pattern:directions>
					<pattern:instruction-group id="ig1">
						<cast-on>1</cast-on>
						<instruction id="instruction1">
							<row number="1"><knit/></row>
							<row number="2"><purl/></row>
						</instruction>
						<instruction id="instruction2">
							<for-each-row-in-instruction ref="instruction1">
								<repeat until="end"><purl/></repeat>
							</for-each-row-in-instruction>
						</instruction>
						<repeat-instruction ref="instruction2">
							<additional-times>2</additional-times>
						</repeat-instruction>
					</pattern:instruction-group>
				</pattern:directions>
			</pattern:pattern>
			'''
		assertThat output.trim(), endsWith ('Rows 3,4: Purl' + LINE_SEPARATOR +
				                            'Repeat rows 3-4 2 additional times.');
	}

	@Test
	void twoRowsFromGlobalInstruction() {
		processXml PATTERN_START_TAG + '''
				<pattern:directions>
					<pattern:instruction-group id="ig1">
						<cast-on>1</cast-on>
						<instruction id="instruction1" label="Sunny Day Stitch">
							<row number="1"><knit/></row>
							<row number="2"><purl/></row>
						</instruction>
						<row number="1"><knit/></row>
						<instruction id="instruction2">
							<for-each-row-in-instruction ref="instruction1">
								<repeat until="end"><purl/></repeat>
							</for-each-row-in-instruction>
						</instruction>
						<repeat-instruction ref="instruction2">
							<additional-times>2</additional-times>
						</repeat-instruction>
					</pattern:instruction-group>
				</pattern:directions>
			</pattern:pattern>
			'''
		assertThat output.trim(), endsWith ('Rows 2,3: Purl' + LINE_SEPARATOR +
				                            'Repeat rows 2-3 2 additional times.');
	}
	
	@Test(expected=ValidationException)
	void lookupInstructionOutOfScope() {
		processXml PATTERN_START_TAG + '''
				<pattern:directions>
					<pattern:instruction-group id="ig1">
						<cast-on>1</cast-on>
						<instruction id="instruction1">
							<row number="1"><knit/></row>
							<row number="2"><purl/></row>
						</instruction>
						<row number="1"><knit/></row>
						<instruction id="instruction2">
							<for-each-row-in-instruction ref="instruction1">
								<repeat until="end"><purl/></repeat>
							</for-each-row-in-instruction>
						</instruction>
					</pattern:instruction-group>
				</pattern:directions>
			</pattern:pattern>
			'''
	}
	
}
 