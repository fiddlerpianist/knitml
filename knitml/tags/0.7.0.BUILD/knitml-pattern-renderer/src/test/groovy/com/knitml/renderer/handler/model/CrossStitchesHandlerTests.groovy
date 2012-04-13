package com.knitml.renderer.handler.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.operations.block.Row;
import com.knitml.core.model.operations.inline.CrossStitches;

import test.support.AbstractRenderingContextTests

class CrossStitchesHandlerTests extends AbstractRenderingContextTests {

	@Before
	void setUpContext() {
		renderingContext.with {
			engine.castOn 5
			engine.startNewRow()
		}
	}
	
	@Test
	void crossInFront() {
		processXml '<cross-stitches xmlns="http://www.knitml.com/schema/operations" first="2" next="2" type="front"/>', CrossStitches
		assertThat output, is ('cross next 2 stitches in front of following 2 stitches')
	}

	@Test
	void crossInBack() {
		processXml '<cross-stitches xmlns="http://www.knitml.com/schema/operations" first="2" next="2" type="back"/>', CrossStitches
		assertThat output, is ('cross next 2 stitches behind following 2 stitches')
	}
	
}
