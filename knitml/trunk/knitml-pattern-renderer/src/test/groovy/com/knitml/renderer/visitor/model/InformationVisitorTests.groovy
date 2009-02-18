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
		processXml '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
				<directions>
					<cast-on>5</cast-on>
					<row xmlns="http://www.knitml.com/schema/pattern" number="1">
						<knit>5</knit>
						<followup-information>
							<number-of-stitches number="5" inform="true"/>
						</followup-information>
					</row>
				</directions>
			</pattern>'''
		assertThat output.trim(), endsWith ('5 stitches in row.')
	}

	@Test
	void numberOfStitchesOutsideOfRow() {
		processXml '''
		<section xmlns="http://www.knitml.com/schema/pattern">
			<information>
				<number-of-stitches number="5"/>
			</information>
		</section>''', Section
		assertThat output.trim(), is ('5 stitches in row.')
	}
	
	static void main(args) {
		JUnitCore.main(InformationVisitorTests.name)
	}
	
}
