package com.knitml.renderer.chart.stylesheet;

public interface StylesheetProvider {
	
	public String getMimeType();
	public String getStylesheet();
	public String getStyleClassPrefix(String symbolSetId) throws UnsupportedSymbolSetException;
	public String getStyleClassPrefix();

}
