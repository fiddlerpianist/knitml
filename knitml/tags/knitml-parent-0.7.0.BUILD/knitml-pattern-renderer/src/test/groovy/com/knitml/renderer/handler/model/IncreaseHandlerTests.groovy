package com.knitml.renderer.handler.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Ignore 
import org.junit.Test
import org.junit.Before

import com.knitml.core.model.operations.inline.Increase;
import com.knitml.core.model.operations.inline.IncreaseIntoNextStitch;

import test.support.AbstractRenderingContextTests

class IncreaseHandlerTests extends AbstractRenderingContextTests {
	
	@Before
	void setup() {
		renderingContext.engine.castOn 3
	}
	
	
	@Test
	void m1() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="m1" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('M1')
	}
	
	@Test
	void m1p() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="m1p" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('M1 (purled)')
	}
	
	@Test
	void m1a() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="m1a" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('M1A')
	}
	
	@Test
	void m1t() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="m1t" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('M1T')
	}
	
	@Test
	void m1l() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="m1l" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('M1L')
	}
	
	@Test
	void m1r() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="m1r" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('M1R')
	}
	
	@Test
	void m1lp() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="m1lp" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('M1L (purled)')
	}
	
	@Test
	void m1rp() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="m1rp" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('M1R (purled)')
	}
	
	@Test
	void kll() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="kll" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('kll inc')
	}
	
	@Test
	void krl() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="krl" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('krl inc')
	}
	
	@Test
	void pll() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="pll" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('pll inc')
	}
	
	@Test
	void prl() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="prl" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('prl inc')
	}
	
	@Test
	void kfb() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="kfb" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('kfb')
	}

	@Test
	void pfb() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="pfb" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('pfb')
	}
	
	@Test
	void moss() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="moss" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('moss inc')
	}

	@Test
	void decorative() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="decorative" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('decorative inc')
	}

	@Test
	void yo() {
		renderingContext.engine.startNewRow()
		processXml '<increase type="yo" xmlns="http://www.knitml.com/schema/operations"/>', Increase
		assertThat output, is ('yo')
	}
	
	@Test
	void increaseIntoNextStitchWithoutRow() {
		renderingContext.engine.startNewRow()
		processXml '''
		  <increase-into-next-stitch xmlns="http://www.knitml.com/schema/operations">
		    <knit>1</knit>
		    <purl>1</purl>
		  </increase-into-next-stitch>''', IncreaseIntoNextStitch
		assertThat output, is ('inc into next st [k1, p1]')
	}
	
	@Test
	void increaseIntoNextStitchWithRow() {
		processXml '''
		<row xmlns="http://www.knitml.com/schema/operations">
		  <increase-into-next-stitch>
		    <knit>1</knit>
		    <purl>1</purl>
		  </increase-into-next-stitch>
		  <knit>2</knit>
		</row>
		''', IncreaseIntoNextStitch
		assertThat output.trim(), is ('Row 1: inc into next st [k1, p1], k2')
	}

}
