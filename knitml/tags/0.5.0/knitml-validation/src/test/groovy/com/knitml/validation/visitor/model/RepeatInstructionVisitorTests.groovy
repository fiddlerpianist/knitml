package com.knitml.validation.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
import com.knitml.core.model.directions.inline.Increase
import org.junit.Test

import test.support.AbstractKnittingContextTests;

import com.knitml.core.units.Units
import com.knitml.core.units.KnittingMeasure

class RepeatInstructionVisitorTests extends AbstractKnittingContextTests {
	
	int numberOfSetUpRows
	
	final void onSetItUp() {
		numberOfSetUpRows = 1
		engine.with {
			// this operation takes one row, as the cast on row doesn't count
			castOn 20
			startNewRow()
			knit 10
			placeMarker()
			knit 10
			endRow()
		}
	}
	
	@Test
	void processAdditionalTimesDocument() {
		processXml '''
		<instruction-group id="cuff">
			<instruction id="cuff-round">
				<row>
					<repeat until="end">
						<knit>1</knit>
						<purl>1</purl>
					</repeat>
				</row>
			</instruction>
			<repeat-instruction ref="cuff-round">
				<additional-times>9</additional-times>
			</repeat-instruction>
		</instruction-group>
		'''
		engine.with {
			assertTrue betweenRows
			assertThat currentRowNumber, is (10 + numberOfSetUpRows)
		}
	}

	@Test
	void processUntilMeasuresDocument() {
		knittingContext.patternRepository.rowGauge = KnittingMeasure.valueOf(10, Units.ROWS_PER_INCH)
		processXml '''
		<instruction-group id="cuff" label="Make the Cuff">
			<instruction id="cuff-round">
				<row>
					<repeat until="end">
						<knit>1</knit>
						<purl>1</purl>
					</repeat>
				</row>
			</instruction>
			<repeat-instruction ref="cuff-round">
				<until-measures unit="in">1.5</until-measures>
			</repeat-instruction>
		</instruction-group>
		'''
		engine.with {
			assertTrue betweenRows
			// knitting 1.5 inches at 10 rows per inch should yield 15 rows worked
			assertThat currentRowNumber, is (15 + numberOfSetUpRows)
		}
	}

	@Test
	void processUntilStitchesRemainDocument() {
		engine.with {
			startNewRow()
			increase (new Increase(20))
			knit 20
			// there are now 40 stitches
			startNewRow()
			knit 10
			placeMarker()
			knit 20
			placeMarker()
			knit 10
			endRow()
			assertThat totalNumberOfStitchesInRow, is (40)
			// we added 2 set up rows
			numberOfSetUpRows += 2
			processXml '''
				<instruction-group id="make-toe">
					<instruction id="toe-rows">
						<row>
							<repeat until="before-marker" value="3">
								<knit/>
							</repeat>
							<decrease type="k2tog"/>
							<knit>1</knit>
							<!-- marker here -->
							<knit>1</knit>
							<decrease type="ssk"/>
							<repeat until="before-marker" value="3">
								<knit/>
							</repeat>
							<decrease type="k2tog"/>
							<knit>1</knit>
							<!-- marker here -->
							<knit>1</knit>
							<decrease type="ssk"/>
							<repeat until="end">
								<knit/>
							</repeat>
						</row>
					</instruction>
					<repeat-instruction ref="toe-rows">
						<until-stitches-remain>20</until-stitches-remain>
					</repeat-instruction>
				</instruction-group>
			'''
			assertTrue betweenRows
			assertThat currentRowNumber, is (5 + numberOfSetUpRows)
		}
	}
}
