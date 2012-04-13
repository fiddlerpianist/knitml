package com.knitml.renderer.handler.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.operations.block.Row;
import com.knitml.core.model.operations.inline.Knit;

import test.support.AbstractRenderingContextTests

class KnitHandlerTests extends AbstractRenderingContextTests {

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
	void knit() {
		renderingContext.engine.startNewRow()
		processXml '<knit xmlns="http://www.knitml.com/schema/operations"/>', Knit
		assertThat output, is ('k1')
	}

	@Test
	void knitOne() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<knit xmlns="http://www.knitml.com/schema/operations">1</knit>', Knit
		assertThat output, is ('k1')
	}
	
	@Test
	void knitOnly() {
		renderingContext.engine.castOn 5
		processXml '''
		<row number="1" xmlns="http://www.knitml.com/schema/operations">
			<repeat until="end">
				<knit/>
			</repeat>
		</row>
		''', Row
		assertThat output, startsWith ('Row 1: Knit')
	}
	
	@Test
	void knitWithYarnOne() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<knit yarn-ref="yarn1" xmlns="http://www.knitml.com/schema/operations">1</knit>', Knit
		assertThat output, is ('k1 (A)')
	}

	@Test
	void knitWithYarnsOneAndTwo() {
		processXml '''
		<row number="1" xmlns="http://www.knitml.com/schema/operations">
			<knit yarn-ref="yarn1">1</knit>
			<knit yarn-ref="yarn2">1</knit>
		</row>
		''', Row
		assertThat output, startsWith ('Row 1: k1 (A), k1 (B)')
	}
	
	@Test
	void knitIntoRowBelow() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<knit rows-below="1" xmlns="http://www.knitml.com/schema/operations"/>', Knit
		assertThat output, is ('k into st below')
	}
	@Test
	void knitInto2RowsBelow() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<knit rows-below="2" xmlns="http://www.knitml.com/schema/operations"/>', Knit
		assertThat output, is ('k into st 2 rows below')
	}
	@Test
	void knit1IntoRowBelow() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<knit rows-below="1" xmlns="http://www.knitml.com/schema/operations"/>', Knit
		assertThat output, is ('k into st below')
	}
	@Test
	void knitTblIntoRowBelow() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<knit rows-below="1" loop-to-work="trailing" xmlns="http://www.knitml.com/schema/operations"/>', Knit
		assertThat output, is ('k tbl into st below')
	}
	@Test
	void knitWithYarnIntoRowBelow() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<knit rows-below="1" yarn-ref="yarn1" xmlns="http://www.knitml.com/schema/operations"/>', Knit
		assertThat output, is ('k into st below (A)')
	}
	@Test
	void knitTblWithYarnIntoRowBelow() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<knit rows-below="1" yarn-ref="yarn1" loop-to-work="trailing" xmlns="http://www.knitml.com/schema/operations"/>', Knit
		assertThat output, is ('k tbl into st below (A)')
	}
	@Test
	void knit1Into2RowsBelow() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<knit rows-below="2" xmlns="http://www.knitml.com/schema/operations"/>', Knit
		assertThat output, is ('k into st 2 rows below')
	}
	@Test
	void knit2IntoRowBelow() {
		renderingContext.engine.castOn 1
		renderingContext.engine.startNewRow()
		processXml '<knit rows-below="1" xmlns="http://www.knitml.com/schema/operations">2</knit>', Knit
		assertThat output, is ('k next 2 sts into sts below')
	}
}
