package com.knitml.renderer.impl.charting

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertThat
import static com.knitml.renderer.chart.ChartElement.*
import static test.support.JiBXUtils.parseXml 

import test.support.AbstractRenderingContextTests

import java.io.StringReader

import org.junit.Before
import org.junit.Test
import org.junit.internal.runners.JUnit4ClassRunner
import org.junit.runner.RunWith

import com.knitml.core.model.directions.block.Instruction
import com.knitml.core.model.Pattern
import com.knitml.renderer.context.RenderingContext
import com.knitml.renderer.context.RenderingContextFactory
import com.knitml.renderer.context.impl.DefaultRenderingContextFactory

@RunWith(JUnit4ClassRunner.class)
class ChartingRendererTests extends AbstractRenderingContextTests {
	
	@Override
	protected String[] getConfigLocations() {
		return ["applicationContext-patternRenderer-charting.xml"]
	}
	@Test
	public void flatChart() {
		processXml PATTERN_START_TAG + '''
			<directives>
				<instruction-definitions>
					<instruction id="inst1" label="Stockinette Stitch" shape="flat"> 
						<row> 
							<knit>4</knit>
						</row>
						<row>
							<purl>4</purl>
						</row>
					</instruction>
				</instruction-definitions>
			</directives>
		 </pattern>'''
		 
		 assertThat renderingContext.renderer.graph, is ([[K,K,K,K],[K,K,K,K]])
	}

	@Test
	public void roundChart() {
		processXml PATTERN_START_TAG + '''
			<directives>
				<instruction-definitions>
					<instruction id="inst1" label="Stockinette Stitch" shape="round"> 
						<row> 
							<knit>4</knit>
						</row>
						<row>
							<knit>4</knit>
						</row>
					</instruction>
				</instruction-definitions>
			</directives>
		 </pattern>'''

		 assertThat renderingContext.renderer.graph, is ([[K,K,K,K],[K,K,K,K]])
	}
	
	@Test
	public void asymmetricFlatChart() {
		processXml PATTERN_START_TAG + '''
			<directives>
				<instruction-definitions>
					<instruction id="inst1" label="2x2 Ribbing" shape="flat"> 
						<row>
							<repeat until="times" value="2">
								<knit>2</knit>
								<purl>2</purl>
							</repeat>
						</row>
						<row>
							<repeat until="end">
								<knit>2</knit>
								<purl>2</purl>
							</repeat>
						</row>
					</instruction>
				</instruction-definitions>
			</directives>
		 </pattern>'''

		 assertThat renderingContext.renderer.graph, is ([[K,K,P,P,K,K,P,P],
		                                                  [K,K,P,P,K,K,P,P]]);
	}

	@Test
	public void laceWithEqualRows() {
		// reminiscient of the nutkin2 sample
		processXml PATTERN_START_TAG + '''
			<directives>
				<instruction-definitions>
					<instruction id="nutkin2" label="Nutkin 2" shape="round"> 
						<row>
							<purl>2</purl>
							<increase type="yo" />
							<knit>3</knit>
							<decrease type="ssk" />
							<knit>8</knit>
							<decrease type="k2tog" />
							<knit>3</knit>
							<increase type="yo" />
							<purl>2</purl>
						</row>
						<row>
							<purl>2</purl>
							<knit>1</knit>
							<increase type="yo" />
							<knit>3</knit>
							<decrease type="ssk" />
							<knit>6</knit>
							<decrease type="k2tog" />
							<knit>3</knit>
							<increase type="yo" />
							<knit>1</knit>
							<purl>2</purl>
						</row>
					</instruction>
				</instruction-definitions>
			</directives>
		 </pattern>'''

		 assertThat renderingContext.renderer.graph, is ([[P,P,YO,K,K,K,SSK,K,K,K,K,K,K,K,K,K2TOG,K,K,K,YO,P,P],
		                                                  [P,P,K,YO,K,K,K,SSK,K,K,K,K,K,K,K2TOG,K,K,K,YO,K,P,P]]);
	}
}
