package com.knitml.validation.visitor.model

import java.util.ArrayList
import java.util.List

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
import static org.easymock.EasyMock.*

import org.junit.Test

import test.support.AbstractKnittingContextTests

import com.knitml.core.common.NeedleStyle
import com.knitml.validation.common.InvalidStructureException
import com.knitml.engine.common.KnittingEngineException
import com.knitml.engine.KnittingEngine
import com.knitml.engine.Needle

class InstructionVisitorTests extends AbstractKnittingContextTests {
	
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
			startNewRow()
			knit 10
			placeMarker()
			knit 10
			endRow()
		}
	}
	
	@Test
	void processInstructionWithSimpleRows() {
		processXml '''
			<instruction id="cuff-round">
				<row>
					<repeat until="end">
						<knit/>
					</repeat>
				</row>
				<row>
					<repeat until="end">
						<purl/>
					</repeat>
				</row>
			</instruction>
		'''
	}

	@Test
	void processInstructionWithComplexRows() {
		processXml '''
			<instruction id="cuff-round">
				<row>
					<inline-instruction id="thingy">
						<knit>4</knit>
					</inline-instruction>
					<repeat until="times" value="4">
						<inline-instruction-ref ref="thingy"/>
					</repeat>
				</row>
				<row>
					<repeat until="end">
						<purl/>
					</repeat>
				</row>
			</instruction>
		'''
	}
	

	@Test
	void processInstruction() {
		def pattern = processXml ('''
			<instruction id="cuff-round">
				<row>
					<repeat until="end">
						<knit>1</knit>
						<purl>1</purl>
					</repeat>
				</row>
			</instruction>
		''')
		assertThat pattern.directions.operations[0].operations[0], is (knittingContext.patternRepository.getBlockInstruction('cuff-round'))
	}
	
	@Test
	void processInstructionGroup() {
		def pattern = processXml ('''
			<instruction-group id="cuff" label="Make the Cuff">
				<instruction id="purl-to-end">
					<row>
						<repeat until="end">
							<purl/>
						</repeat>
					</row>
				</instruction>
				<instruction id="cuff-round">
					<row>
						<repeat until="end">
							<knit>1</knit>
							<purl>1</purl>
						</repeat>
					</row>
				</instruction>
			</instruction-group>
		''')
		// note that we are not saving instruction groups at this time
		assertThat knittingContext.patternRepository.getBlockInstruction('cuff'), is (null)
		def ig1 = pattern.directions.operations[0]
		def i1 = pattern.directions.operations[0].operations[0]
		def i2 = pattern.directions.operations[0].operations[1]
		assertThat pattern.directions.operations[0].operations[0], is (knittingContext.patternRepository.getBlockInstruction('purl-to-end'))
		assertThat pattern.directions.operations[0].operations[1], is (knittingContext.patternRepository.getBlockInstruction('cuff-round'))
	}
	
}
