package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.directions.inline.Decrease
import com.knitml.core.model.directions.inline.DoubleDecrease

import test.support.AbstractRenderingContextTests

class DecreaseHandlerTests extends AbstractRenderingContextTests {

	@Before
	void setup() {
		renderingContext.engine.castOn 3
	}
	
	@Test
	void k2tog() {
		renderingContext.engine.startNewRow()
		processXml '<decrease type="k2tog" xmlns="http://www.knitml.com/schema/pattern"/>', Decrease
		assertThat output, is ('k2tog')
	}
	
	@Test
	void sssk() {
		renderingContext.engine.startNewRow()
		processXml '<double-decrease type="sssk" xmlns="http://www.knitml.com/schema/pattern"/>', DoubleDecrease
		assertThat output, is ('sssk')
	}
	
	static void main(args) {
		JUnitCore.main(DecreaseHandlerTests.name)
	}
	
}
