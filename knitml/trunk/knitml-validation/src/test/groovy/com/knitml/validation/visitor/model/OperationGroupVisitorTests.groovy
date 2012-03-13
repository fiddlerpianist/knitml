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


class OperationGroupVisitorTests extends AbstractKnittingContextTests {
	
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
				<purl>19</purl>
				<group size="1">
					<slip yarn-position="front"/>
					<increase type="yo"/>
				</group>
			</row>
		'''
		assertTrue engine.betweenRows
	}

}
