package com.knitml.validation.visitor.model

import java.util.ArrayList
import java.util.List

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
import static org.easymock.EasyMock.*
import static com.knitml.engine.settings.Direction.FORWARDS
import static com.knitml.engine.settings.Direction.BACKWARDS

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
	void processNonSequentialRowNumbersWithinInstruction() {
		knittingContext.engine = createNiceMock(KnittingEngine)
		engine = knittingContext.engine
		engine.with {
			expect (totalNumberOfStitchesInRow) . andStubReturn (20)
			for (int i=1; i <= 4; i++) {
				expect (currentRowNumber) . andReturn (i)
			}
			startNewRow(); knit 20; endRow()
			startNewRow(); purl 20; endRow()
			startNewRow(); knit 20; endRow()
			startNewRow(); purl 20; endRow()
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
	
	@Test
	void processRowNumbersWithinInstructionWithSubsequentAttribute() {
		knittingContext.engine = createNiceMock(KnittingEngine)
		engine = knittingContext.engine
		engine.with {
			expect (totalNumberOfStitchesInRow) . andStubReturn (20)
			for (int i=1; i <= 8; i++) {
				expect (currentRowNumber) . andReturn (i)
			}
			startNewRow(); knit 20; endRow()
			startNewRow(); purl 20; endRow()
			startNewRow(); knit 20; endRow()
			startNewRow(); purl 20; endRow()
			startNewRow(); knit 20; endRow()
			startNewRow(); purl 20; endRow()
			startNewRow(); knit 20; endRow()
			startNewRow(); purl 20; endRow()
		}
		replay engine
		processXml '''
		<instruction id="thingy" row-count="8">
			<row number="1" subsequent="odd">
				<knit>20</knit>
			</row>
			<row number="2" subsequent="even">
				<purl>20</purl>
			</row>
		</instruction>
		'''
		verify engine
	}
	
	@Test
	void processRowNumbersWithinInstructionWithUnusualSubsequentConfiguration() {
		knittingContext.engine = createNiceMock(KnittingEngine)
		engine = knittingContext.engine
		engine.with {
			expect (totalNumberOfStitchesInRow) . andStubReturn (20)
			startNewRow(); knit 20; endRow()
			startNewRow(); knit 20; endRow()
			startNewRow(); knit 20; endRow()
			startNewRow(); purl 20; endRow()
			startNewRow(); knit 20; endRow()
			startNewRow(); purl 20; endRow()
			startNewRow(); knit 20; endRow()
			startNewRow(); purl 20; endRow()
		}
		replay engine
		processXml '''
		<instruction id="thingy" row-count="8">
			<row number="1 2" subsequent="odd">
				<knit>20</knit>
			</row>
			<row number="4" subsequent="even">
				<purl>20</purl>
			</row>
		</instruction>
		'''
		verify engine
	}
	
	@Test(expected=InvalidStructureException)
	void processUnassignedRowNumbersWithinLocalInstruction() {
		knittingContext.engine = createNiceMock(KnittingEngine)
		engine = knittingContext.engine
		expect (engine.totalNumberOfStitchesInRow) . andStubReturn (20)
		replay engine
		// expecting that this should be a global instruction (i.e., require a label or message-key)
		processXml '''
		<instruction id="thingy">
			<row assign-row-number="false">
				<knit>20</knit>
			</row>
			<row assign-row-number="false">
				<purl>20</purl>
			</row>
		</instruction>
		'''
		verify engine
	}

	@Test
	void processUnassignedRowNumbersWithinGlobalInstruction() {
		knittingContext.engine = createNiceMock(KnittingEngine)
		engine = knittingContext.engine
		engine.with {
			expect (totalNumberOfStitchesInRow) . andStubReturn (20)
			startNewRow(); knit 20; endRow()
			startNewRow(); purl 20; endRow()
		}
		replay engine
		processXml '''
		<instruction id="thingy" label="Thingy">
			<row assign-row-number="false">
				<knit>20</knit>
			</row>
			<row assign-row-number="false">
				<purl>20</purl>
			</row>
		</instruction>
		'''
		verify engine
	}

	@Test(expected=InvalidStructureException)
	void processMixedRowNumberDeclarations1WithinInstruction() {
		// expecting that this should be a global instruction (i.e., require a label or message-key)
		processXml '''
		<instruction id="thingy">
			<row number="1">
				<knit>20</knit>
			</row>
			<row>
				<purl>20</purl>
			</row>
		</instruction>
		'''
	}

	@Test(expected=InvalidStructureException)
	void processMixedRowNumberDeclarations2WithinInstruction() {
		// expecting that this should be a global instruction (i.e., require a label or message-key)
		processXml '''
		<instruction id="thingy">
			<row>
				<knit>20</knit>
			</row>
			<row number="2">
				<purl>20</purl>
			</row>
		</instruction>
		'''
	}

	@Test(expected=UnexpectedRowNumberException)
	void processLocalInstructionWithRowNumberNotConsecutive() {
		knittingContext.engine = createNiceMock(KnittingEngine)
		engine = knittingContext.engine
		engine.with {
			expect (totalNumberOfStitchesInRow) . andStubReturn (20)
			expect (currentRowNumber) . andReturn (1).times(2)
			expect (currentRowNumber) . andReturn (2).times(2)
			startNewRow(); knit 20; endRow()
		}
		replay engine
		processXml '''
		<row number="1"><knit>20</knit></row>
		<instruction id="thingy">
			<row number="3">
				<knit>20</knit>
			</row>
			<row number="4">
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
	
	@Test(expected=InvalidStructureException)
	void zeroStitchesInRowAtStartWithNoSideSpecified() {
		// when there are 0 stitches in a row, the side of the next row must be specified
		processXml '''
			<row>
				<bind-off-all/>
			</row>
			<row>
				<inline-pick-up-stitches>20</inline-pick-up-stitches>
			</row>
		'''
	}

	@Test
	void zeroStitchesInRowAtStartWithRightSideSpecified() {
		processXml '''
			<row>
				<bind-off-all/>
			</row>
			<row side="right">
				<inline-pick-up-stitches>20</inline-pick-up-stitches>
			</row>
		'''
		assertThat engine.direction, is (FORWARDS)
	}

	@Test
	void zeroStitchesInRowAtStartWithWrongSideSpecified() {
		processXml '''
			<row>
				<bind-off-all/>
			</row>
			<row side="wrong">
				<inline-pick-up-stitches>20</inline-pick-up-stitches>
			</row>
		'''
		assertThat engine.direction, is (BACKWARDS)
	}

	@Test(expected=WrongKnittingShapeException)
	void attemptToKnitLongRowFlat() {
		processXml '''
			<row long="true">
				<knit>25</knit>
			</row>
			<row>
				<knit>20</knit>
			</row>
		'''
	}
	
	@Test
	void knitLongRow() {
		engine.declareRoundKnitting()
		processXml '''
			<row long="true">
				<knit>25</knit>
			</row>
			<row>
				<knit>20</knit>
			</row>
		'''
	}
	
}
