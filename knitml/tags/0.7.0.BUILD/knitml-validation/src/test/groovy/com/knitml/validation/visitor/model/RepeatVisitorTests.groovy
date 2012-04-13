package com.knitml.validation.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue

import org.junit.Test
import org.junit.Ignore
import org.junit.runner.JUnitCore

import test.support.AbstractKnittingContextTests

import com.knitml.core.common.ValidationException

class RepeatVisitorTests extends AbstractKnittingContextTests {

	final void onSetItUp() {
		setupEngine()
		assertThat (engine.totalNumberOfStitchesInRow % 20, is (0)) 
	}

	void setupEngine() {
		engine.with {
			castOn(20)
			startNewRow()
			knit(10)
			placeMarker()
			knit(10)
			endRow()
		}
	}
	
	@Test
	void knitToEnd() {
		processXml '''
		<row>
			<repeat until="end">
				<knit/>
				<purl/>
			</repeat>
		</row>
		'''
	}

	@Test(expected=ValidationException)
	void knitToEndWithValueAttribute() {
		processXml '''
		<row short="true">
			<repeat until="end" value="5">
				<knit/>
				<purl/>
			</repeat>
		</row>
		'''
	}
	
	@Test
	void knitToMarker() {
		processXml '''
		<row short="true">
			<repeat until="marker">
	    		<knit/>
	    		<purl/>
	    	</repeat>
	    </row>
		'''
		engine.with {
			int stitchesInRow = totalNumberOfStitchesInRow / 2 
			assertThat stitchesInRow, is (stitchesRemainingInRow)
		}
	}
	
	@Test(expected=ValidationException)
	void knitToMarkerWithValueAttribute() {
		processXml '''
		<row short="true">
			<repeat until="marker" value="5">
    			<knit/>
    			<purl/>
    		</repeat>
    	</row>
    	'''
	}
	
	@Test
	void knitToTwoBeforeEnd() {
		processXml '''
		<row short="true">
			<repeat until="before-end" value="2">
    			<knit/>
    			<purl/>
    		</repeat>
    	</row>
    	'''
		assertThat engine.stitchesRemainingInRow, is (2)
	}
	
	@Test(expected=ValidationException)
	void knitToTwoBeforeEndNoValueAttribute() {
		processXml '''
		<row>
			<repeat until="before-end">
				<knit/>
				<purl/>
			</repeat>
		</row>
		'''
	}
	
	@Test(expected=NumberFormatException)
	void knitToTwoBeforeEndBlankValueAttribute() {
		processXml '''
		<row>
		<repeat until="before-end" value="">
			<knit/>
			<purl/>
		</repeat>
		</row>
		'''
	}
	
	@Test
	void knitToTwoBeforeMarker() {
		processXml '''
		<row short="true">
			<repeat until="before-marker" value="2">
    			<knit/>
    			<purl/>
    		</repeat>
    	</row>
    	'''
    	engine.with {
    		// make sure there are an even number of stitches
    		assertThat totalNumberOfStitchesInRow % 2, is (0)
    		int stitchesToMarker = stitchesRemainingInRow - (totalNumberOfStitchesInRow / 2)
    		assertThat stitchesToMarker, is (2)
    	}
	}
	
	@Test(expected=ValidationException)
	void knitToTwoBeforeMarkerNoValueAttribute() {
		processXml '''
		<row short="true">
			<repeat until="before-marker">
				<knit/>
				<purl/>
			</repeat>
		</row>
		'''
	}
	
	@Test(expected=NumberFormatException)
	void knitToTwoBeforeMarkerBlankValueAttribute() {
		processXml '''
		<row>
			<repeat until="before-marker" value="">
				<knit/>
				<purl/>
			</repeat>
		</row>
		'''
	}
	
	@Test
	void knitTimes() {
		processXml '''
		<row short="true">
			<repeat until="times" value="5">
				<knit/>
				<purl/>
			</repeat>
		</row>
		'''
		engine.with {
			assertThat (totalNumberOfStitchesInRow - stitchesRemainingInRow, is (10))
		}
	}
	
	@Test(expected=ValidationException)
	void knitTimesNoValueAttribute() {
		processXml '''
		<row short="true">
			<repeat until="times">
				<knit/>
				<purl/>
			</repeat>
		</row>
		'''
	}
	
	@Test(expected=NumberFormatException)
	void knitTimesBlankValueAttribute() {
		processXml '''
		<row>
			<repeat until="times" value="">
				<knit/>
				<purl/>
			</repeat>
		</row>
		'''
	}
	
	@Test(expected=ValidationException)
	void knitWithNegativeValueAttribute() {
		processXml '''
		<row>
		<repeat until="times" value="-1">
			<knit/>
			<purl/>
		</repeat>
		</row>
		'''
	}

	@Test(expected=ValidationException)
	void knitWithZeroValueAttribute() {
		processXml '''
		<row>
		<repeat until="times" value="0">
			<knit/>
			<purl/>
		</repeat>
		</row>
		'''
	}
	
	@Test
	public void repeatDecreasingInstruction() throws Exception {
		processXml '''
		<row>
		<repeat until="end">
	    	<decrease type="k2tog"/>
	    </repeat>
	    </row>
	    '''
		assertThat engine.totalNumberOfStitchesInRow, is (10)
	}
	
	static void main(args) {
		JUnitCore.main(RepeatVisitorTests.name)
	}
	
}
