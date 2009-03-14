package com.knitml.validation.visitor.model

import java.util.ArrayList
import java.util.List

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
import static org.easymock.EasyMock.*

import org.junit.Test
import org.junit.runner.JUnitCore

import test.support.AbstractKnittingContextTests

import com.knitml.core.common.NeedleStyle
import com.knitml.validation.common.InvalidStructureException
import com.knitml.engine.common.WrongKnittingShapeException
import com.knitml.engine.common.KnittingEngineException
import com.knitml.engine.common.UnexpectedRowNumberException
import com.knitml.engine.Needle
import com.knitml.engine.KnittingEngine

public class RowVisitorTests extends AbstractKnittingContextTests {
	
	void onSetItUp() {
		setupEngine()
		assertThat ((engine.totalNumberOfStitchesInRow % 20), is (0))
	}

	void setupEngine() {
		Needle needle1 = knittingContext.knittingFactory.createNeedle("needle1", NeedleStyle.CIRCULAR)
		knittingContext.patternRepository.addNeedle(needle1)
		List<Needle> needles = []
		needles.add needle1
		engine.with {
			useNeedles needles
			castOn 20
		}
	}
	
	@Test(expected=WrongKnittingShapeException)
	void processFlatRowWhileKnittingInRound() {
		engine.declareRoundKnitting()
		processXml '''
			<row type="flat">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
		'''
	}

	@Test(expected=WrongKnittingShapeException)
	void processRoundRowWhileKnittingFlat() {
		processXml '''
			<row type="round">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
		'''
	}
	
	@Test
	void processTwoSequentialRows() {
		processXml '''
			<row number="1">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
			<row number="2">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
		'''
	}
	
	@Test
	void processImpliedRowReset() {
		processXml '''
			<row number="1">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
			<row number="2">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
			<row number="1">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
		'''
	}
	
	
	@Test(expected=UnexpectedRowNumberException)
	void processInvalidImpliedRowReset() {
		processXml '''
			<row number="1">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
			<row number="2">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
			<row number="3">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
			<row number="2">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
		'''
	}
	
	@Test
	void processExplicitRowReset() {
		processXml '''
			<row number="1">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
			<!-- Note that reset-row-count takes effect before
                 the row number is processed -->
			<row number="1" reset-row-count="true">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
		'''
	}
	
	@Test(expected=UnexpectedRowNumberException)
	void processInvalidExplicitRowReset() {
		processXml '''
			<row number="1">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
			<!-- Note that reset-row-count takes effect before
                 the row number is processed -->
			<row number="2" reset-row-count="true">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
		'''
	}
	
	@Test(expected=UnexpectedRowNumberException)
	void processTwoNonSequentialRows() {
		processXml '''
			<row number="1">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
			<row number="3">
				<repeat until="end">
					<knit/>
				</repeat>
			</row>
		'''
	}
	
	@Test
	void processVaryingRowNumbers() {
		knittingContext.engine = createNiceMock(KnittingEngine)
		engine = knittingContext.engine
		engine.with {
			startNewRow()
			knit 20
			endRow()
			startNewRow()
			purl 20
			endRow()
			startNewRow()
			knit 20
			endRow()
			startNewRow()
			purl 20
			endRow()
		}
		replay engine
		processXml '''
		<instruction id="thingy">
			<row number="1 3">
				<knit>20</knit>
			</row>
			<row number="2 4">
				<purl>20</purl>
			</row>
		</instruction>
		'''
		verify engine
	}
	
	@Test(expected=InvalidStructureException)
	void processVaryingRowNumbersOutsideOfInstruction() {
		processXml '''
			<row number="1 3">
				<knit>20</knit>
			</row>
			<row number="2 4">
				<purl>20</purl>
			</row>
		'''
	}
	
	static void main(args) {
		JUnitCore.main(RowVisitorTests.name)
	}
}
