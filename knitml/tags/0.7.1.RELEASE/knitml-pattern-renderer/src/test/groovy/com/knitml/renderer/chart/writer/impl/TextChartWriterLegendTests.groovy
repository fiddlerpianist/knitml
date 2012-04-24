package com.knitml.renderer.chart.writer.impl

import static com.knitml.core.common.KnittingShape.FLAT
import static com.knitml.core.common.KnittingShape.ROUND
import static com.knitml.core.common.Side.WRONG
import static com.knitml.renderer.chart.ChartElement.*
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.text.StringContains.containsString
import static org.junit.Assert.assertThat

import org.junit.Ignore
import org.junit.Test

import test.support.AbstractRenderingContextTests
import test.support.GuiceJUnit4Runner
import test.support.TextChartTestModule
import test.support.GuiceJUnit4Runner.GuiceModules

@GuiceModules( TextChartTestModule )
class TextArtChartWriterLegendTests extends AbstractRenderingContextTests {

	@Test
	public void stockinetteLegend() {
		processXml PATTERN_START_TAG + '''
	<pattern:directives>
		<pattern:instruction-definitions>
			<instruction id="stockinette" label="St St" shape="flat">
				<row number="1">
					<knit>5</knit>
				</row>
				<row number="2">
					<purl>5</purl>
				</row>
			</instruction>
		</pattern:instruction-definitions>
	</pattern:directives>
    <pattern:directions />
</pattern:pattern>'''

		assertThat renderer.graph, is ([
			[K, K, K, K, K],
			[K, K, K, K, K]
		]);
		assertThat output, containsString ("-: k1 on RS; p1 on WS")
	}

	@Test
	public void garterLegend() {
		processXml PATTERN_START_TAG + '''
	<pattern:directives>
		<pattern:instruction-definitions>
			<instruction id="garter" label="Garter St" shape="flat">
				<row number="1 2">
					<knit>5</knit>
				</row>
			</instruction>
		</pattern:instruction-definitions>
	</pattern:directives>
    <pattern:directions />
</pattern:pattern>'''

		assertThat renderer.graph, is ([
			[K, K, K, K, K],
			[P, P, P, P, P]
		]);
		assertThat output, containsString ("-: k1" + LINE_SEPARATOR)
		assertThat output, containsString ("=: k1 on WS")
	}

	@Test
	public void decreaseIncreaseCableLegend() {
		processXml PATTERN_START_TAG + '''
	<pattern:directives>
		<pattern:instruction-definitions>
			<instruction id="chart" label="Chart" shape="flat">
				<row number="1">
					<knit />
					<purl />
					<decrease type="k2tog" />
					<increase type="yo" />
					<decrease type="ssk" />
				</row>
				<row number="2 4">
					<repeat until="end">
						<knit />
					</repeat>
				</row>
				<row number="3">
					<purl />
					<group size="4">
						<cross-stitches first="2" next="2" type="back" />
						<knit>4</knit>
					</group>
				</row>
				<row number="5">
					<purl />
					<group size="4">
						<cross-stitches first="2" next="2" type="front" />
						<knit>4</knit>
					</group>
				</row>
			</instruction>
		</pattern:instruction-definitions>
	</pattern:directives>
    <pattern:directions />
</pattern:pattern>'''

		assertThat renderer.graph, is ([
			[K, P, K2TOG, YO, SSK],
			[P, P, P, P, P],
			[P, CBL_2_2_RC],
			[P, P, P, P, P],
			[P, CBL_2_2_LC]
		]);
		assertThat output, containsString ("-: k")
		assertThat output, containsString ("=: p")
		assertThat output, containsString ("/: k2tog")
		assertThat output, containsString ("\\: ssk")
		assertThat output, containsString ("o: yo")
		assertThat output, containsString ("2RC2: cross next 2 stitches behind following 2 stitches, k4")
		assertThat output, containsString ("2LC2: cross next 2 stitches in front of following 2 stitches, k4")
	}

	@Test
	public void repeatLegend() {
		processXml PATTERN_START_TAG + '''
	<pattern:directives>
		<pattern:instruction-definitions>
			<instruction id="chart" label="Chart" shape="flat">
				<row side="right" number="1">
					<repeat until="times" value="2">
						<knit>2</knit>
						<increase type="m1" />
					</repeat>
				</row>
				<row number="2 3">
					<repeat until="end">
						<work-even />
					</repeat>
				</row>
				<row side="wrong" number="4">
					<repeat until="times" value="2">
						<knit>3</knit>
						<increase type="m1" />
					</repeat>
				</row>
			</instruction>
		</pattern:instruction-definitions>
	</pattern:directives>
	<pattern:directions />
</pattern:pattern>'''

		assertThat renderer.graph, is ([
			[K, K, M1, K, K, M1, NS, NS],
			[K, K, K, K, K, K, NS, NS],
			[K, K, K, K, K, K, NS, NS],
			[M1, P, P, P, M1, P, P, P]
		]);
		assertThat output, containsString ("-: k1 on RS; p1 on WS")
		assertThat output, containsString ("M: M1")
	}

	@Test
	public void incIntoNextStitchLegend() {
		processXml PATTERN_START_TAG + '''
		<pattern:directives>
		<pattern:instruction-definitions>
			<instruction id="chart" label="Chart" shape="round">
				<row>
					<knit>1</knit>
				</row>
				<row>
					<increase-into-next-stitch>
						<knit/>
						<purl/>
						<knit/>
					</increase-into-next-stitch>
				</row>
				<row>
					<knit>3</knit>
				</row>
			</instruction>
		</pattern:instruction-definitions>
	</pattern:directives>
	<pattern:directions />
 </pattern:pattern>'''
		assertThat output, containsString ("[M]: inc into next st [k, p, k]")
	}
}