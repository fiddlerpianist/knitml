/**
 * 
 */
package com.knitml.validation.visitor.model;

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
import static com.knitml.core.model.directions.StitchNature.KNIT;
import static com.knitml.core.model.directions.StitchNature.PURL;

import org.junit.Ignore
import org.junit.Test

import test.support.AbstractKnittingContextTests

class KnitPurlSlipTests extends AbstractKnittingContextTests {
	
	final void onSetItUp() {
		setupEngine()
		assertThat ((engine.totalNumberOfStitchesInRow % 20), is (0))
	}
	
	void setupEngine() {
		engine.castOn 20 
	}
	
	@Test
	void workRibbing() {
		processXml '''
			<row>
		  		<knit>10</knit>
		  		<purl>10</purl>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (20)
		engine.startNewRow()
		assertThat engine.peekAtNextStitch().currentNature, is (PURL)
		engine.knit(10)
		assertThat engine.peekAtNextStitch().currentNature, is (KNIT)
	}
	
	@Test
	void knitSlip() {
		processXml '''
			<row>
		  		<knit>10</knit>
		  		<slip>10</slip>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (20)
		engine.startNewRow()
		assertThat engine.peekAtNextStitch().currentNature, is (KNIT)
		engine.knit(10)
		assertThat engine.peekAtNextStitch().currentNature, is (KNIT)
	}

	@Test
	void knitReverseSlipKnit() {
		processXml '''
			<row>
		  		<knit>10</knit>
		  		<slip direction="reverse">10</slip>
		  		<knit>20</knit>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (20)
	}
	
	@Test
	void slipToHolder() {
		processXml PATTERN_START_TAG + '''
			  <supplies>
			    <yarns/>
			    <needles/>
                <accessories>
              	  <stitch-holder id="sh1"/>
                </accessories>
              </supplies>
			  <directions>
                <row>
		  		  <knit>10</knit>
		  		  <slip-to-stitch-holder ref="sh1">10</slip-to-stitch-holder>
		  	    </row>
              </directions>
          </pattern>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (10)
	}

	@Test
	void workFromHolderOnRightSide() {
		processXml PATTERN_START_TAG + '''
			  <supplies>
			    <yarns/>
			    <needles/>
                <accessories>
              	  <stitch-holder id="sh1"/>
                </accessories>
              </supplies>
			  <directions>
                <row>
		  		  <knit>10</knit>
		  		  <slip-to-stitch-holder ref="sh1">10</slip-to-stitch-holder>
		  	    </row>
		  	    <row>
		  	    	<knit>10</knit>
		  	    </row>
                <row side="right">
		  		  <knit>10</knit>
		  		  <from-stitch-holder ref="sh1">
		  		    <knit>10</knit>
		  		  </from-stitch-holder>
		  	    </row>
              </directions>
          </pattern>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (20)
	}
	
	@Test
	void workFromHolderOnWrongSide() {
		processXml PATTERN_START_TAG + '''
			  <supplies>
			    <yarns/>
			    <needles/>
                <accessories>
              	  <stitch-holder id="sh1"/>
                </accessories>
              </supplies>
			  <directions>
                <row>
		  		  <knit>10</knit>
		  		  <slip-to-stitch-holder ref="sh1">10</slip-to-stitch-holder>
		  	    </row>
                <row side="wrong">
		  		  <knit>10</knit>
		  		  <from-stitch-holder ref="sh1">
		  		    <knit>10</knit>
		  		  </from-stitch-holder>
		  	    </row>
              </directions>
          </pattern>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (20)
	}

	@Test
	void bindOffOnHolder() {
		processXml PATTERN_START_TAG + '''
			  <supplies>
			    <yarns/>
			    <needles/>
                <accessories>
              	  <stitch-holder id="sh1"/>
                </accessories>
              </supplies>
			  <directions>
			    <declare-round-knitting/>
                <row>
		  		  <knit>10</knit>
		  		  <slip-to-stitch-holder ref="sh1">10</slip-to-stitch-holder>
		  	    </row>
		  	    <row>
		  		    <knit>10</knit>
		  	    	<from-stitch-holder ref="sh1">
		  	    		<bind-off>5</bind-off>
		  	    		<knit>5</knit>
		  	    	</from-stitch-holder>
		  	    </row>
              </directions>
          </pattern>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (15)
	}
	
	
	@Test
	void workFromStitchHolderWithNoStitchesInRow() {
		processXml PATTERN_START_TAG + '''
			  <supplies>
			    <yarns/>
			    <needles/>
                <accessories>
              	  <stitch-holder id="sh1"/>
                </accessories>
              </supplies>
			  <directions>
                <row>
		  		  <slip-to-stitch-holder ref="sh1">20</slip-to-stitch-holder>
		  	    </row>
                <row side="right">
		  		  <from-stitch-holder ref="sh1">
		  		    <knit>20</knit>
		  		  </from-stitch-holder>
		  	    </row>
              </directions>
          </pattern>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (20)
	}
	
	@Test
	void knitNoStitchKnit() {
		processXml '''
			<row>
		  		<knit>10</knit>
		  		<no-stitch>10</no-stitch>
		  		<knit>10</knit>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (20)
	}
	
	@Test
	void placeMarker() {
		processXml '''
			<row>
		  		<knit>10</knit>
		  		<place-marker/>
		  		<knit>10</knit>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (20)
		engine.startNewRow()
		assertThat engine.stitchesToNextMarker, is (10)
	}

	@Test
	void passPreviousStitchOver() {
		processXml '''
			<row>
		  		<knit>10</knit>
		  		<pass-previous-stitch-over/>
		  		<knit>10</knit>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (19)
	}
	
	@Test
	void passPreviousStitchOverTwice() {
		processXml '''
			<row>
		  		<knit>10</knit>
		  		<pass-previous-stitch-over>2</pass-previous-stitch-over>
		  		<knit>10</knit>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (18)
	}
	
	@Test
	void workRibbingThenWorkEven() {
		processXml PATTERN_START_TAG + '''
		  <directions>
			<row>
		  		<knit>10</knit>
		  		<purl>10</purl>
		  	</row>
		  	<row>
		  		<work-even>20</work-even>
		  	</row>
		  </directions>
        </pattern>
    	'''
		assertThat engine.totalNumberOfStitchesInRow, is (20)
		engine.startNewRow()
		assertThat engine.peekAtNextStitch().currentNature, is (KNIT)
		engine.knit(10)
		assertThat engine.peekAtNextStitch().currentNature, is (PURL)
	}
	

}
