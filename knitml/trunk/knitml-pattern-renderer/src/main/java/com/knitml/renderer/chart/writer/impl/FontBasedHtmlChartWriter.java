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
import com.knitml.renderer.chart.translator.FontBasedChartElementTranslator;
import com.knitml.renderer.chart.translator.NoSymbolFoundException;
import com.knitml.renderer.chart.writer.ChartWriter;

public class FontBasedHtmlChartWriter implements ChartWriter {

	private FontBasedChartElementTranslator translator;
	private boolean writeLineNumbers = true;
	private String suffix = ":";
	private String rowDelimiter = "";
	private static final String LINE_BREAK = "<br />"
			+ System.getProperty("line.separator");

	public FontBasedHtmlChartWriter(FontBasedChartElementTranslator translator) {
		super();
		this.translator = translator;
	}

	public void writeChart(Chart chart, Writer writer) throws NoSymbolFoundException {

		List<List<ChartElement>> graph = chart.getGraph();
		int currentLineNumber = chart.getStartingRowNumber() + graph.size() - 1;

		Set<ChartElement> elementsUsed = new TreeSet<ChartElement>();
		ListIterator<List<ChartElement>> graphIt = graph.listIterator(graph
				.size());
		try {
			writer.write("<p>" + LINE_BREAK);
			if (chart.getTitle() != null) {
				writer.write(chart.getTitle());
				writer.write(suffix);
				writer.write(LINE_BREAK);
			}
			while (graphIt.hasPrevious()) {
				List<ChartElement> row = graphIt.previous();
				ListIterator<ChartElement> rowIt = row.listIterator(row.size());

				if (renderLeftSideRowNumber(currentLineNumber, chart)) {
					writer.write(String.valueOf(currentLineNumber));
				}
				writer.write(rowDelimiter);
				
				writer.write("<span style=\"font-family: KSymbolsW\">");

				while (rowIt.hasPrevious()) {
					ChartElement element = rowIt.previous();
					elementsUsed.add(element);
					String symbol = translator.getSymbol(element);
					writer.write(symbol);
				}
				writer.write("</span>");

				writer.write(rowDelimiter);
				if (renderRightSideRowNumber(currentLineNumber, chart)) {
					writer.write(String.valueOf(currentLineNumber));
				}
				currentLineNumber--;
				writer.write(LINE_BREAK);
			}
			writer.write("Legend");
			writer.write(LINE_BREAK);
			for (ChartElement element : elementsUsed) {
				// FIXME non-internationalized, not to mention ugly
				writer.write("<span style=\"font-family: KSymbolsW\">");
				writer.write(translator.getSymbol(element));
				writer.write("</span>");
				writer.write(suffix + " "
						+ element.toString().toLowerCase());
				writer.write(LINE_BREAK);
			}
			writer.write("</p>");
			writer.write(LINE_BREAK);

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

	private boolean renderRightSideRowNumber(int currentLineNumber, Chart chart) {
		return writeLineNumbers
				&& !renderLeftSideRowNumber(currentLineNumber, chart);
	}

}
