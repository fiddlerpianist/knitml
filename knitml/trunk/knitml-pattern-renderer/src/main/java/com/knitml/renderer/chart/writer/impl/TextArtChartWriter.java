package com.knitml.renderer.chart.writer.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.Side;
import com.knitml.renderer.chart.Chart;
import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.translator.ChartElementTranslator;
import com.knitml.renderer.chart.writer.ChartWriter;

public class TextArtChartWriter implements ChartWriter {

	private ChartElementTranslator translator;
	private boolean writeLineNumbers = true;
	private String suffix = ":";
	private String rowDelimiter = "|";
	private static final String LINE_BREAK = System
			.getProperty("line.separator");

	public TextArtChartWriter(ChartElementTranslator translator) {
		super();
		this.translator = translator;
	}

	public void writeChart(Chart chart, Writer writer) {
		
		List<List<ChartElement>> graph = chart.getGraph();
		int currentLineNumber = chart.getStartingRowNumber() + graph.size() - 1;
		int fillCount = 2; // one for the row number digit, one for the space
		if (currentLineNumber >= 10) {
			fillCount++;
		}
		if (currentLineNumber >= 100) {
			fillCount++;
		}

		Set<ChartElement> elementsUsed = new TreeSet<ChartElement>();
		ListIterator<List<ChartElement>> graphIt = graph.listIterator(graph
				.size());
		try {
			if (chart.getTitle() != null) {
				writer.write(chart.getTitle());
				writer.write(suffix);
				writer.write(LINE_BREAK);
			}
			while (graphIt.hasPrevious()) {
				List<ChartElement> row = graphIt.previous();
				ListIterator<ChartElement> rowIt = row.listIterator(row.size());

				if (renderLeftSideRowNumber(currentLineNumber, chart)) {
					writer.write(StringUtils.rightPad(String
							.valueOf(currentLineNumber), fillCount));
				} else {
					writer.write(StringUtils.rightPad("", fillCount));
				}
				writer.write(rowDelimiter);
				
				while (rowIt.hasPrevious()) {
					ChartElement element = rowIt.previous();
					elementsUsed.add(element);
					String symbol = translator.getSymbol(element);
					if (symbol == null) {
						throw new NotImplementedException("The stitch " + element + " is undefined for this translator");
					}
					writer.write(symbol);
				}

				writer.write(rowDelimiter);
				if (renderRightSideRowNumber(currentLineNumber, chart)) {
					writer.write(StringUtils.leftPad(String.valueOf(currentLineNumber), fillCount));
				} else {
					writer.write(StringUtils.leftPad("", fillCount));
				}
				currentLineNumber--;
				writer.write(LINE_BREAK);
			}
			writer.write("Legend");
			writer.write(LINE_BREAK);
			for (ChartElement element : elementsUsed) {
				// FIXME non-internationalized, not to mention ugly
				writer.write(translator.getSymbol(element) + suffix + " "
						+ element.toString().toLowerCase());
				writer.write(LINE_BREAK);
			}
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
