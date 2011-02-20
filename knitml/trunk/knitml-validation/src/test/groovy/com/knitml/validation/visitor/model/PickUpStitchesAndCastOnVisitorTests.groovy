/**
 * 
 */
package com.knitml.validation.visitor.model;

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue

import org.junit.Ignore
import org.junit.Test

import test.support.AbstractKnittingContextTests

class PickUpStitchesAndCastOnVisitorTests extends AbstractKnittingContextTests {
	
	final void onSetItUp() {
		setupEngine()
		assertThat ((engine.totalNumberOfStitchesInRow % 20), is (0))
	}

	void setupEngine() {
		engine.castOn 20, true
	}
	
	@Test
	void simpleInlinePickUpStitches() {
		processXml '''
			<row>
				<inline-pick-up-stitches>10</inline-pick-up-stitches>
				<knit>20</knit>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (30)
	}
	
	@Test
	void inlinePickUpStitchesWithSpecifiedYarn() {
		processXml PATTERN_START_TAG + '''
		<supplies>
			<yarns>
				<yarn-type id="lornas" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering"/>
				<yarn id="main-color" typeref="lornas">
					<total-weight unit="g">100</total-weight>
					<color name="watercolor"/>
				</yarn>
			</yarns>
			<needles>
				<needle-type id="needle-type1" type="circular"/> 
				<needle id="needle1" typeref="needle-type1"/>
			</needles>
			<accessories/>
		</supplies>
		<directions>
			<cast-on yarn-ref="main-color">20</cast-on>
			<row>
				<inline-pick-up-stitches yarn-ref="main-color" type="knitwise">10</inline-pick-up-stitches>
				<knit>20</knit>
		  	</row>
    	</directions>
      </pattern>
	  '''
	  assertThat engine.totalNumberOfStitchesInRow, is (30)
	}
	@Test
	void simpleInlineCastOn() {
		processXml '''
			<row>
				<inline-cast-on>10</inline-cast-on>
				<knit>20</knit>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (30)
	}
	
	@Test
	void inlineCastOnWithSpecifiedYarn() {
		processXml PATTERN_START_TAG + '''
		<supplies>
			<yarns>
				<yarn-type id="lornas" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering"/>
				<yarn id="main-color" typeref="lornas">
					<total-weight unit="g">100</total-weight>
					<color name="watercolor"/>
				</yarn>
			</yarns>
			<needles>
				<needle-type id="needle-type1" type="circular"/> 
				<needle id="needle1" typeref="needle-type1"/>
			</needles>
			<accessories/>
		</supplies>
		<directions>
			<cast-on yarn-ref="main-color">20</cast-on>
			<row>
				<inline-cast-on yarn-ref="main-color" style="backwards-loop">10</inline-cast-on>
				<knit>20</knit>
		  	</row>
    	</directions>
      </pattern>
	  '''
	  assertThat engine.totalNumberOfStitchesInRow, is (30)
	}
}
