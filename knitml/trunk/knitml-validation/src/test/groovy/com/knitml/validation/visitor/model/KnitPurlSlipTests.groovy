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

class KnitPurlSlipTests extends AbstractKnittingContextTests {
	
	final void onSetItUp() {
		setupEngine()
		assertThat ((engine.totalNumberOfStitchesInRow % 20), is (0))
	}
	
	void setupEngine() {
		engine.castOn 20 
	}
	
	@Test
	void knitPurl() {
		processXml '''
			<row>
		  		<knit>10</knit>
		  		<purl>10</purl>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (20)
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
		processXml '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
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
		  		  <slip-to-holder ref="sh1">10</slip-to-holder>
		  	    </row>
              </directions>
          </pattern>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (10)
	}

	@Test
	void slipFromHolder() {
		processXml '''
			<pattern xmlns="http://www.knitml.com/schema/pattern">
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
		  		  <slip-to-holder ref="sh1">10</slip-to-holder>
		  	    </row>
                <row>
		  		  <knit>10</knit>
		  		  <from-holder ref="sh1">
		  		    <knit>10</knit>
		  		  </from-to-holder>
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
}
