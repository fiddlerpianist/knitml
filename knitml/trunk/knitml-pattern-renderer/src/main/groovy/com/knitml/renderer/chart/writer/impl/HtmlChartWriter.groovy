package com.knitml.renderer.chart.writer.impl;

import java.io.IOException
import java.io.Writer
import java.util.List
import java.util.ListIterator
import java.util.Map

import javax.inject.Inject

import com.knitml.core.common.KnittingShape
import com.knitml.core.common.Side
import com.knitml.renderer.chart.Chart
import com.knitml.renderer.chart.ChartElement
import com.knitml.renderer.chart.legend.LegendOperationRenderer
import com.knitml.renderer.chart.stylesheet.StylesheetProvider
import com.knitml.renderer.chart.symbol.Symbol
import com.knitml.renderer.chart.symbol.SymbolProvider
import com.knitml.renderer.chart.writer.ChartWriter

public class HtmlChartWriter implements ChartWriter {

	private SymbolProvider symbolProvider
	private LegendOperationRenderer legendOperationRenderer
	private boolean writeLineNumbers = true
	private StylesheetProvider stylesheetProvider
	private String stylesheetClassPrefix
	private String suffix = ":"
	private static final String SYS_LF = System.getProperty("line.separator")
	private static final String LF = "<br />" + SYS_LF;

	@Inject
	public HtmlChartWriter(SymbolProvider symbolProvider, LegendOperationRenderer legendOperationRenderer) {
		this.symbolProvider = symbolProvider
		if (symbolProvider instanceof StylesheetProvider) {
			this.stylesheetProvider = (StylesheetProvider)symbolProvider
			this.stylesheetClassPrefix = stylesheetProvider.getStyleClassPrefix()
		}
		this.legendOperationRenderer = legendOperationRenderer
	}

	public void writeChart(Chart chart, Writer writer) {

		List<List<ChartElement>> graph = chart.graph
		int currentLineNumber = chart.startingRowNumber + graph.size() - 1

		Map<ChartElement, Symbol> elementsUsed = [:]
		ListIterator<List<ChartElement>> graphIt = graph.listIterator(graph.size())
		try {
			writer.with {
				it << "<table class=\"${stylesheetClassPrefix}-chart\">" << SYS_LF
				if (chart.title != null) {
					it << "<caption>${chart.title}</caption>" << SYS_LF
				}
				it << "<colgroup class=\"${stylesheetClassPrefix}-lhcol\" />" << SYS_LF
				it << "<colgroup class=\"${stylesheetClassPrefix}-bodycol\" span=\"${chart.width}\"/>" << SYS_LF
				it << "<colgroup class=\"${stylesheetClassPrefix}-rhcol\" />" << SYS_LF
				it << "<tbody>" << SYS_LF
				while (graphIt.hasPrevious()) {
					def row = graphIt.previous()
					def rowIt = row.listIterator(row.size())

					// left side row number column
					it << "<tr><td>" <<	renderLeftSideRowNumber(currentLineNumber, chart) << "</td>"

					// each chart column
					while (rowIt.hasPrevious()) {
						ChartElement element = rowIt.previous()
						Symbol symbol = symbolProvider.getSymbol(element)
						elementsUsed[element] = symbol
						def cssClass = stylesheetProvider.getStyleClassPrefix(symbol.symbolSetId)

						it << "<td class=\"${cssClass}-cell\"" <<
								(element.width() > 1 ? " colspan=\"${element.width()}\">" : ">") << "${symbol.symbol}</td>"
					}
					it << "<td>"
					if (renderRightSideRowNumber(currentLineNumber, chart)) {
						it << currentLineNumber
					}
					it << "</td></tr>" << SYS_LF
					currentLineNumber--
				}
				it << "</tbody></table>" << SYS_LF
				it << "<p>Legend" << LF
				for (ChartElement element : chart.legend.keySet()) {
					Symbol symbol = elementsUsed[element]
					it << "<span class=\"${stylesheetProvider.getStyleClassPrefix(symbol.getSymbolSetId())}-legend\">${symbol.symbol}</span>"
					it << "${suffix} "
					it << legendOperationRenderer.resolveLegendFor(chart.legend[element]) << LF
					// FIXME
					//it << "${element.toString().toLowerCase()}" << LF
				}
				it << "</p>"
			}
		} catch (IOException ex) {
			throw new RuntimeException("Could not write to writer", ex);
		}

	}

	private boolean shouldRenderLeftSideRowNumber(int currentLineNumber, Chart chart) {
		if (!writeLineNumbers) {
			return false;
		}
		if (chart.getShape() == KnittingShape.ROUND) {
			return false;
		}
		if ((currentLineNumber - chart.getStartingRowNumber()) % 2 == 0) {
			// same direction as first row
			if (chart.getStartingSide() == Side.RIGHT) {
				return false;
			}
			return true;
		}
		// different direction from first row
		if (chart.getStartingSide() == Side.RIGHT) {
			return true;
		}
		return false;
	}

	private String renderLeftSideRowNumber(int currentLineNumber, Chart chart) throws IOException {
		return (shouldRenderLeftSideRowNumber(currentLineNumber, chart) ? currentLineNumber : "")
	}

	private boolean renderRightSideRowNumber(int currentLineNumber, Chart chart) {
		return writeLineNumbers	&& !renderLeftSideRowNumber(currentLineNumber, chart)
	}

}
