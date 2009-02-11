package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.header.Yarn
import com.knitml.core.model.directions.block.Row
import com.knitml.core.model.directions.inline.CrossStitches

import test.support.AbstractRenderingContextTests

class CrossStitchesVisitorTests extends AbstractRenderingContextTests {

	
	@Test
	void crossInFront() {
		processXml '<cross-stitches xmlns="http://www.knitml.com/schema/pattern" first="2" next="2" type="front"/>', CrossStitches
		assertThat output, is ('cross next 2 stitches in front of following 2 stitches')
	}

	@Test
	void crossInBack() {
		processXml '<cross-stitches xmlns="http://www.knitml.com/schema/pattern" first="2" next="2" type="back"/>', CrossStitches
		assertThat output, is ('cross next 2 stitches behind following 2 stitches')
	}
	
	static void main(args) {
		JUnitCore.main(CrossStitchesVisitorTests.name)
	}
	
}
