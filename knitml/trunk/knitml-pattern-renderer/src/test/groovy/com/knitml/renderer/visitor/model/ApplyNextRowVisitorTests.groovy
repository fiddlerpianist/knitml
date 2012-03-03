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

class ApplyNextRowVisitorTests extends AbstractRenderingContextTests {

	@Test
	void applyNextRowFromGlobalInstruction() {
		processXml PATTERN_START_TAG + '''
				<pattern:directions>
					<pattern:instruction-group id="ig1">
						<cast-on>1</cast-on>
						<instruction id="instruction1" label="Sunny Day Stitch">
							<row number="1"><knit/></row>
							<row number="2"><purl/></row>
						</instruction>
						<row number="1"><knit/><increase/></row>
						<row number="2">
							<apply-next-row instruction-ref="instruction1"/>
							<knit>1</knit>
						</row>
					</pattern:instruction-group>
				</pattern:directions>
			</pattern:pattern>
			'''
		assertThat output.trim(), endsWith ('Row 2: work next row from Sunny Day Stitch instruction, k1')
	}

	@Test(expected=ValidationException)
	void applyNextRowFromLocalInstruction() {
		processXml PATTERN_START_TAG + '''
				<pattern:directions>
					<pattern:instruction-group id="ig1">
						<cast-on>1</cast-on>
						<instruction id="instruction1">
							<row number="1"><knit/></row>
							<row number="2"><purl/></row>
						</instruction>
						<row number="1">
							<apply-next-row instruction-ref="instruction1"/>
						</row>
					</pattern:instruction-group>
				</pattern:directions>
			</pattern:pattern>
			'''
	}
	
}
 