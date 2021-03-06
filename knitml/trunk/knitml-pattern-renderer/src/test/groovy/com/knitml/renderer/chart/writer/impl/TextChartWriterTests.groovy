package com.knitml.renderer.chart.writer.impl

import static com.knitml.core.common.KnittingShape.FLAT
import static com.knitml.core.common.KnittingShape.ROUND
import static com.knitml.core.common.Side.WRONG
import static com.knitml.renderer.chart.ChartElement.*
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.text.StringContains.containsString
import static org.junit.Assert.assertThat

import javax.inject.Inject

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import test.support.AbstractRenderingContextTests;
import test.support.BasicRendererTestModule
import test.support.GuiceJUnit4Runner
import test.support.GuiceJUnit4Runner.GuiceModules
import test.support.TextChartTestModule;

import com.knitml.renderer.chart.Chart
import com.knitml.renderer.chart.symbol.impl.TextArtSymbolProvider
import com.knitml.renderer.chart.writer.ChartWriter

class TextChartWriterTests {
	
	private ChartWriter chartWriter
	private static final String LINE_BREAK = System.getProperty("line.separator")
	
	@Before
	void setUp() {
		chartWriter = new TextChartWriter(new TextArtSymbolProvider(), null)
	}

	@Test
	public void asymmetricFlatChart() {
		def logicalChart = [[K,P,P,K,K,P,P,P,P],
		                    [K,P,K2TOG,YO,YO,SSK,P,P,P]];
		StringWriter output = new StringWriter()
		chartWriter.writeChart(new Chart(logicalChart), output, null)
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
		chartWriter.writeChart(chart, output, null)
		assertThat output.toString(), containsString ( 
				"  |==-o---/------\\---o-==| 2" + LINE_BREAK +
				"  |==o---/--------\\---o==| 1");
		
	}
	
	@Test
	public void basicChartStartingOnWrongSide() {
		def logicalChart = [[K,K,K,K],
                            [K,K,K,K]];
		StringWriter output = new StringWriter()
		Chart chart = new Chart(logicalChart)
		chart.shape = FLAT
		chart.startingSide = WRONG
		chartWriter.writeChart(chart, output, null)
		assertThat output.toString(), containsString ( 
				"  |----| 2" + LINE_BREAK +
				"1 |----|");
		
	}

}