package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before

import com.knitml.core.model.directions.block.Row 
import com.knitml.core.model.directions.inline.Decrease
import com.knitml.core.model.directions.inline.DoubleDecrease
import com.knitml.core.model.directions.inline.PassPreviousStitchOver;

import test.support.AbstractRenderingContextTests

class DecreaseHandlerTests extends AbstractRenderingContextTests {

	@Before
	void setup() {
		renderingContext.engine.castOn 3
	}
	
	@Test
	void k2tog() {
		renderingContext.engine.startNewRow()
		processXml '<decrease type="k2tog" xmlns="http://www.knitml.com/schema/operations"/>', Decrease
		assertThat output, is ('k2tog')
	}
	
	@Test
	void sssk() {
		renderingContext.engine.startNewRow()
		processXml '<double-decrease type="sssk" xmlns="http://www.knitml.com/schema/operations"/>', DoubleDecrease
		assertThat output, is ('sssk')
	}
	
	@Test
	void passPreviousStitchOverOnce() {
		processXml '''
		<row xmlns="http://www.knitml.com/schema/operations">
		  <knit>3</knit>
		  <pass-previous-stitch-over/>
		</row>''', Row
		assertThat output.trim(), is ('Row 1: k3, pass prev st over')
	}

	@Test
	void passPreviousStitchOverTwice() {
		processXml '''
		<row xmlns="http://www.knitml.com/schema/operations">
		  <knit>3</knit>
		  <pass-previous-stitch-over>2</pass-previous-stitch-over>
		</row>''', Row
		assertThat output.trim(), is ('Row 1: k3, pass prev st over, pass prev st over')
	}
}
