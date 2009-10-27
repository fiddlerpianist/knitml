package com.knitml.renderer.visitor.model;

import static org.hamcrest.text.StringEndsWith.endsWith;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import test.support.AbstractRenderingContextTests;

import com.knitml.core.model.directions.block.Row;

public class StitchHolderHandlerTestsJava extends AbstractRenderingContextTests {

	@Before
	public void before() {
		renderingContext.getEngine().castOn(60);
	}

	@Test
	public void developingTest() throws Exception {
		processXml("<row xmlns='http://www.knitml.com/schema/pattern' number='1'>" +
		"<knit>60</knit>" +
		"<repeat until='end'><knit /></repeat></row>", Row.class);
		assertThat (getOutput().trim(), endsWith ("Row 1: k60"));
	}
	

}
