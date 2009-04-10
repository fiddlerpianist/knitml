package com.knitml.renderer.visitor.model;

import static org.hamcrest.text.StringEndsWith.endsWith;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import test.support.AbstractRenderingContextTests;

public class StitchHolderHandlerTestsJava extends AbstractRenderingContextTests {

	@Test
	public void slipToHolderSingular() throws Exception {
		processXml(PATTERN_START_TAG +
		  "<supplies><yarns/><needles/><accessories><stitch-holder id='sh1'/></accessories></supplies>" +
		  "<directions>" +
			"<cast-on>10</cast-on>" +
			"<row><knit>9</knit><slip-to-stitch-holder ref='sh1'>1</slip-to-stitch-holder></row>" +
		  "</directions></pattern>");
		assertThat (getOutput(), endsWith ("sl next st to holder"));
	}
	
}
