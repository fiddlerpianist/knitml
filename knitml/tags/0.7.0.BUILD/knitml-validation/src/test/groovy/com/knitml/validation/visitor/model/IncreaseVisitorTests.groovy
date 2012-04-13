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

class IncreaseVisitorTests extends AbstractKnittingContextTests {
	
	final void onSetItUp() {
		setupEngine()
		assertThat ((engine.totalNumberOfStitchesInRow % 20), is (0))
	}

	void setupEngine() {
		engine.castOn 20 
	}
	
	@Test
	void knitSimpleIncreases() {
		processXml '''
			<row short="true">
		  		<knit>10</knit>
		  		<increase type="m1a"/>
		  		<increase type="m1a"/>
		  		<knit>10</knit>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (22)
	}

	@Test
	void knitMultiIncreases() {
		processXml '''
			<row short="true">
		  		<knit>10</knit>
		  		<increase type="m1a">5</increase>
		  		<knit>10</knit>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (25)
	}
	
	@Test
	void kfbMultiIncrease() {
		processXml '''
			<row short="true">
		  		<increase type="kfb">15</increase>
		  		<knit>5</knit>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (35)
	}
	
	@Test
	void knitToFrontAndBackIncrease() {
		processXml '''
			<row short="true">
		  		<knit>10</knit>
		  		<increase type="kfb"/>
		  		<knit>9</knit>
		  	</row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (21)
	}
	
	@Test
	void knitIncreasesIntoNextStitch() {
		processXml '''
			<row>
				<knit>10</knit>
				<increase-into-next-stitch>
					<knit>1</knit>
					<purl>1</purl>		
					<knit>1</knit>
					<purl>1</purl>		
				</increase-into-next-stitch>
				<knit>9</knit>
			</row>
		'''
		assertThat engine.totalNumberOfStitchesInRow, is (23)
	}
}
