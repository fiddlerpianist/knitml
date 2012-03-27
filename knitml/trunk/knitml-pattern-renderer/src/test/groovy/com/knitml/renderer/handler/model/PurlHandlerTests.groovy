package com.knitml.renderer.handler.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.operations.inline.Purl;

import test.support.AbstractRenderingContextTests

class PurlHandlerTests extends AbstractRenderingContextTests {

	@Before
	void addYarns() {
		Yarn yarnOne = new Yarn('yarn1', 'A')
		Yarn yarnTwo = new Yarn('yarn2', 'B')
		renderingContext.patternRepository.addYarn(yarnOne)
		renderingContext.patternRepository.addYarn(yarnTwo)
		renderingContext.with {
			engine.castOn 2
		}
	}
	
	@Test
	void purl() {
		renderingContext.engine.startNewRow()
		processXml '<purl xmlns="http://www.knitml.com/schema/operations"/>', Purl
		assertThat output, is ('p1')
	}

	@Test
	void purlOne() {
		renderingContext.engine.startNewRow()
		processXml '<purl xmlns="http://www.knitml.com/schema/operations">1</purl>', Purl
		assertThat output, is ('p1')
	}
	
	@Test
	void purlOnly() {
		processXml '''
		<row number="1" xmlns="http://www.knitml.com/schema/operations">
			<repeat until="end">
				<purl/>
			</repeat>
		</row>
		''', Purl
		assertThat output, startsWith ('Row 1: Purl')
	}
	
	@Test
	void purlWithYarnOne() {
		renderingContext.engine.startNewRow()
		processXml '<purl yarn-ref="yarn1" xmlns="http://www.knitml.com/schema/operations">1</purl>', Purl
		assertThat output, is ('p1 (A)')
	}

	@Test
	void purlWithYarnsOneAndTwo() {
		processXml '''
		<row number="1" xmlns="http://www.knitml.com/schema/operations">
			<purl yarn-ref="yarn1">1</purl>
			<purl yarn-ref="yarn2">1</purl>
		</row>
		''', Purl
		assertThat output, startsWith ('Row 1: p1 (A), p1 (B)')
	}
	
}
