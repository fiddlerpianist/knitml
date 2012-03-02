package com.knitml.core.model.pattern;

import java.io.Reader;
import java.io.Writer;


public class Parameters {
	
	private boolean checkSyntax = false;
	private Reader reader;
	private Writer writer;
	private Pattern pattern;

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public boolean isCheckSyntax() {
		return checkSyntax;
	}

	public void setCheckSyntax(boolean validate) {
		this.checkSyntax = validate;
	}

	public Reader getReader() {
		return reader;
	}

	public void setReader(Reader reader) {
		this.reader = reader;
	}

	public Writer getWriter() {
		return writer;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
	}
}