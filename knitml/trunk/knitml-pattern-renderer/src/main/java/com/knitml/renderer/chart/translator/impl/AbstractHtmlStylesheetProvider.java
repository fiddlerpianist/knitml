package com.knitml.renderer.chart.translator.impl;

import static com.knitml.renderer.chart.ChartElement.K;
import static com.knitml.renderer.chart.ChartElement.K2TOG;
import static com.knitml.renderer.chart.ChartElement.NS;
import static com.knitml.renderer.chart.ChartElement.P;
import static com.knitml.renderer.chart.ChartElement.SL;
import static com.knitml.renderer.chart.ChartElement.SSK;
import static com.knitml.renderer.chart.ChartElement.YO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.translator.ChartElementTranslator;
import com.knitml.renderer.chart.writer.HtmlStylesheetProvider;

public abstract class AbstractHtmlStylesheetProvider implements HtmlStylesheetProvider {

	private static final String LINE_BREAK = System.getProperty("line.separator");
	private String stylesheet;

	public AbstractHtmlStylesheetProvider() {
		try {
			String basePackagePath = this.getClass().getPackage().getName()
					.replace('.', '/');
			Resource knittersSymbolsStylesheet = new ClassPathResource(
					basePackagePath + "/" + getRelativePathToCssFile());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					knittersSymbolsStylesheet.getInputStream()));
			Writer writer = new StringWriter(1024);
			String buffer = reader.readLine();
			while (buffer != null) {
				writer.write(buffer);
				writer.write(LINE_BREAK);
				buffer = reader.readLine();
			}
			stylesheet = writer.toString();
		} catch (IOException ex) {
			throw new RuntimeException("An unexpected I/O exception occurred",
					ex);
		}

	}

	public String getMimeType() {
		return "text/css";
	}

	public String getStylesheet() {
		return stylesheet;
	}
	
	protected abstract String getRelativePathToCssFile();

}
