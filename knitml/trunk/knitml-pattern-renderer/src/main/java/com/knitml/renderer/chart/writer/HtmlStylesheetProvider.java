package com.knitml.renderer.chart.writer;

public interface HtmlStylesheetProvider {
	
	public String getMimeType();
	public String getStylesheet();
	public String getStyleClassPrefix();

}
