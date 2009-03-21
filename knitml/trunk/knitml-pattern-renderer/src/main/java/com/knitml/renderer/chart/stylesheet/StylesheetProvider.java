package com.knitml.renderer.chart.stylesheet;

public interface StylesheetProvider {
	
	public String getMimeType();
	public String getStylesheet();
	public String getStyleClassPrefix();

}
