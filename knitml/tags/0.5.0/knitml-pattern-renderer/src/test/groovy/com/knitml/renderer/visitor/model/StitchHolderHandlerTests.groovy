package com.knitml.renderer.visitor.model

import static org.junit.Assert.assertThat
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringEndsWith.endsWith


import org.junit.Test
import org.junit.runner.JUnitCore

import test.support.AbstractRenderingContextTests
import com.knitml.core.model.directions.inline.Slip

class StitchHolderHandlerTests extends AbstractRenderingContextTests {

	@Test
	void slipToHolderSingular() {
		processXml PATTERN_START_TAG + '''
		  <supplies>
			<yarns/>
			<needles/>
			<accessories>
				<stitch-holder id="sh1"/>
			</accessories>
		  </supplies>
		  <directions>
			<cast-on>10</cast-on>
			<row>
				<knit>9</knit>
				<slip-to-stitch-holder ref="sh1">1</slip-to-stitch-holder>
			</row>
		  </directions>
		</pattern>
		'''
		assertThat output.trim(), endsWith ('sl next st to holder')
	}

	@Test
	void slipToHolderPlural() {
		processXml PATTERN_START_TAG + '''
		  <supplies>
			<yarns/>
			<needles/>
			<accessories>
				<stitch-holder id="sh1"/>
			</accessories>
		  </supplies>
		  <directions>
			<cast-on>10</cast-on>
			<row>
				<knit>5</knit>
				<slip-to-stitch-holder ref="sh1">5</slip-to-stitch-holder>
			</row>
		  </directions>
		</pattern>
		'''
		assertThat output.trim(), endsWith ('sl next 5 sts to holder')
	}
	
	@Test
	void slipToLabelledHolder() {
		processXml PATTERN_START_TAG + '''
		  <supplies>
			<yarns/>
			<needles/>
			<accessories>
				<stitch-holder id="sh1" label="Stitch Holder A"/>
			</accessories>
		  </supplies>
		  <directions>
			<cast-on>10</cast-on>
			<row>
				<knit>5</knit>
				<slip-to-stitch-holder ref="sh1">5</slip-to-stitch-holder>
			</row>
		  </directions>
		</pattern>
		'''
		assertThat output.trim(), endsWith ('sl next 5 sts to Stitch Holder A')
	}
	
	static void main(args) {
		JUnitCore.main(StitchHolderHandlerTests.name)
	}
	
}
