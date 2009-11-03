package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.JUnitCore

import com.knitml.core.model.header.Yarn
import com.knitml.core.model.directions.block.Row
import com.knitml.core.model.directions.block.Instruction

import test.support.AbstractRenderingContextTests

class RepeatHandlerTests extends AbstractRenderingContextTests {
	
	private static final String LINE_BREAK = System.getProperty("line.separator")
	
	@Before
	void castOnStitches() {
		renderingContext.engine.castOn 60
	}
	
	@Test
	void simpleRepeat() {
		processXml '''
			<row xmlns="http://www.knitml.com/schema/pattern" number="1">
				<knit>5</knit>
				<repeat until="end">
					<knit />
				</repeat>
			</row>''', Row
		assertThat output.trim(), is ('Row 1: k5, k to end')
	}
	
	@Test
	void nestedRepeat() {
		processXml '''
			<row xmlns="http://www.knitml.com/schema/pattern" number="1">
				<repeat value="4" until="times">
					<decrease type="k2tog" />
					<repeat until="times" value="10">
						<knit />
					</repeat>
				</repeat>
				<repeat until="end">
					<knit />
				</repeat>
			</row>''', Row
		assertThat output.trim(), is ('Row 1: repeat [k2tog, k 10 times] 4 times, k to end')
	}
	
	@Test
	void repeatToEndWithNoDisplay() {
		processXml '''
			<row xmlns="http://www.knitml.com/schema/pattern" number="1">
				<knit>60</knit>
				<repeat until="end">
					<knit />
				</repeat>
			</row>''', Row
		assertThat output.trim(), is ('Row 1: k60')
	}
	
	static void main(args) {
		JUnitCore.main(RepeatHandlerTests.name)
	}
	
}
