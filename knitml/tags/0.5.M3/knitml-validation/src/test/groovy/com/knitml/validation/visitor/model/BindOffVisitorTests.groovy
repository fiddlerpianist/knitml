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


class BindOffVisitorTests extends AbstractKnittingContextTests {
	
	final void onSetItUp() {
		setupEngine()
		assertThat ((engine.totalNumberOfStitchesInRow % 30), is (0))
	}

	void setupEngine() {
		engine.castOn(30, true)
	}
	
	@Test
	public void bindOffAll() {
		processXml '''
			<row>
				<bind-off-all/>
			</row>
		'''
		assertTrue engine.betweenRows
		assertThat engine.totalNumberOfStitchesInRow, is (0)
	}

	@Test
	public void bindOffAllWithNoFastenOff() {
		processXml '''
			<row>
				<bind-off-all fasten-off-last-stitch="false"/>
			</row>
		'''
		assertTrue engine.betweenRows
		assertThat engine.totalNumberOfStitchesInRow, is (1)
	}
	
	@Test
	public void bindOff30() {
		processXml '''
			<row>
				<bind-off>30</bind-off>
			</row>
		'''
		assertTrue engine.betweenRows
		assertThat engine.totalNumberOfStitchesInRow, is (0)
	}
	
	@Test
	public void bindOff10InMiddle() {
		processXml '''
			<row>
				<knit>10</knit>
				<bind-off>10</bind-off>
				<knit>10</knit>
			</row>
		'''
		assertTrue engine.betweenRows
		assertThat engine.totalNumberOfStitchesInRow, is (20)
	}
	
	static void main(args) {
		JUnitCore.main(BindOffVisitorTests.name)
	}
}
