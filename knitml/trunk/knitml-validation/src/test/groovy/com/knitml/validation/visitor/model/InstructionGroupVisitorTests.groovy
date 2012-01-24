package com.knitml.validation.visitor.model

import java.util.ArrayList
import java.util.List
import com.knitml.core.model.directions.block.CastOnimport org.junit.runner.JUnitCoreimport org.junit.runner.RunWith
import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
import static org.easymock.EasyMock.*

import org.junit.Test

import test.support.AbstractKnittingContextTests

import com.knitml.core.common.KnittingShape
import com.knitml.validation.common.InvalidStructureException
import com.knitml.engine.common.KnittingEngineException
import com.knitml.engine.KnittingEngine
import com.knitml.engine.Needle

class InstructionGroupVisitorTests extends AbstractKnittingContextTests {
	
	@Test
	void mergedInstructionAtRow() {
		knittingContext.engine = createStrictMock(KnittingEngine)
		engine = knittingContext.engine
		engine.with {
			expect (knittingShape) . andStubReturn (KnittingShape.FLAT)
			expect (totalNumberOfStitchesInRow) . andStubReturn (20)
			expect (currentRowNumber) . andStubReturn (1)
			expect (totalRowsCompleted) . andStubReturn (1)
			startNewRow(); knit 5; knit 15; endRow()
			startNewRow(); purl 15; purl 5;	endRow()
			startNewRow(); knit 5; knit 15; endRow()
			startNewRow(); purl 15; purl 5;	endRow()
		}
		replay engine
		processXml PATTERN_START_TAG + '''
				<pattern:directives>
					<pattern:instruction-definitions>
						<instruction id="knit-purl" label="Knit / Purl" shape="flat">
							<row><knit>5</knit></row>
							<row><purl>15</purl></row>
						</instruction>
						<instruction id="purl-knit" label="Purl / Knit" shape="flat">
							<row><knit>15</knit></row>
							<row><purl>5</purl></row>
						</instruction>
						<merged-instruction id="merged1" merge-point="row" type="physical" label="Alternate Knits and Purls">
							<instruction-ref ref="knit-purl"/>
							<instruction-ref ref="purl-knit"/>
						</merged-instruction>
					</pattern:instruction-definitions>
				</pattern:directives>
				<pattern:directions>
					<pattern:instruction-group id="ig1">
						<instruction-ref ref="merged1"/>
						<repeat-instruction ref="merged1">
							<additional-times>1</additional-times>
						</repeat-instruction>
					</pattern:instruction-group>
				</pattern:directions>
			</pattern:pattern>
			'''
		verify engine
	}
	
	@Test
	void mergedInstructionAtEnd() {
		knittingContext.engine = createStrictMock(KnittingEngine)
		engine = knittingContext.engine
		engine.with {
			expect (knittingShape) . andStubReturn (KnittingShape.FLAT)
			expect (totalNumberOfStitchesInRow) . andStubReturn (20)
			expect (currentRowNumber) . andStubReturn (1)
			expect (totalRowsCompleted) . andStubReturn (1)
			startNewRow(); knit 5; purl 15; endRow()
			startNewRow(); purl 5; knit 15;	endRow()
			startNewRow(); knit 5; purl 15; endRow()
			startNewRow(); purl 5; knit 15;	endRow()
		}
		replay engine
		processXml PATTERN_START_TAG + '''
				<pattern:directives>
					<pattern:instruction-definitions>
						<instruction id="knit-purl" label="Knit / Purl" shape="flat">
							<row>
								<knit>5</knit>
								<purl>15</purl>
							</row>
						</instruction>
						<instruction id="purl-knit" label="Purl / Knit" shape="flat">
							<row>
								<purl>5</purl>
								<knit>15</knit>
							</row>
						</instruction>
						<merged-instruction id="merged1" merge-point="end" type="physical" label="Alternating knits and purls">
							<instruction-ref ref="knit-purl"/>
							<instruction-ref ref="purl-knit"/>
						</merged-instruction>
					</pattern:instruction-definitions>
				</pattern:directives>
				<pattern:directions>
					<pattern:instruction-group id="ig1">
						<instruction-ref ref="merged1"/>
						<repeat-instruction ref="merged1">
							<additional-times>1</additional-times>
						</repeat-instruction>
					</pattern:instruction-group>
				</pattern:directions>
			</pattern:pattern>
			'''
		verify engine
	}
	
