package com.knitml.renderer.handler.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Before
import org.junit.Test

import test.support.AbstractRenderingContextTests

import com.knitml.core.model.operations.block.Row
import com.knitml.core.model.operations.inline.Decrease
import com.knitml.core.model.operations.inline.DoubleDecrease
import com.knitml.core.model.operations.inline.MultipleDecrease

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
	void k9tog() {
		renderingContext.engine.castOn 6
		renderingContext.engine.startNewRow()
		processXml '<multiple-decrease stitches-into-one="9" lean="right" nature="knit" xmlns="http://www.knitml.com/schema/operations"/>', MultipleDecrease
		assertThat output, is ('k9tog')
	}
	
	@Test
	void sssk() {
		renderingContext.engine.startNewRow()
		processXml '<double-decrease type="sssk" xmlns="http://www.knitml.com/schema/operations"/>', DoubleDecrease
		assertThat output, is ('sssk')
	}
	
	@Test
	void s9k() {
		renderingContext.engine.castOn 6
		renderingContext.engine.startNewRow()
		processXml '<multiple-decrease stitches-into-one="9" lean="left" nature="knit" xmlns="http://www.knitml.com/schema/operations"/>', MultipleDecrease
		assertThat output, is ('s9k')
	}
	
	@Test
	void purl9StsIntoOne() {
		renderingContext.engine.castOn 6
		renderingContext.engine.startNewRow()
		processXml '<multiple-decrease stitches-into-one="9" nature="purl" xmlns="http://www.knitml.com/schema/operations"/>', MultipleDecrease
		assertThat output, is ('p 9 sts into 1')
	}
	
	@Test
	void centeredDec9StsIntoOne() {
		renderingContext.engine.castOn 6
		renderingContext.engine.startNewRow()
		processXml '<multiple-decrease stitches-into-one="9" lean="balanced" xmlns="http://www.knitml.com/schema/operations"/>', MultipleDecrease
		assertThat output, is ('centered dec 9 sts into 1')
	}
	
	@Test
	void dec9StsIntoOne() {
		renderingContext.engine.castOn 6
		renderingContext.engine.startNewRow()
		processXml '<multiple-decrease stitches-into-one="9" xmlns="http://www.knitml.com/schema/operations"/>', MultipleDecrease
		assertThat output, is ('dec 9 sts into 1')
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
