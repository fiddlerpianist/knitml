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
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<slip xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1')
	}

	
	@Test
	void slip1() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<slip xmlns="http://www.knitml.com/schema/pattern">1</slip>', Slip
		assertThat output, is ('sl 1')
	}

	@Test
	void slip3() {
		renderingContext.engine.castOn 3
		renderingContext.engine.startNewRow()
		processXml '<slip xmlns="http://www.knitml.com/schema/pattern">3</slip>', Slip
		assertThat output, is ('sl 3')
	}

	@Test
	void reverseSlip() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		renderingContext.engine.knit 1
		processXml '<slip xmlns="http://www.knitml.com/schema/pattern" direction="reverse"/>', Slip
		assertThat output, is ('sl 1 from RH to LH needle')
	}

	@Test
	void reverseSlip1() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		renderingContext.engine.knit 1
		processXml '<slip xmlns="http://www.knitml.com/schema/pattern" direction="reverse">1</slip>', Slip
		assertThat output, is ('sl 1 from RH to LH needle')
	}

	@Test
	void reverseSlip3() {
		renderingContext.engine.castOn 3
		renderingContext.engine.startNewRow()
		renderingContext.engine.knit 3
		processXml '<slip xmlns="http://www.knitml.com/schema/pattern" direction="reverse">3</slip>', Slip
		assertThat output, is ('sl 3 from RH to LH needle')
	}

	@Test
	void slipKnitwise() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<slip type="knitwise" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 knitwise')
	}
	
	@Test
	void slipKnitwiseWithYarnInBack() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<slip type="knitwise" yarn-position="back" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 knitwise wyib')
	}

	@Test
	void slipKnitwiseWithYarnInFront() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<slip type="knitwise" yarn-position="front" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 knitwise wyif')
	}

	@Test
	void slipPurlwise() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<slip type="purlwise" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 purlwise')
	}
	
	@Test
	void slipPurlwiseWithYarnInBack() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<slip type="purlwise" yarn-position="back" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 purlwise wyib')
	}
	
	@Test
	void slipPurlwiseWithYarnInFront() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<slip type="purlwise" yarn-position="front" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 purlwise wyif')
	}
	
	@Test
	void slipYarnInBack() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<slip yarn-position="back" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 wyib')
	}
	
	@Test
	void slipYarnInFront() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<slip yarn-position="front" xmlns="http://www.knitml.com/schema/pattern"/>', Slip
		assertThat output, is ('sl 1 wyif')
	}
	
	static void main(args) {
		JUnitCore.main(SlipVisitorTests.name)
	}
	
}
