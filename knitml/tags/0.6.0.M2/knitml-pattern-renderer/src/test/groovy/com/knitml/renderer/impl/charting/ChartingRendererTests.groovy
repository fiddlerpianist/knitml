package com.knitml.renderer.impl.charting

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertThat
import static com.knitml.renderer.chart.ChartElement.*
import static test.support.JiBXUtils.parseXml 

import test.support.AbstractRenderingContextTests


import org.junit.Ignore 
import org.junit.Test
import org.junit.runner.RunWith


class ChartingRendererTests extends AbstractRenderingContextTests {
	
	@Override
	protected String[] getConfigLocations() {
		return ["applicationContext-patternRenderer-charting.xml"]
	}
	@Test
	public void flatChart() {
		processXml PATTERN_START_TAG + '''
			<pattern:directives>
				<pattern:instruction-definitions>
					<instruction id="inst1" label="Stockinette Stitch" shape="flat"> 
						<row> 
							<purl>2</purl>
							<knit loop-to-work="trailing">2</knit>
						</row>
						<row>
							<knit loop-to-work="trailing">2</knit>
							<purl>2</purl>
						</row>
					</instruction>
				</pattern:instruction-definitions>
			</pattern:directives>
		 </pattern:pattern>'''
		 
		 assertThat renderer.graph, is ([[P,P,K_TW,K_TW],[K,K,P_TW,P_TW]])
	}

	@Test
	public void flatChartStartingOnWrongSide() {
		processXml PATTERN_START_TAG + '''
			<pattern:directives>
				<pattern:instruction-definitions>
					<instruction id="inst1" label="Stockinette Stitch" shape="flat">
						<row side="wrong">
							<purl>2</purl>
							<knit loop-to-work="trailing">2</knit>
						</row>
						<row>
							<knit loop-to-work="trailing">2</knit>
							<purl>2</purl>
						</row>
					</instruction>
				</pattern:instruction-definitions>
			</pattern:directives>
		 </pattern:pattern>'''
		 
		 assertThat renderer.graph, is ([[P_TW,P_TW,K,K],[K_TW,K_TW,P,P]])
	}

	
	@Test
	public void flatChartWithWorkEven() {
		processXml PATTERN_START_TAG + '''
			<pattern:directives>
				<pattern:instruction-definitions>
					<instruction id="inst1" label="Stockinette Stitch" shape="flat"> 
						<row> 
							<knit>2</knit>
							<purl>2</purl>
						</row>
						<row>
							<work-even>4</work-even>
						</row>
					</instruction>
				</pattern:instruction-definitions>
			</pattern:directives>
		 </pattern:pattern>'''

		 assertThat renderer.graph, is ([[K,K,P,P],[K,K,P,P]])
	}
	
	@Test
	public void flatChartWithWorkEvenWithRepeat() {
		processXml PATTERN_START_TAG + '''
			<pattern:directives>
				<pattern:instruction-definitions>
					<instruction id="inst1" label="Stockinette Stitch" shape="flat"> 
						<row> 
							<knit>2</knit>
							<purl>2</purl>
						</row>
						<row>
							<repeat until="times" value="4">
								<work-even/>
							</repeat>
						</row>
					</instruction>
				</pattern:instruction-definitions>
			</pattern:directives>
		 </pattern:pattern>'''

		 assertThat renderer.graph, is ([[K,K,P,P],[K,K,P,P]])
	}

	
	@Test
	public void roundChart() {
		processXml PATTERN_START_TAG + '''
			<pattern:directives>
				<pattern:instruction-definitions>
					<instruction id="inst1" label="Stockinette Stitch" shape="round"> 
						<row> 
							<knit>4</knit>
						</row>
						<row>
							<knit>4</knit>
						</row>
					</instruction>
				</pattern:instruction-definitions>
			</pattern:directives>
		 </pattern:pattern>'''

		 assertThat renderer.graph, is ([[K,K,K,K],[K,K,K,K]])
	}
	
	@Test
	public void roundChartWithWorkEven() {
		processXml PATTERN_START_TAG + '''
			<pattern:directives>
				<pattern:instruction-definitions>
					<instruction id="inst1" label="Stockinette Stitch" shape="round"> 
						<row> 
							<knit>2</knit>
							<purl>2</purl>
						</row>
						<row>
							<work-even>4</work-even>
						</row>
					</instruction>
				</pattern:instruction-definitions>
			</pattern:directives>
		 </pattern:pattern>'''

		 assertThat renderer.graph, is ([[K,K,P,P],[K,K,P,P]])
	}

	
	@Test
	public void asymmetricFlatChart() {
		processXml PATTERN_START_TAG + '''
			<pattern:directives>
				<pattern:instruction-definitions>
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
				</pattern:instruction-definitions>
			</pattern:directives>
		 </pattern:pattern>'''

		 assertThat renderer.graph, is ([[K,K,P,P,K,K,P,P],
		                                 [K,K,P,P,K,K,P,P]]);
	}

	@Test
	public void laceWithEqualRows() {
		// reminiscient of the nutkin2 sample
		processXml PATTERN_START_TAG + '''
			<pattern:directives>
				<pattern:instruction-definitions>
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
				</pattern:instruction-definitions>
			</pattern:directives>
		 </pattern:pattern>'''

		 assertThat renderer.graph, is ([[P,P,YO,K,K,K,SSK,K,K,K,K,K,K,K,K,K2TOG,K,K,K,YO,P,P],
		                                 [P,P,K,YO,K,K,K,SSK,K,K,K,K,K,K,K2TOG,K,K,K,YO,K,P,P]]);
	}
}
