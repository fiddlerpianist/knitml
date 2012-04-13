/**
 * 
 */
package com.knitml.validation.visitor.model

import org.junit.Test
import org.junit.runner.JUnitCore

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue

import test.support.AbstractKnittingContextTests
import com.knitml.engine.common.KnittingEngineException


class CrossStitchesVisitorTests extends AbstractKnittingContextTests {
	
	final void onSetItUp() {
		setupEngine()
		assertThat ((engine.totalNumberOfStitchesInRow % 20), is (0))
	}

	void setupEngine() {
		engine.castOn(20)
	}
	
	@Test
	public void simpleCableCross() {
		processXml '''
			<row>
				<purl>8</purl>
				<group size="4">
					<cross-stitches first="2" next="2" type="front"/>
					<knit>4</knit>
				</group>
				<purl>8</purl>
			</row>
		'''
		assertTrue engine.betweenRows
	}

	@Test
	public void cableCrossWithSkippedStitches() {
		processXml '''
			<row>
				<purl>7</purl>
				<group size="5">
					<cross-stitches first="2" next="2" type="front" skip="1" skip-type="back"/>
					<knit>5</knit>
				</group>
				<purl>8</purl>
			</row>
		'''
		assertTrue engine.betweenRows
	}
}
