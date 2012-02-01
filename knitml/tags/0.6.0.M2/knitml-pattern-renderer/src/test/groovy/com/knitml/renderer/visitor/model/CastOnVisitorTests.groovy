package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.hamcrest.text.StringEndsWith.endsWith
import static org.junit.Assert.assertThat

import org.junit.Test

import com.knitml.core.model.directions.block.CastOn
import com.knitml.core.model.directions.block.Row 
import com.knitml.core.model.directions.inline.InlineCastOn;

import test.support.AbstractRenderingContextTests

class CastOnVisitorTests extends AbstractRenderingContextTests {

	
	@Test
	void castOnIdentifiedKey() {
		processXml '<cast-on style="long-tail" xmlns="http://www.knitml.com/schema/operations">5</cast-on>', CastOn
		assertThat output, startsWith ('Using the long-tail method, cast on 5')
	}

	@Test
	void castOnUnidentifiedKey() {
		processXml '<cast-on style="filbert" xmlns="http://www.knitml.com/schema/operations">5</cast-on>', CastOn
		assertThat output, startsWith ('Using the filbert method, cast on 5')
	}
	
	@Test
	void inlineCastOn() {
		renderingContext.with {
			engine.castOn 1
			engine.startNewRow()
		}
		
		processXml '<inline-cast-on xmlns="http://www.knitml.com/schema/operations">5</inline-cast-on>', InlineCastOn
		assertThat output.trim(), is ('cast on 5 stitches')
	}
	
}
