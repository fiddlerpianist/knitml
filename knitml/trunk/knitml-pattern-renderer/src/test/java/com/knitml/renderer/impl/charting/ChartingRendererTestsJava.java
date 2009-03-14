package com.knitml.renderer.impl.charting;

import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

import test.support.AbstractRenderingContextTests;

@RunWith(JUnit4ClassRunner.class)
public class ChartingRendererTestsJava extends AbstractRenderingContextTests {

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "applicationContext-patternRenderer-charting.xml" };
	}

//	@Test
//	public void simpleInstruction() throws Exception {
//		processXml(
//				"<pattern xmlns='http://www.knitml.com/schema/pattern'><directives><instruction-definitions><instruction id='inst1' label='That' shape='flat'><row><knit>4</knit></row>"
//						+ "   	<row>       	<purl>4</purl>	</row> </instruction></instruction-definitions></directives></pattern>");
//		List<List<ChartElement>> chart = ((ChartingRenderer) renderer).getGraph();
//	}

}
