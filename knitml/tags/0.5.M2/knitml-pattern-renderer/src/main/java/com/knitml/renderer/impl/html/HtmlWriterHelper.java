package com.knitml.renderer.impl.html;

import java.io.Writer;

import com.knitml.renderer.impl.helpers.WriterHelper;

public class HtmlWriterHelper extends WriterHelper {

	private boolean preformatted = false;
	
	public HtmlWriterHelper(Writer writer) {
		super(writer);
	}
	
	public void writeSystemNewLine() {
		super.writeNewLine();
	}
	
	@Override
	public void writeIndent() {
		if (!isPreformatted()) {
			// FIXME this should be done with a div or something
			write("&nbsp;&nbsp;&nbsp;&nbsp;");
		} else {
			super.writeIndent();
		}
	}

	@Override
	public void writeNewLine() {
		if (!isPreformatted()) {
			write("<br />");
		} else {
			super.writeNewLine();
		}
	}

	public boolean isPreformatted() {
		return preformatted;
	}

	public void setPreformatted(boolean preformatted) {
		if (preformatted && !this.preformatted) {
			write("<pre>");
			writeSystemNewLine();
		} else if (!preformatted && this.preformatted) {
			write("</pre>");
		}
		this.preformatted = preformatted;
	}

}
