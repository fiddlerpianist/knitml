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
