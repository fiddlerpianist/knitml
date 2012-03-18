package com.knitml.renderer.chart.writer.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.inject.Inject;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.Side;
import com.knitml.renderer.chart.Chart;
import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.stylesheet.StylesheetProvider;
import com.knitml.renderer.chart.symbol.NoSymbolFoundException;
import com.knitml.renderer.chart.symbol.Symbol;
import com.knitml.renderer.chart.symbol.SymbolProvider;
import com.knitml.renderer.chart.writer.ChartWriter;

public class HtmlChartWriter implements ChartWriter {

	private SymbolProvider symbolProvider;
	private boolean writeLineNumbers = true;
	private StylesheetProvider stylesheetProvider;
	private String stylesheetClassPrefix;
	private String suffix = ":";
	private static final String SYSTEM_LINE_BREAK = System
			.getProperty("line.separator");
	private static final String LINE_BREAK = "<br />" + SYSTEM_LINE_BREAK;

	@Inject
	public HtmlChartWriter(SymbolProvider symbolProvider) {
		this.symbolProvider = symbolProvider;
		if (symbolProvider instanceof StylesheetProvider) {
			this.stylesheetProvider = (StylesheetProvider)symbolProvider;
			this.stylesheetClassPrefix = stylesheetProvider.getStyleClassPrefix();
		}
	}

	public void writeChart(Chart chart, Writer writer)
			throws NoSymbolFoundException {

		List<List<ChartElement>> graph = chart.getGraph();
		int currentLineNumber = chart.getStartingRowNumber() + graph.size() - 1;

		Map<ChartElement, Symbol> elementsUsed = new LinkedHashMap<ChartElement, Symbol>();
		ListIterator<List<ChartElement>> graphIt = graph.listIterator(graph
				.size());
		try {
			writer.write("<table class=\"" + stylesheetClassPrefix + "-chart\">"
					+ SYSTEM_LINE_BREAK);
			if (chart.getTitle() != null) {
				writer.write("<caption>");
				writer.write(chart.getTitle());
				writer.write("</caption>" + SYSTEM_LINE_BREAK);
			}

			writer.write("<colgroup class=\"");
			writer.write(stylesheetClassPrefix + "-lhcol\" />"
					+ SYSTEM_LINE_BREAK);
			writer.write("<colgroup class=\"");
			writer.write(stylesheetClassPrefix + "-bodycol\" ");
			writer.write("span=\"" + chart.getWidth() + "\" ");
			writer.write("/>" + SYSTEM_LINE_BREAK);
			writer.write("<colgroup class=\"");
			writer.write(stylesheetClassPrefix + "-rhcol\" />"
					+ SYSTEM_LINE_BREAK);

			writer.write("<tbody>" + SYSTEM_LINE_BREAK);
			while (graphIt.hasPrevious()) {
				List<ChartElement> row = graphIt.previous();
				ListIterator<ChartElement> rowIt = row.listIterator(row.size());

				// left side row number column
				writer.write("<tr><td>");
				renderLeftSideRowNumber(currentLineNumber, chart, writer);
				writer.write("</td>");

				// each chart column
				while (rowIt.hasPrevious()) {
					ChartElement element = rowIt.previous();
					Symbol symbol = symbolProvider.getSymbol(element);
					elementsUsed.put(element, symbol);
					String cssClass = stylesheetProvider.getStyleClassPrefix(symbol.getSymbolSetId());

					writer.write("<td class=\"" + cssClass + "-cell\"");
					if (element.width() > 1) {
						writer.write(" colspan=\"" + element.width() + "\"");
					}
					writer.write(">");
					writer.write(symbol.getSymbol());
					writer.write("</td>");
				}

				writer.write("<td>");
				if (renderRightSideRowNumber(currentLineNumber, chart)) {
					writer.write(String.valueOf(currentLineNumber));
				}
				writer.write("</td></tr>");
				currentLineNumber--;
				writer.write(SYSTEM_LINE_BREAK);
			}
			writer.write("</tbody></table>" + SYSTEM_LINE_BREAK);
			writer.write("<p>Legend");
			writer.write(LINE_BREAK);
			for (ChartElement element : elementsUsed.keySet()) {
				// FIXME non-internationalized, not to mention ugly
				Symbol symbol = elementsUsed.get(element);
				writer.write("<span class=\"" + stylesheetProvider.getStyleClassPrefix(symbol.getSymbolSetId())
						+ "-legend\">");
				writer.write(symbol.getSymbol());
				writer.write("</span>");
				writer.write(suffix + " " + element.toString().toLowerCase());
				writer.write(LINE_BREAK);
			}
			writer.write("</p>");

		} catch (IOException ex) {
			throw new RuntimeException("Could not write to writer", ex);
		}

	}

	private boolean renderLeftSideRowNumber(int currentLineNumber, Chart chart) {
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

	private void renderLeftSideRowNumber(int currentLineNumber, Chart chart,
			Writer writer) throws IOException {
		if (renderLeftSideRowNumber(currentLineNumber, chart)) {
			writer.write(String.valueOf(currentLineNumber));
		}
	}

	private boolean renderRightSideRowNumber(int currentLineNumber, Chart chart) {
		return writeLineNumbers
				&& !renderLeftSideRowNumber(currentLineNumber, chart);
	}

}
