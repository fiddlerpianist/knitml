package com.knitml.renderer.handler.model

import static org.junit.Assert.assertThat
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringEndsWith.endsWith


import org.junit.Test
import org.junit.runner.JUnitCore

import test.support.AbstractRenderingContextTests

import com.knitml.core.model.operations.inline.Slip;

class StitchHolderHandlerTests extends AbstractRenderingContextTests {

	@Test
	void slipToHolderSingular() {
		processXml PATTERN_START_TAG + '''
		  <pattern:supplies>
			<pattern:yarn-types/>
			<pattern:needle-types/>
			<pattern:accessories>
				<common:stitch-holder id="sh1"/>
			</pattern:accessories>
		  </pattern:supplies>
		  <pattern:directions>
			<cast-on>10</cast-on>
			<row>
				<knit>9</knit>
				<slip-to-stitch-holder ref="sh1">1</slip-to-stitch-holder>
			</row>
		  </pattern:directions>
		</pattern:pattern>
		'''
		assertThat output.trim(), endsWith ('sl next st to holder')
	}

	@Test
	void slipToHolderPlural() {
		processXml PATTERN_START_TAG + '''
		  <pattern:supplies>
			<pattern:yarn-types/>
			<pattern:needle-types/>
			<pattern:accessories>
				<common:stitch-holder id="sh1"/>
			</pattern:accessories>
		  </pattern:supplies>
		  <pattern:directions>
			<cast-on>10</cast-on>
			<row>
				<knit>5</knit>
				<slip-to-stitch-holder ref="sh1">5</slip-to-stitch-holder>
			</row>
		  </pattern:directions>
		</pattern:pattern>
		'''
		assertThat output.trim(), endsWith ('sl next 5 sts to holder')
	}
	
	@Test
	void slipToLabelledHolder() {
		processXml PATTERN_START_TAG + '''
		  <pattern:supplies>
			<pattern:yarn-types/>
			<pattern:needle-types/>
			<pattern:accessories>
				<common:stitch-holder id="sh1" label="Stitch Holder A"/>
			</pattern:accessories>
		  </pattern:supplies>
		  <pattern:directions>
			<cast-on>10</cast-on>
			<row>
				<knit>5</knit>
				<slip-to-stitch-holder ref="sh1">5</slip-to-stitch-holder>
			</row>
		  </pattern:directions>
		</pattern:pattern>
		'''
		assertThat output.trim(), endsWith ('sl next 5 sts to Stitch Holder A')
	}
	
}
