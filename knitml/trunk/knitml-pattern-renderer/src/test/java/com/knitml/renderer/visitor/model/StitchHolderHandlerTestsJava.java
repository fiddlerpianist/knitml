package com.knitml.renderer.visitor.model;

import static org.hamcrest.text.StringEndsWith.endsWith;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import test.support.AbstractRenderingContextTests;

import com.knitml.core.model.directions.block.Row;

public class StitchHolderHandlerTestsJava extends AbstractRenderingContextTests {

	@Override
	protected String[] getConfigLocations() {
		return new String[] {"applicationContext-patternRenderer-charting.xml"};
	}
	
	@Test
	public void flatChartWithWorkEven() throws Exception{
		processXml (PATTERN_START_TAG + "<directives><instruction-definitions>" +
		"<instruction id='inst1' label='Stockinette Stitch' shape='flat'>" + 
						"<row><knit>2</knit><purl>2</purl></row>" +
						"<row><work-even>4</work-even></row></instruction>" +
				"</instruction-definitions></directives></pattern>");
//
//		 assertThat renderer.graph, is ([[K,K,P,P],[K,K,P,P]])
	}
	

}
