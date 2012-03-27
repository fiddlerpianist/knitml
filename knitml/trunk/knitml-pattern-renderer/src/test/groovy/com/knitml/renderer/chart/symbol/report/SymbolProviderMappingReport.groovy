package com.knitml.renderer.chart.symbol.report;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml
import static org.apache.commons.lang.StringEscapeUtils.escapeXml

import java.io.Writer

import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.Multimap
import com.knitml.renderer.chart.ChartElement
import com.knitml.renderer.chart.legend.LegendOperationRenderer
import com.knitml.renderer.chart.legend.impl.SimpleLegendOperationRenderer
import com.knitml.renderer.chart.stylesheet.StylesheetProvider
import com.knitml.renderer.chart.symbol.Symbol
import com.knitml.renderer.chart.symbol.SymbolProvider

class SymbolProviderMappingReport {

	private SymbolProvider symbolProvider
	private LegendOperationRenderer legendOperationRenderer
	private StylesheetProvider stylesheetProvider
	private String stylesheetClassPrefix
	private static final String SYS_LF = System.getProperty("line.separator")
	private static final String LF = "<br />" + SYS_LF;

	public SymbolProviderMappingReport(SymbolProvider symbolProvider) {
		this.symbolProvider = symbolProvider
		if (symbolProvider instanceof StylesheetProvider) {
			this.stylesheetProvider = (StylesheetProvider)symbolProvider
			this.stylesheetClassPrefix = stylesheetProvider.getStyleClassPrefix()
		}
		this.legendOperationRenderer = new SimpleLegendOperationRenderer()
	}

	public void writeMap(Writer writer) {

		writer << "<html><head><style type=\"" << escapeXml(stylesheetProvider.mimeType) << "\">" << SYS_LF
		String stylesheet = stylesheetProvider.stylesheet
		stylesheet = stylesheet.replaceAll("<\\s*/?\\s*[A-Za-z]+\\s*>", "");
		writer << stylesheet << SYS_LF << "</style></head><body>" << SYS_LF


//		writer << "<table class=\"${stylesheetProvider.styleClassPrefix}-legend\">" << SYS_LF
		writer << "<table>" << SYS_LF
		'''<caption>Legend</caption>
				<colgroup />
				<colgroup />
				<tbody>
				'''
		def collisionSet = new LinkedHashSet<Symbol>()
		Multimap<Symbol, ChartElement> symbolMap = new LinkedHashMultimap<Symbol, ChartElement>()
		def unmappedElements = new LinkedHashSet<ChartElement>()
		ChartElement.values().each {
			try {
				def symbol = symbolProvider.getSymbol(it)
				if (symbolMap.containsKey(symbol)) {
					collisionSet << symbol
				}
				symbolMap.put(symbol, it)
				def rawKey = symbol.symbol
				def rawValue = legendOperationRenderer.resolveLegendFor(it, null)
				writer << "<tr><td class=\"${stylesheetProvider.getSymbolSetName(symbol.symbolSetId)}-legend\">" <<
						"${escapeHtml rawKey}</td>" <<
						"<td>${escapeHtml rawValue}</td></tr>" << SYS_LF
			}
			catch (Exception ex) {
				// no value mapped
				unmappedElements << it
			}
		}
		writer << "</tbody></table>"
		if (!collisionSet.empty) {
			writer << "Collisions:" << LF
			collisionSet.each {
				writer << "<span class=\"${stylesheetProvider.getSymbolSetName(it.symbolSetId)}-legend\">" <<
						"${escapeHtml it.symbol}</span>" << "maps to: "
				symbolMap.get(it).each { writer << it << " " }
				writer << LF
			}
		}
		if (!unmappedElements.empty) {
			writer << "Unmapped elements:" << LF
			unmappedElements.each { writer << it << " " }
			writer << LF
		}
		writer << "</body></html>"
	}
}
