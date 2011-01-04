package com.knitml.renderer.visitor.model;

import org.junit.Test;

import test.support.AbstractRenderingContextTests;

public class GenericTestsJava extends AbstractRenderingContextTests {

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "applicationContext-patternRenderer-charting.xml" };
	}

	@Test
	public void flatChartWithWorkEven() throws Exception {
//		processXml(PATTERN_START_TAG
//				+ "<directives><instruction-definitions>"
//				+ "<instruction id='inst1' label='Stockinette Stitch' shape='flat'>"
//				+ "<row><knit>2</knit><purl>2</purl></row>"
//				+ "<row><repeat until='times' value='4'><work-even/></repeat></row>"
//				+ "</instruction-definitions></directives></pattern>");
		//
		// assertThat renderer.graph, is ([[K,K,P,P],[K,K,P,P]])
	}

}
