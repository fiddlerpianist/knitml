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


class DecreaseVisitorTests extends AbstractKnittingContextTests {
	
	final void onSetItUp() {
		setupEngine()
		assertThat ((engine.totalNumberOfStitchesInRow % 20), is (0))
	}

	void setupEngine() {
		engine.castOn(20)
	}
	
	@Test
	public void knitSimpleDecreases() {
		processXml '''
			<row>
				<knit>7</knit>
				<decrease type="k2tog"/>
				<knit>1</knit>
				<decrease type="k2tog"/>
				<knit>8</knit>
			</row>
		'''
		assertTrue engine.betweenRows
		assertThat engine.totalNumberOfStitchesInRow, is (18)
	}

	@Test
	void knitCenterDoubleDecrease() {
		processXml '''
			<row>
				<knit>8</knit>
				<double-decrease type="cdd"/>
				<knit>9</knit>
			</row>
		'''
		assertTrue engine.betweenRows
		assertThat engine.totalNumberOfStitchesInRow, is (18)
	}

	@Test
	void knitThreeTogetherDecrease() {
		processXml '''
			<row>
				<knit>8</knit>
				<double-decrease type="k3tog"/>
				<knit>9</knit>
			</row>
		'''
		assertTrue engine.betweenRows
		assertThat engine.totalNumberOfStitchesInRow, is (18)
	}
	static void main(args) {
		JUnitCore.main(DecreaseVisitorTests.name)
	}
}
