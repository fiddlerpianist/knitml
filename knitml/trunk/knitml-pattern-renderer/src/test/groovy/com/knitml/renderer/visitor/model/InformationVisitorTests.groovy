package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringEndsWith.endsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.directions.block.Row
import com.knitml.core.model.directions.block.Section

import test.support.AbstractRenderingContextTests

class InformationVisitorTests extends AbstractRenderingContextTests {

	@Test
	void numberOfStitchesAtRowEnd() {
		processXml PATTERN_START_TAG + '''
				<pattern:directions>
					<cast-on>5</cast-on>
					<row number="1">
						<knit>5</knit>
						<followup-information>
							<number-of-stitches number="5" inform="true"/>
						</followup-information>
					</row>
				</pattern:directions>
			</pattern:pattern>'''
		assertThat output.trim(), endsWith ('5 stitches in row.')
	}

	@Test
	void numberOfStitchesAtRowEndWithinInstruction() {
		processXml PATTERN_START_TAG + '''
				<pattern:directions>
					<cast-on>5</cast-on>
					<instruction id="inst-1">
					    <row number="1">
						    <knit>5</knit>
						    <followup-information>
							    <number-of-stitches number="5" inform="true"/>
						    </followup-information>
					    </row>
					</instruction>
				</pattern:directions>
			</pattern:pattern>'''
		assertThat output.trim(), endsWith ('5 stitches in row.')
	}
	
	@Test
	void messageAtRowEnd() {
		processXml PATTERN_START_TAG + '''
				<pattern:directions>
					<cast-on>5</cast-on>
					<row number="1">
						<knit>5</knit>
						<followup-information>
							<message label="This row is tricky."/>
						</followup-information>
					</row>
				</pattern:directions>
			</pattern:pattern>'''
		assertThat output.trim(), endsWith ('This row is tricky.')
	}

	@Test
	void messageAtRowEndWithinInstruction() {
		processXml PATTERN_START_TAG + '''
				<pattern:directions>
					<cast-on>5</cast-on>
					<instruction id="inst-1">
					    <row number="1">
						    <knit>5</knit>
						    <followup-information>
							    <message label="This row is tricky."/>
						    </followup-information>
					    </row>
					</instruction>
				</pattern:directions>
			</pattern:pattern>'''
		assertThat output.trim(), endsWith ('This row is tricky.')
	}
	
	@Test
	void numberOfStitchesOutsideOfRow() {
		renderingContext.engine.castOn 5
		processXml '''
		<pattern:section xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations">
			<information>
				<number-of-stitches number="5"/>
			</information>
		</pattern:section>''', Section
		assertThat output.trim(), is ('5 stitches in row.')
	}
	
}
