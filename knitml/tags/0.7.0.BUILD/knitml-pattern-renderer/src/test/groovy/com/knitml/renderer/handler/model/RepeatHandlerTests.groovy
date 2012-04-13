package com.knitml.renderer.handler.model

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.JUnitCore

import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.operations.block.Instruction;
import com.knitml.core.model.operations.block.Row;

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
			<row xmlns="http://www.knitml.com/schema/operations" number="1">
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
			<row xmlns="http://www.knitml.com/schema/operations" number="1">
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
			<row xmlns="http://www.knitml.com/schema/operations" number="1">
				<knit>60</knit>
				<repeat until="end">
					<knit />
				</repeat>
			</row>''', Row
		assertThat output.trim(), is ('Row 1: k60')
	}
	
	@Test
	void increaseIntoNextStitchWithinRepeat() {
		processXml '''
		    <row xmlns="http://www.knitml.com/schema/operations">
		      <repeat until="end">
		      <increase-into-next-stitch>
		        <knit>1</knit>
		        <purl>1</purl>
		      </increase-into-next-stitch>
		      </repeat>
		    </row>''', Row
		assertThat output.trim(), is ('Row 1: repeat [inc into next st [k1, p1]] to end')
	}
	
	@Test
	void increaseIntoNextStitchWithinRepeatWithinInstruction() {
		processXml '''
		  <instruction id="inst-1" xmlns="http://www.knitml.com/schema/operations">
		    <row>
		      <repeat until="end">
		      <increase-into-next-stitch>
		        <knit>1</knit>
		        <purl>1</purl>
		      </increase-into-next-stitch>
		      </repeat>
		    </row>
		  </instruction>''', Instruction
		assertThat output.trim(), is ('Row 1: repeat [inc into next st [k1, p1]] to end')
	}
}