	@Test
	void compositeInstructionFromTwoInlineInstructions() {
		knittingContext.engine = createStrictMock (KnittingEngine)
		engine = knittingContext.engine
		def castOnSpec = new CastOn()
		castOnSpec.numberOfStitches = 8
		engine.with {
			expect (knittingShape) . andStubReturn (KnittingShape.FLAT)
			expect (totalNumberOfStitchesInRow) . andStubReturn (20)
			expect (currentRowNumber) . andStubReturn 1
			expect (totalRowsCompleted) . andStubReturn 1
			castOn castOnSpec
			startNewRow()
			expect (stitchesRemainingInRow) . andReturn 8
			knit 2; purl 2; expect (stitchesRemainingInRow) . andReturn 4
			knit 2; purl 2; expect (stitchesRemainingInRow) . andReturn 0
			endRow()
			startNewRow()
			expect (stitchesRemainingInRow) . andReturn 8
			knit 1; expect (stitchesRemainingInRow) . andReturn 7
			knit 1; expect (stitchesRemainingInRow) . andReturn 6
			knit 1; expect (stitchesRemainingInRow) . andReturn 5
			knit 1; expect (stitchesRemainingInRow) . andReturn 4
			knit 1; expect (stitchesRemainingInRow) . andReturn 3
			knit 1; expect (stitchesRemainingInRow) . andReturn 2
			knit 1; expect (stitchesRemainingInRow) . andReturn 1
			knit 1; expect (stitchesRemainingInRow) . andReturn 0
			endRow()
		}
		replay engine
		processXml PATTERN_START_TAG + '''
				<pattern:directives>
					<pattern:instruction-definitions>
						<inline-instruction id="k2p2-ribbing" label="k2p2 rib">
							<knit>2</knit>
							<purl>2</purl>
						</inline-instruction>
						<inline-instruction id="stockinette" label="st st">
							<knit/>
						</inline-instruction>
						<instruction id="garter-ribbing" label="garter rib" shape="flat">
							<row>
								<repeat until="end">
									<inline-instruction-ref ref="k2p2-ribbing"/>
								</repeat>
							</row>
							<row>
								<repeat until="end">
									<inline-instruction-ref ref="stockinette"/>
								</repeat>
							</row>
						</instruction>
					</pattern:instruction-definitions>
				</pattern:directives>
				<pattern:directions>
					<pattern:instruction-group id="ig1">
						<cast-on>8</cast-on>
						<instruction-ref ref="garter-ribbing"/>
					</pattern:instruction-group>
				</pattern:directions>
			</pattern:pattern>
			'''
		verify engine
	}
	
