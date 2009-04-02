package com.knitml.renderer.chart.writer.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.Side;
import com.knitml.renderer.chart.Chart;
import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.stylesheet.StylesheetProvider;
import com.knitml.renderer.chart.symbol.NoSymbolFoundException;
import com.knitml.renderer.chart.symbol.SymbolProvider;
import com.knitml.renderer.chart.writer.ChartWriter;

public class HtmlChartWriter implements ChartWriter {

	private SymbolProvider symbolProvider;
	private boolean writeLineNumbers = true;
	private String stylesheetClassPrefix = "";
	private String suffix = ":";
	private static final String SYSTEM_LINE_BREAK = System
			.getProperty("line.separator");
	private static final String LINE_BREAK = "<br />" + SYSTEM_LINE_BREAK;

	public HtmlChartWriter(SymbolProvider symbolProvider) {
		super();
		this.symbolProvider = symbolProvider;
		if (symbolProvider instanceof StylesheetProvider) {
			stylesheetClassPrefix = ((StylesheetProvider) symbolProvider)
					.getStyleClassPrefix()
					+ "-";
		}
	}

	public void writeChart(Chart chart, Writer writer)
			throws NoSymbolFoundException {

		List<List<ChartElement>> graph = chart.getGraph();
		int currentLineNumber = chart.getStartingRowNumber() + graph.size() - 1;

		Set<ChartElement> elementsUsed = new TreeSet<ChartElement>();
		ListIterator<List<ChartElement>> graphIt = graph.listIterator(graph
				.size());
		try {
			writer.write("<table class=\"" + stylesheetClassPrefix + "chart\">"
					+ SYSTEM_LINE_BREAK);
			if (chart.getTitle() != null) {
				writer.write("<caption>");
				writer.write(chart.getTitle());
				writer.write("</caption>" + SYSTEM_LINE_BREAK);
			}

			writer.write("<colgroup class=\"");
			writer.write(stylesheetClassPrefix + "lhcol\" />" + SYSTEM_LINE_BREAK);
			writer.write("<colgroup class=\"");
			writer.write(stylesheetClassPrefix + "bodycol\" ");
			writer.write("span=\"" + chart.getWidth() + "\" ");
			writer.write("/>" + SYSTEM_LINE_BREAK);
			writer.write("<colgroup class=\"");
			writer.write(stylesheetClassPrefix + "rhcol\" />" + SYSTEM_LINE_BREAK);
			
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
					elementsUsed.add(element);
					String symbol = symbolProvider.getSymbol(element);

					if (symbol.length() > 1) { // as in an Aire River cable that
						// spans x number of characters
						for (int i = symbol.length() - 1; i >= 0; i--) {
							writer.write("<td class=\"" + stylesheetClassPrefix
									+ "cell\">");
							writer.write(String.valueOf(symbol.charAt(i)));
							writer.write("</td>");
						}
					} else {
						writer.write("<td class=\"" + stylesheetClassPrefix
								+ "cell\"");
						if (element.width() > 1) {
							// as in a Knitter's Symbols font that is one
							// character but 4x as wide
							writer
									.write(" colspan=\"" + element.width()
											+ "\"");
						}
						writer.write(">");
						writer.write(symbol);
						writer.write("</td>");
					}
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
			for (ChartElement element : elementsUsed) {
				// FIXME non-internationalized, not to mention ugly
				writer.write("<span class=\"" + stylesheetClassPrefix
						+ "legend\">");
				writer.write(symbolProvider.getSymbol(element));
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
