package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat

import org.junit.Before
import org.junit.Test

import test.support.AbstractRenderingContextTests

import com.knitml.core.model.common.Yarn
import com.knitml.core.model.operations.inline.InlinePickUpStitches

class PickUpStitchesVisitorTests extends AbstractRenderingContextTests {

	@Before
	void addYarns() {
		Yarn yarnOne = new Yarn('yarn1', 'A')
		Yarn yarnTwo = new Yarn('yarn2', 'B')
		renderingContext.patternRepository.addYarn(yarnOne)
		renderingContext.patternRepository.addYarn(yarnTwo)
		renderingContext.with {
			engine.castOn 5
			engine.startNewRow()
		}
	}
	
	@Test
	void simplePickUpStitches() {
		processXml '<inline-pick-up-stitches xmlns="http://www.knitml.com/schema/operations">10</inline-pick-up-stitches>', InlinePickUpStitches
		assertThat output, is ('pick up 10 stitches')
	}

	@Test
	void pickUpStitchesWithYarnRef() {
		processXml '<inline-pick-up-stitches xmlns="http://www.knitml.com/schema/operations" yarn-ref="yarn1">10</inline-pick-up-stitches>', InlinePickUpStitches
		assertThat output, is ("with 'A', pick up 10 stitches")
	}

	@Test
	void pickUpStitchesWithYarnRefKnitwise() {
		processXml '<inline-pick-up-stitches xmlns="http://www.knitml.com/schema/operations" yarn-ref="yarn1" type="knitwise">10</inline-pick-up-stitches>', InlinePickUpStitches
		assertThat output, is ("with 'A', pick up 10 stitches knitwise")
	}
	
}
