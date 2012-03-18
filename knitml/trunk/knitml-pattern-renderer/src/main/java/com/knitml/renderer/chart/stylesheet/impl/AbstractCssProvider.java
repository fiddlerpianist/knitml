package com.knitml.renderer.chart.stylesheet.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.knitml.renderer.chart.stylesheet.StylesheetProvider;

public abstract class AbstractCssProvider implements StylesheetProvider {

	private static final String LINE_BREAK = System.getProperty("line.separator");
	private String stylesheet;

	public AbstractCssProvider() {
		try {
//			String basePackagePath = this.getClass().getPackage().getName()
//					.replace('.', '/');
			Resource stylesheetResource = new ClassPathResource(getRelativePathToCssFile(), this.getClass());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stylesheetResource.getInputStream()));
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
	
	protected String getRelativePathToCssFile() {
		return getStyleClassPrefix() + ".css";
	}

	
}
