package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.header.Yarn
import com.knitml.core.model.directions.inline.BindOffAll
import com.knitml.core.model.directions.inline.BindOff

import test.support.AbstractRenderingContextTests

class BindOffVisitorTests extends AbstractRenderingContextTests {


	@Before
	void setUpContext() {
		Yarn yarnOne = new Yarn('yarn1', 'A')
		renderingContext.with {
			patternRepository.addYarn(yarnOne)
			engine.castOn 5
			engine.startNewRow()
		}
	}
	
	@Test
	void bindOffAll() {
		processXml '<bind-off-all xmlns="http://www.knitml.com/schema/pattern"/>', BindOffAll
		assertThat output, is ('bind off all stitches')
	}
	@Test
	void bindOffAllKnitwise() {
		processXml '<bind-off-all xmlns="http://www.knitml.com/schema/pattern" type="knitwise"/>', BindOffAll
		assertThat output, is ('bind off all stitches knitwise')
	}
	@Test
	void bindOffAllPurlwise() {
		processXml '<bind-off-all xmlns="http://www.knitml.com/schema/pattern" type="purlwise"/>', BindOffAll
		assertThat output, is ('bind off all stitches purlwise')
	}
	@Test
	void bindOffAllWithYarn() {
		processXml '<bind-off-all xmlns="http://www.knitml.com/schema/pattern" yarn-ref="yarn1"/>', BindOffAll
		assertThat output, is ("with 'A', bind off all stitches")
	}
	@Test
	void bindOffAllPurlwiseWithYarn() {
		processXml '<bind-off-all xmlns="http://www.knitml.com/schema/pattern" yarn-ref="yarn1" type="purlwise"/>', BindOffAll
		assertThat output, is ("with 'A', bind off all stitches purlwise")
	}
	@Test
	void bindOff5() {
		processXml '<bind-off xmlns="http://www.knitml.com/schema/pattern">5</bind-off>', BindOff
		assertThat output, is ('bind off 5 stitches')
	}
	@Test
	void bindOff5Knitwise() {
		processXml '<bind-off xmlns="http://www.knitml.com/schema/pattern" type="knitwise">5</bind-off>', BindOff
		assertThat output, is ('bind off 5 stitches knitwise')
	}
	@Test
	void bindOff5Purlwise() {
		processXml '<bind-off xmlns="http://www.knitml.com/schema/pattern" type="purlwise">5</bind-off>', BindOff
		assertThat output, is ('bind off 5 stitches purlwise')
	}
	@Test
	void bindOff5WithYarn() {
		processXml '<bind-off xmlns="http://www.knitml.com/schema/pattern" yarn-ref="yarn1">5</bind-off>', BindOff
		assertThat output, is ("with 'A', bind off 5 stitches")
	}
	@Test
	void bindOff5PurlwiseWithYarn() {
		processXml '<bind-off xmlns="http://www.knitml.com/schema/pattern" type="purlwise" yarn-ref="yarn1">5</bind-off>', BindOff
		assertThat output, is ("with 'A', bind off 5 stitches purlwise")
	}

	static void main(args) {
		JUnitCore.main(BindOffVisitorTests.name)
	}
	
}
