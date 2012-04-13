package com.knitml.renderer.chart.writer.impl;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml

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
import com.knitml.renderer.context.RenderingContext

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

	public void writeChart(Chart chart, Writer writer, RenderingContext context) {

		List<List<ChartElement>> graph = chart.graph
		int currentLineNumber = chart.startingRowNumber + graph.size() - 1

		Map<ChartElement, Symbol> elementsUsed = [:]
		ListIterator<List<ChartElement>> graphIt = graph.listIterator(graph.size())
		try {
			writer.with {
				it << "<div class=\"${stylesheetClassPrefix}-graph\">" << SYS_LF
				it << "<table>" << SYS_LF
				if (chart.title != null) {
					it << "<caption>${escapeHtml chart.title}</caption>" << SYS_LF
				}
				it << "<colgroup class=\"${stylesheetClassPrefix}-graph-lhcol\" />" << SYS_LF
				it << "<colgroup class=\"${stylesheetClassPrefix}-graph-bodycol\" span=\"${chart.width}\"/>" << SYS_LF
				it << "<colgroup class=\"${stylesheetClassPrefix}-graph-rhcol\" />" << SYS_LF
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
						def symbolSetFont = stylesheetProvider.getSymbolSetName(symbol.symbolSetId)
						it << "<td class=\"${stylesheetClassPrefix}-graph-cell ${symbolSetFont}"
						if (element == ChartElement.NS && context.options.greyNoStitches) {
							it << " ${stylesheetClassPrefix}-cell-grey"
						}
						it << "\"" << (element.width() > 1 ? " colspan=\"${element.width()}\">" : ">")
						it << "${symbol.symbol}</td>"
					}
					it << "<td>"
					if (renderRightSideRowNumber(currentLineNumber, chart)) {
						it << currentLineNumber
					}
					it << "</td></tr>" << SYS_LF
					currentLineNumber--
				}
				it << "</tbody></table></div>" << SYS_LF
				// write the legend
				it << "<div class=\"${stylesheetClassPrefix}-legend\">" << SYS_LF
				it << "<table>" << SYS_LF
				it << '''<caption>Legend</caption>
				<colgroup />
				<colgroup />
				<tbody>
				'''

				for (ChartElement element : chart.legend.keySet()) {
					Symbol symbol = elementsUsed[element]
					def rawKey = symbol.symbol
					def rawValue = legendOperationRenderer.resolveLegendFor(element, chart.legend[element])
					def symbolSetFont = stylesheetProvider.getSymbolSetName(symbol.getSymbolSetId())
					it << "<tr><td class=\"${stylesheetClassPrefix}-legend-key ${symbolSetFont}"
					if (element == ChartElement.NS && context.options.greyNoStitches) {
						it << " ${stylesheetClassPrefix}-cell-grey"
					}
					it << "\">" <<
							"${escapeHtml rawKey}</td>" <<
							"<td>${escapeHtml rawValue}</td></tr>"
				}
				it << "</tbody></table></div>"
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
