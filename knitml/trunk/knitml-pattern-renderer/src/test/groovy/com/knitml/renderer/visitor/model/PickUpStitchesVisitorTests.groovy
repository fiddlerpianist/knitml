package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.header.Yarn
import com.knitml.core.model.directions.block.Row
import com.knitml.core.model.directions.inline.InlinePickUpStitches

import test.support.AbstractRenderingContextTests

class PickUpStitchesVisitorTests extends AbstractRenderingContextTests {

	@Before
	void addYarns() {
		Yarn yarnOne = new Yarn('yarn1', 'A')
		Yarn yarnTwo = new Yarn('yarn2', 'B')
		renderingContext.patternRepository.addYarn(yarnOne)
		renderingContext.patternRepository.addYarn(yarnTwo)
	}
	
	@Test
	void simplePickUpStitches() {
		processXml '<inline-pick-up-stitches xmlns="http://www.knitml.com/schema/pattern">10</inline-pick-up-stitches>', InlinePickUpStitches
		assertThat output, is ('pick up 10 stitches')
	}

	@Test
	void pickUpStitchesWithYarnRef() {
		processXml '<inline-pick-up-stitches xmlns="http://www.knitml.com/schema/pattern" yarn-ref="yarn1">10</inline-pick-up-stitches>', InlinePickUpStitches
		assertThat output, is ("with 'A', pick up 10 stitches")
	}

	@Test
	void pickUpStitchesWithYarnRefKnitwise() {
		processXml '<inline-pick-up-stitches xmlns="http://www.knitml.com/schema/pattern" yarn-ref="yarn1" type="knitwise">10</inline-pick-up-stitches>', InlinePickUpStitches
		assertThat output, is ("with 'A', pick up 10 stitches knitwise")
	}
	
	static void main(args) {
		JUnitCore.main(PickUpStitchesVisitorTests.name)
	}
	
}
