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
		<pattern:supplies>
			<pattern:yarn-types>
				<pattern:yarn-type id="lornas" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering">
					<pattern:yarns>
						<common:yarn id="main-color">
							<common:total-weight unit="g">100</common:total-weight>
							<common:color name="watercolor"/>
						</common:yarn>
					</pattern:yarns>
				</pattern:yarn-type>
			</pattern:yarn-types>
			<pattern:needle-types>
				<pattern:needle-type id="needle-type1" type="circular"> 
					<pattern:needles>
						<common:needle id="needle1" typeref="needle-type1"/>
					</pattern:needles>
				</pattern:needle-type>
			</pattern:needle-types>
			<pattern:accessories/>
		</pattern:supplies>
		<pattern:directions>
			<cast-on yarn-ref="main-color">20</cast-on>
			<row>
				<inline-pick-up-stitches yarn-ref="main-color" type="knitwise">10</inline-pick-up-stitches>
				<knit>20</knit>
		  	</row>
    	</pattern:directions>
      </pattern:pattern>
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
		<pattern:supplies>
			<pattern:yarn-types>
				<pattern:yarn-type id="lornas" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering">
					<pattern:yarns>
						<common:yarn id="main-color">
							<common:total-weight unit="g">100</common:total-weight>
							<common:color name="watercolor"/>
						</common:yarn>
					</pattern:yarns>
				</pattern:yarn-type>
			</pattern:yarn-types>
			<pattern:needle-types>
				<pattern:needle-type id="needle-type1" type="circular"> 
					<pattern:needles>
						<common:needle id="needle1" typeref="needle-type1"/>
					</pattern:needles>
				</pattern:needle-type>
			</pattern:needle-types>
			<pattern:accessories/>
		</pattern:supplies>
		<pattern:directions>
			<cast-on yarn-ref="main-color">20</cast-on>
			<row>
				<inline-cast-on yarn-ref="main-color" style="backwards-loop">10</inline-cast-on>
				<knit>20</knit>
		  	</row>
    	</pattern:directions>
      </pattern:pattern>
	  '''
	  assertThat engine.totalNumberOfStitchesInRow, is (30)
	}
}
