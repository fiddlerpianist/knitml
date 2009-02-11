package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.runner.JUnitCore

import test.support.AbstractRenderingContextTests
import com.knitml.core.model.directions.inline.Slip

class SlipVisitorTests extends AbstractRenderingContextTests {

	@Test
	void slip() {
		processXml '<slip xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1')
	}

	
	@Test
	void slip1() {
		processXml '<slip xmlns="http://www.knitml.com/schema/pattern">1</slip>', Slip
		assertThat output, is ('sl 1')
	}

	@Test
	void slip3() {
		processXml '<slip xmlns="http://www.knitml.com/schema/pattern">3</slip>', Slip
		assertThat output, is ('sl 3')
	}
	
	@Test
	void slipKnitwise() {
		processXml '<slip type="knitwise" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 knitwise')
	}
	
	@Test
	void slipKnitwiseWithYarnInBack() {
		processXml '<slip type="knitwise" yarn-position="back" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 knitwise wyib')
	}

	@Test
	void slipKnitwiseWithYarnInFront() {
		processXml '<slip type="knitwise" yarn-position="front" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 knitwise wyif')
	}

	@Test
	void slipPurlwise() {
		processXml '<slip type="purlwise" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 purlwise')
	}
	
	@Test
	void slipPurlwiseWithYarnInBack() {
		processXml '<slip type="purlwise" yarn-position="back" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 purlwise wyib')
	}
	
	@Test
	void slipPurlwiseWithYarnInFront() {
		processXml '<slip type="purlwise" yarn-position="front" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 purlwise wyif')
	}
	
	@Test
	void slipYarnInBack() {
		processXml '<slip yarn-position="back" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 wyib')
	}
	
	@Test
	void slipYarnInFront() {
		processXml '<slip yarn-position="front" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 wyif')
	}
	
	static void main(args) {
		JUnitCore.main(SlipVisitorTests.name)
	}
	
}
