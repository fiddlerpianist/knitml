package com.knitml.renderer.impl.html;

import java.io.Writer;

import com.knitml.renderer.impl.helpers.WriterHelper;

public class HtmlWriterHelper extends WriterHelper {

	private boolean preformatted = false;
	private boolean inDiv = false;

	public HtmlWriterHelper(Writer writer) {
		super(writer);
	}

	public void writeSystemNewLine() {
		super.write(LINE_SEPARATOR);
	}

	@Override
	public void writeIndent() {
		if (!isPreformatted()) {
			log.trace("writeIndent (with div)");
			double ems = 0.0;
			for (int i = getIndent(); i > 0; i--) {
				ems += 2.5d;
			}
			if (ems > 0.0) {
				write("<div style=\"text-indent: " + ems + "em;\">");
				this.inDiv = true;
			}
		} else {
			super.writeIndent();
		}
	}

	@Override
	protected String getNewLineCharacters() {
		if (!isPreformatted()) {
			if (inDiv) {
				this.inDiv = false;
				return "</div>" + LINE_SEPARATOR;
			}
			return "<br />" + LINE_SEPARATOR;
		}
		return super.getNewLineCharacters();
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
