package com.knitml.renderer.impl.helpers;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.output.NullWriter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.compatibility.Stack;

public class WriterHelper {

	public static final String SYSTEM_LINE_SEPARATOR = System
			.getProperty("line.separator");

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	// this is THE final output
	private Writer writer = new NullWriter();

	// this is where temporary buffers are kept and stashed
	private Map<String, StringBuffer> namedBuffers = new HashMap<String, StringBuffer>();
	// this is the current buffer that all write() operations will go to, if set
//	private StringBuffer currentBuffer;
	private Stack<StringBuffer> bufferStack = new Stack<StringBuffer>();
	private boolean beginningOfSentence = true;
	private boolean beginningOfParagraph = true;
	private int indent = 0;

	protected int getIndent() {
		return this.indent;
	}

	public WriterHelper() {
	}

	public WriterHelper(Writer writer) {
		this.writer = writer;
	}

	public void startWritingToAnonymousSegment() {
		bufferStack.push(new StringBuffer(256));
	}
	public String stopWritingToAnonymousSegment() {
		return bufferStack.pop().toString();
	}
	
	public void startWritingToSegment(String id) {
		StringBuffer targetBuffer = namedBuffers.get(id);
		if (targetBuffer == null) {
			targetBuffer = new StringBuffer(256);
			namedBuffers.put(id, targetBuffer);
		}
		bufferStack.push(targetBuffer);
	}

	public void stopWritingToSegment(String id) {
		StringBuffer namedBuffer = namedBuffers.get(id);
		if (bufferStack.empty() || bufferStack.peek() != namedBuffer) {
			throw new IllegalStateException(
					"Currently not writing to the same buffer whose end was requested");
		}
		bufferStack.pop();
	}

	public String getSegment(String id) {
		StringBuffer buffer = namedBuffers.get(id);
		if (buffer == null) {
			throw new IllegalStateException("Cannot find segment " + id);
		}
		return buffer.toString();
	}

	public void writeSegmentToWriter(String id) {
		StringBuffer buffer = namedBuffers.get(id);
		if (buffer != null) {
			write(buffer.toString());
		}
	}

	public void decrementIndent() {
		if (indent > 0) {
			indent--;
		}
	}

	public void incrementIndent() {
		indent++;
	}

	public boolean isBeginningOfParagraph() {
		return beginningOfParagraph;
	}

	public boolean isBeginningOfSentence() {
		return beginningOfSentence;
	}

	public final void writeNewLine() {
		log.trace("writeNewLine");
		writeInternal(getNewLineCharacters());
		this.beginningOfSentence = true;
		this.beginningOfParagraph = true;
	}

	protected String getNewLineCharacters() {
		return SYSTEM_LINE_SEPARATOR;
	}

	private void writeInternal(String string) {
		if (!bufferStack.empty()) {
			bufferStack.peek().append(string);
		} else {
			try {
				writer.write(string);
				// writer.flush();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public void write(String string) {
		log.trace("write {}", string);
		this.beginningOfSentence = false;
		this.beginningOfParagraph = false;
		String stringToWrite = string != null ? string : StringUtils.EMPTY;
		writeInternal(stringToWrite);
	}

	public void writeIndent() {
		log.trace("writeIndent");
		for (int i = indent; i > 0; i--) {
			writeInternal("    ");
		}
	}

	public void writeLine(String text) {
		log.trace("writeLine begin");
		writeIndent();
		write(text);
		writeNewLine();
		log.trace("writeLine end");
	}

	public void writeSentence(String text, boolean withPeriod, boolean capitalize) {
		log.trace("writeSentence begin");
		if (beginningOfParagraph) {
			writeIndent();
		} else if (!beginningOfParagraph && beginningOfSentence) {
			write(" ");
		}
		if (capitalize) {
			text = StringUtils.capitalize(text); 
		}
		write(text);
		if (withPeriod) {
			write(".");
		}
		this.beginningOfSentence = true;
		log.trace("writeSentence end");
	}

	public void closeWriter() {
		try {
			writer.close();
		} catch (IOException ex) {
			throw new RuntimeException("Couldn't close the renderer writer!");
		}
	}

	public String convertSystemToOutputLines(String description) {
		if (description == null) {
			return null;
		}
		return description.replaceAll("\r?\n", getNewLineCharacters());
	}

}