	@Test
	void forEachRowInInstructionWithApplyNextRow() {
		knittingContext.engine = createStrictMock(KnittingEngine)
		engine = knittingContext.engine
		def castOnSpec = new CastOn()
		castOnSpec.numberOfStitches = 12

		engine.with {
			expect (knittingShape) . andStubReturn (KnittingShape.FLAT)
			castOn castOnSpec
			startNewRow()
			knit 5; purl 3; knit 2; purl 2
			endRow()
			startNewRow()
			knit 1; purl 7; knit 4
			endRow()
			startNewRow()
			knit 2; purl 6; knit 2; purl 2
			endRow()
			startNewRow()
			knit 5; purl 3; knit 4
			endRow()
			startNewRow()
			knit 1; purl 7; knit 2; purl 2
			endRow()
			startNewRow()
			knit 2; purl 6; knit 4
			endRow()
		}
		replay engine
		processXml PATTERN_START_TAG + '''
				<pattern:directives>
					<pattern:instruction-definitions>
						<instruction id="garter-ribbing" label="garter rib" shape="flat">
							<row>
								<knit>2</knit>
								<purl>2</purl>
							</row>
							<row>
								<knit>4</knit>
							</row>
						</instruction>
						<instruction id="body-pattern" label="body pattern" shape="flat">
							<row>
								<knit>5</knit>
								<purl>3</purl>
							</row>
							<row>
								<knit>1</knit>
								<purl>7</purl>
							</row>
							<row>
								<knit>2</knit>
								<purl>6</purl>
							</row>
						</instruction>
					</pattern:instruction-definitions>
				</pattern:directives>
				<pattern:directions>
					<pattern:instruction-group id="ig1">
						<cast-on>12</cast-on>
						<instruction id="hybrid-pattern">
							<for-each-row-in-instruction ref="body-pattern">
								<apply-next-row instruction-ref="body-pattern"/>
								<apply-next-row instruction-ref="garter-ribbing"/>
							</for-each-row-in-instruction>
						</instruction>
						<repeat-instruction ref="hybrid-pattern">
							<additional-times>1</additional-times>
						</repeat-instruction>
					</pattern:instruction-group>
				</pattern:directions>
			</pattern:pattern>
			'''
		verify engine
	}
	@Test
	void forEachRowInInstruction() {
		knittingContext.engine = createStrictMock (KnittingEngine)
		engine = knittingContext.engine
		def castOnSpec = new CastOn()
		castOnSpec.numberOfStitches = 8

		engine.with {
			castOn castOnSpec
			startNewRow(); knit 8; endRow()
			startNewRow(); knit 8; endRow()
			startNewRow(); knit 8; endRow()
			startNewRow(); knit 8; endRow()
			startNewRow(); knit 8; endRow()
			startNewRow(); knit 8; endRow()
		}
		replay engine
		processXml PATTERN_START_TAG + '''
				<pattern:directives>
					<pattern:instruction-definitions>
						<instruction id="test-instruction" label="test instruction" shape="flat">
							<row/>
							<row/>
							<row/>
						</instruction>
					</pattern:instruction-definitions>
				</pattern:directives>
				<pattern:directions>
					<pattern:instruction-group id="ig1">
						<cast-on>8</cast-on>
						<instruction id="hybrid-pattern">
							<for-each-row-in-instruction ref="test-instruction">
								<knit>8</knit>
							</for-each-row-in-instruction>
						</instruction>
						<repeat-instruction ref="hybrid-pattern">
							<additional-times>1</additional-times>
						</repeat-instruction>
					</pattern:instruction-group>
				</pattern:directions>
			</pattern:pattern>
			'''
		verify engine
	}
	@Test
	void nestedForEachRowInInstruction() {
		knittingContext.engine = createStrictMock(KnittingEngine)
		engine = knittingContext.engine
		def castOnSpec = new CastOn()
		castOnSpec.numberOfStitches = 8
		checkOrder engine, true
		engine.with {
			castOn castOnSpec
			startNewRow(); knit 8; endRow()
			startNewRow(); knit 8; endRow()
			startNewRow(); knit 8; endRow()
			startNewRow(); knit 8; endRow()
			startNewRow(); knit 8; endRow()
			startNewRow(); knit 8; endRow()
		}
		replay engine
		processXml PATTERN_START_TAG + '''
				<pattern:directives>
					<pattern:instruction-definitions>
						<instruction id="test-instruction-target" label="test instruction target" shape="flat">
							<row/>
							<row/>
							<row/>
						</instruction>
						<instruction id="test-instruction" label="test instruction" shape="flat">
							<for-each-row-in-instruction ref="test-instruction-target"/>
						</instruction>
					</pattern:instruction-definitions>
				</pattern:directives>
				<pattern:directions>
					<pattern:instruction-group id="ig1">
						<cast-on>8</cast-on>
						<instruction id="final-instruction">
							<for-each-row-in-instruction ref="test-instruction">
								<knit>8</knit>
							</for-each-row-in-instruction>
						</instruction>
						<repeat-instruction ref="final-instruction">
							<additional-times>1</additional-times>
						</repeat-instruction>
					</pattern:instruction-group>
				</pattern:directions>
			</pattern:pattern>
			'''
		verify engine
	}
}
