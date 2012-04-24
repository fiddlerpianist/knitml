package com.knitml.validation.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue

import org.junit.Test

import test.support.AbstractKnittingContextTests

import com.knitml.core.common.ValidationException

class MoreStitchesRepeatVisitorTests extends RepeatVisitorTests {

	@Override
	void setupEngine() {
		engine.with {
			castOn 40
			startNewRow()
			knit 20
			placeMarker()
			knit 20
			endRow()
		}
	}

	@Test
	@Override
	void repeatDecreasingInstruction() {
		def xml = '''
			<row>
				<repeat until="end">
					<decrease type="k2tog"/>
				</repeat>
			</row>
			'''
		engine.with {
			processXml xml
	    	assertThat totalNumberOfStitchesInRow, is (20)
	    	processXml xml
	    	assertThat totalNumberOfStitchesInRow, is (10)
		}
	}

}
