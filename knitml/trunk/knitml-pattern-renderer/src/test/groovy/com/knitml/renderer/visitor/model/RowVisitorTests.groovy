package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.JUnitCore

import com.knitml.core.model.header.Yarn
import com.knitml.core.model.directions.block.Row
import com.knitml.core.model.directions.block.Instruction

import test.support.AbstractRenderingContextTests

class RowVisitorTests extends AbstractRenderingContextTests {
	
	private static final String LINE_BREAK = System.getProperty("line.separator")
	
	@Before
	void addYarns() {
		Yarn yarnOne = new Yarn('yarn1', 'A')
		Yarn yarnTwo = new Yarn('yarn2', 'B')
		renderingContext.patternRepository.addYarn yarnOne
		renderingContext.patternRepository.addYarn yarnTwo
		renderingContext.engine.castOn 1
	}
	
	@Test
	void rowOne() {
		processXml '''
			<row xmlns="http://www.knitml.com/schema/pattern" number="1">
				<knit>1</knit>
			</row>''', Row
		assertThat output.trim(), is ('Row 1: k1')
	}
	
	@Test
	void rowOneTwoFive() {
		processXml '''
		<instruction id="blah" xmlns="http://www.knitml.com/schema/pattern">
			<row number="1 3 5"><knit>1</knit></row>
			<row number="2 4 6"><purl>1</purl></row>
		</instruction>''', Instruction
		assertThat output.trim(), is ('Rows 1,3,5: k1' + LINE_BREAK + 'Rows 2,4,6: p1')
	}
	
	@Test
	void nextRow() {
		processXml '<row assign-row-number="false" xmlns="http://www.knitml.com/schema/pattern"><knit>1</knit></row>', Row
		assertThat output.trim(), is ('Next row: k1')
	}
	
	@Test
	void roundOne() {
		renderingContext.engine.declareRoundKnitting()
		processXml '<row xmlns="http://www.knitml.com/schema/pattern" number="1" type="round"><knit>1</knit></row>', Row
		assertThat output.trim(), is ('Round 1: k1')
	}
	

	@Test
	void roundOneThreeFive() {
		renderingContext.engine.declareRoundKnitting()
		processXml '''
		<instruction id="blah" xmlns="http://www.knitml.com/schema/pattern">
			<row number="1 3 5" type="round"><knit>1</knit></row>
			<row number="2 4 6" type="round"><purl>1</purl></row>
		</instruction>''', Instruction
		assertThat output.trim(), is ('Rounds 1,3,5: k1' + LINE_BREAK + 'Rounds 2,4,6: p1')
	}
	
	@Test
	void nextRound() {
		renderingContext.engine.declareRoundKnitting()
		processXml '<row assign-row-number="false" xmlns="http://www.knitml.com/schema/pattern" type="round"><knit>1</knit></row>', Row
		assertThat output.trim(), is ('Next round: k1')
	}
	
	@Test 
	void rowOneWithA() {
		processXml '<row xmlns="http://www.knitml.com/schema/pattern" number="1" yarn-ref="yarn1"><knit>1</knit></row>', Row
		assertThat output.trim(), is ('Row 1 (A): k1')
	}
	
	@Test
	void rowOneThreeFiveWithAB() {
		processXml '''
		<instruction id="blah" xmlns="http://www.knitml.com/schema/pattern">
			<row number="1 3 5" yarn-ref="yarn1"><knit>1</knit></row>
			<row number="2 4 6" yarn-ref="yarn2"><purl>1</purl></row>
		</instruction>
		''', Instruction
		assertThat output.trim(), is ('Rows 1,3,5 (A): k1' + LINE_BREAK + 'Rows 2,4,6 (B): p1')
	}
	
	@Test
	void rowOneThroughFive() {
		processXml '''
		<instruction id="blah" xmlns="http://www.knitml.com/schema/pattern">
			<row number="1 2 3 4 5"><knit>1</knit></row>
		</instruction>
		''', Instruction
		assertThat output.trim(), is ('Rows 1-5: k1')
	}

	@Test
	void rowOneThroughFiveWithA() {
		processXml '''
		<instruction id="blah" xmlns="http://www.knitml.com/schema/pattern">
			<row number="1 2 3 4 5" yarn-ref="yarn1"><knit>1</knit></row>
		</instruction>
		''', Instruction
		assertThat output.trim(), is ('Rows 1-5 (A): k1')
	}
	
	@Test
	void nextRowWithA() {
		processXml '<row assign-row-number="false" xmlns="http://www.knitml.com/schema/pattern" yarn-ref="yarn1"><knit>1</knit></row>', Row
		assertThat output.trim(), is ('Next row (A): k1')
	}
	
	@Test
	void roundOneWithA() {
		renderingContext.engine.declareRoundKnitting()
		processXml '<row xmlns="http://www.knitml.com/schema/pattern" number="1" type="round" yarn-ref="yarn1"><knit>1</knit></row>', Row
		assertThat output.trim(), is ('Round 1 (A): k1')
	}
	
	@Test
	void roundOneThreeFiveWithAB() {
		renderingContext.engine.declareRoundKnitting()
		processXml '''
		<instruction id="blah" xmlns="http://www.knitml.com/schema/pattern">
			<row number="1 3 5" type="round" yarn-ref="yarn1"><knit>1</knit></row>
			<row number="2 4 6" type="round" yarn-ref="yarn2"><purl>1</purl></row>
		</instruction>
		''', Instruction
		assertThat output.trim(), is ('Rounds 1,3,5 (A): k1' + LINE_BREAK + 'Rounds 2,4,6 (B): p1')
	}
	
	@Test
	void nextRoundWithA() {
		renderingContext.engine.declareRoundKnitting()
		processXml '<row assign-row-number="false" xmlns="http://www.knitml.com/schema/pattern" type="round" yarn-ref="yarn1"><knit>1</knit></row>', Row
		assertThat output.trim(), is ('Next round (A): k1')
	}
	
	@Test
	void rowWithInformation() {
		processXml '''<row xmlns="http://www.knitml.com/schema/pattern" number="1">
						<information>
							<message label="whew!"/>
						</information>
						<knit>1</knit>
					</row>''', Row
		assertThat output.trim(), is ('Row 1 [whew!]: k1')
	}
	
	@Test
	void rowWithSide() {
		processXml '''<row xmlns="http://www.knitml.com/schema/pattern" number="1" side="right">
						<knit>1</knit>
					</row>''', Row
		assertThat output.trim(), is ('Row 1 [RS]: k1')
	}
	@Test
	void rowWithInformationAndSide() {
		processXml '''<row xmlns="http://www.knitml.com/schema/pattern" number="1" side="right">
						<information><message label="whew!"/></information>
						<knit>1</knit>
					</row>''', Row
		assertThat output.trim(), is ('Row 1 [whew!,RS]: k1')
	}
	
	static void main(args) {
		JUnitCore.main(RowVisitorTests.name)
	}
	
}
