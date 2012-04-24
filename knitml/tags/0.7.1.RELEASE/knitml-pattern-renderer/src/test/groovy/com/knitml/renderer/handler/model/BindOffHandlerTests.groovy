package com.knitml.renderer.handler.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.operations.inline.BindOff;
import com.knitml.core.model.operations.inline.BindOffAll;

import test.support.AbstractRenderingContextTests

class BindOffHandlerTests extends AbstractRenderingContextTests {


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
		processXml '<bind-off-all xmlns="http://www.knitml.com/schema/operations"/>', BindOffAll
		assertThat output, is ('bind off all stitches')
	}
	@Test
	void bindOffAllKnitwise() {
		processXml '<bind-off-all xmlns="http://www.knitml.com/schema/operations" type="knitwise"/>', BindOffAll
		assertThat output, is ('bind off all stitches knitwise')
	}
	@Test
	void bindOffAllPurlwise() {
		processXml '<bind-off-all xmlns="http://www.knitml.com/schema/operations" type="purlwise"/>', BindOffAll
		assertThat output, is ('bind off all stitches purlwise')
	}
	@Test
	void bindOffAllWithYarn() {
		processXml '<bind-off-all xmlns="http://www.knitml.com/schema/operations" yarn-ref="yarn1"/>', BindOffAll
		assertThat output, is ("with 'A', bind off all stitches")
	}
	@Test
	void bindOffAllPurlwiseWithYarn() {
		processXml '<bind-off-all xmlns="http://www.knitml.com/schema/operations" yarn-ref="yarn1" type="purlwise"/>', BindOffAll
		assertThat output, is ("with 'A', bind off all stitches purlwise")
	}
	@Test
	void bindOff5() {
		processXml '<bind-off xmlns="http://www.knitml.com/schema/operations">5</bind-off>', BindOff
		assertThat output, is ('bind off 5 stitches')
	}
	@Test
	void bindOff5Knitwise() {
		processXml '<bind-off xmlns="http://www.knitml.com/schema/operations" type="knitwise">5</bind-off>', BindOff
		assertThat output, is ('bind off 5 stitches knitwise')
	}
	@Test
	void bindOff5Purlwise() {
		processXml '<bind-off xmlns="http://www.knitml.com/schema/operations" type="purlwise">5</bind-off>', BindOff
		assertThat output, is ('bind off 5 stitches purlwise')
	}
	@Test
	void bindOff5WithYarn() {
		processXml '<bind-off xmlns="http://www.knitml.com/schema/operations" yarn-ref="yarn1">5</bind-off>', BindOff
		assertThat output, is ("with 'A', bind off 5 stitches")
	}
	@Test
	void bindOff5PurlwiseWithYarn() {
		processXml '<bind-off xmlns="http://www.knitml.com/schema/operations" type="purlwise" yarn-ref="yarn1">5</bind-off>', BindOff
		assertThat output, is ("with 'A', bind off 5 stitches purlwise")
	}

}
