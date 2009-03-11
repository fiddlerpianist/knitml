package com.knitml.renderer.chart.writer.impl

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.text.StringContains.containsString
import static org.junit.Assert.assertThat
import static com.knitml.renderer.chart.ChartElement.*
import static com.knitml.core.common.KnittingShape.ROUND
import com.knitml.renderer.chart.Chart
import com.knitml.renderer.chart.writer.ChartWriter
import com.knitml.renderer.chart.translator.impl.TextArtLaceTranslator
import org.hamcrest.text.StringContains
import org.junit.Before
import org.junit.Test
import org.junit.internal.runners.JUnit4ClassRunner
import org.junit.runner.RunWith


@RunWith(JUnit4ClassRunner.class)
class TextArtChartWriterTests {
	
	private ChartWriter chartWriter
	private static final String LINE_BREAK = System.getProperty("line.separator")	

	@Before
	void setUp() {
		chartWriter = new TextArtChartWriter(new TextArtLaceTranslator())
	}
	
	@Test
	public void stitchLegend() {
		def logicalChart = [[K,P,K2TOG,YO,SSK]];
		StringWriter output = new StringWriter()
		chartWriter.writeChart(new Chart(logicalChart), output)
		def result = output.toString()
		assertThat result, containsString ("-: k")
		assertThat result, containsString ("=: p")
		assertThat result, containsString ("/: k2tog")
		assertThat result, containsString ("\\: ssk")
		assertThat result, containsString ("o: yo")
	}

	@Test
	public void asymmetricFlatChart() {
		def logicalChart = [[K,P,P,K,K,P,P,P,P],
		                    [K,P,K2TOG,YO,YO,SSK,P,P,P]];
		StringWriter output = new StringWriter()
		chartWriter.writeChart(new Chart(logicalChart), output)
		assertThat output.toString(), containsString (
				               "2 |===\\oo/=-|  " + LINE_BREAK +
				               "  |====--==-| 1");
	}
	
	@Test
	public void symmetricLaceChart() {
		def logicalChart = [[P,P,YO,K,K,K,SSK,K,K,K,K,K,K,K,K,K2TOG,K,K,K,YO,P,P],
                            [P,P,K,YO,K,K,K,SSK,K,K,K,K,K,K,K2TOG,K,K,K,YO,K,P,P]];
		StringWriter output = new StringWriter()
		Chart chart = new Chart(logicalChart)
		chart.shape = ROUND
		chartWriter.writeChart(chart, output)
		assertThat output.toString(), containsString ( 
				"  |==-o---/------\\---o-==| 2" + LINE_BREAK +
				"  |==o---/--------\\---o==| 1");
		
	}
	
}