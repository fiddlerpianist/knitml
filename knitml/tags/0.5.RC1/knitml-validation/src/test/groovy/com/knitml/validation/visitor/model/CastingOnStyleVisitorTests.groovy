/**
 * 
 */
package com.knitml.validation.visitor.model;

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static com.knitml.engine.settings.Direction.FORWARDS

import org.junit.Ignore
import org.junit.Test

import test.support.AbstractKnittingContextTests

class CastingOnStyleVisitorTests extends AbstractKnittingContextTests {
	
	@Test
	void simpleCastOn() {
		processXml '''
		  <directions>
			<cast-on>20</cast-on>
		  </directions>
	    '''
	    engine.with {
		  assertThat totalNumberOfStitchesInRow, is (20)
		  assertThat direction, is (FORWARDS)
	    }
	}

	@Test
	void multipleCastOns() {
		processXml '''
		  <directions>
			<cast-on>20</cast-on>
			<cast-on>10</cast-on>
		  </directions>
	    '''
	    engine.with {
		  assertThat totalNumberOfStitchesInRow, is (30)
		  assertThat direction, is (FORWARDS)
	    }
	}
	
	@Test
	void simplePickUpStitches() {
		processXml '''
		  <directions>
			<pick-up-stitches>20</pick-up-stitches>
		  </directions>
	    '''
	    engine.with {
		  assertThat totalNumberOfStitchesInRow, is (20)
		  assertThat direction, is (FORWARDS)
	    }
	}

	@Test
	void castOnPickUpStitchesCastOn() {
		processXml '''
		  <directions>
			<cast-on>10</cast-on>
			<pick-up-stitches>20</pick-up-stitches>
			<cast-on>10</cast-on>
			<row short="true">
				<knit>30</knit>
				<turn/>
			</row>
			<row short="true">
				<knit>30</knit>
			</row>
			<row>
				<knit>40</knit>
			</row>
		  </directions>
	    '''
	    assertThat engine.totalNumberOfStitchesInRow, is (40)
	}
}
