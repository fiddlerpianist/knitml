package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.header.Yarn
import com.knitml.core.model.directions.block.Row
import com.knitml.core.model.directions.inline.Knit

import test.support.AbstractRenderingContextTests

class KnitVisitorTests extends AbstractRenderingContextTests {

	@Before
	void addYarns() {
		Yarn yarnOne = new Yarn('yarn1', 'A')
		Yarn yarnTwo = new Yarn('yarn2', 'B')
		renderingContext.patternRepository.addYarn(yarnOne)
		renderingContext.patternRepository.addYarn(yarnTwo)
	}
	
	@Test
	void knit() {
		processXml '<knit xmlns="http://www.knitml.com/schema/pattern"/>', Knit
		assertThat output, is ('k1')
	}

	@Test
	void knitOne() {
		processXml '<knit xmlns="http://www.knitml.com/schema/pattern">1</knit>', Knit
		assertThat output, is ('k1')
	}
	
	@Test
	void knitOnly() {
		processXml '''
		<row number="1" xmlns="http://www.knitml.com/schema/pattern">
			<repeat until="end">
				<knit/>
			</repeat>
		</row>
		''', Row
		assertThat output, startsWith ('Row 1: Knit')
	}
	
	@Test
	void knitWithYarnOne() {
		processXml '<knit yarn-ref="yarn1" xmlns="http://www.knitml.com/schema/pattern">1</knit>', Knit
		assertThat output, is ('k1 (A)')
	}

	@Test
	void knitWithYarnsOneAndTwo() {
		processXml '''
		<row number="1" xmlns="http://www.knitml.com/schema/pattern">
			<knit yarn-ref="yarn1">1</knit>
			<knit yarn-ref="yarn2">1</knit>
		</row>
		''', Row
		assertThat output, startsWith ('Row 1: k1 (A), k1 (B)')
	}
	
	static void main(args) {
		JUnitCore.main(KnitVisitorTests.name)
	}
	
}
