package com.knitml.renderer.impl.basic;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.output.NullWriter;
import org.apache.commons.lang.StringUtils;

class WriterHelper {

	public final static String LINE_SEPARATOR = System
			.getProperty("line.separator");

	// this is THE final output
	private Writer writer = new NullWriter();
	
	// this is where temporary buffers are kept and stashed
	private Map<String, StringBuffer> namedBuffers = new HashMap<String, StringBuffer>();
	// this is the current buffer that all write() operations will go to, if set
	private StringBuffer currentBuffer;
	private boolean beginningOfSentence = true;
	private boolean beginningOfParagraph = true;
	private int indent = 0;

	public WriterHelper() {
	}

	public WriterHelper(Writer writer) {
		this.writer = writer;
	}
	
	public void startWritingToSegment(String id) {
		if (currentBuffer != null) {
			throw new IllegalStateException("Cannot write to a new segment while currently writing to another segment");
		}
		StringBuffer buffer = new StringBuffer(256); 
		namedBuffers.put(id, buffer);
		currentBuffer = buffer;
	}
	
	public void stopWritingToSegment(String id) {
		StringBuffer namedBuffer = namedBuffers.get(id);
		if (currentBuffer != namedBuffer) {
			throw new IllegalStateException("Currently not writing to the same buffer whose end was requested");
		}
		currentBuffer = null;
	}
	
	public void writeSegmentToWriter(String id) {
		StringBuffer buffer = namedBuffers.get(id);
		if (currentBuffer != null) {
			throw new IllegalStateException("Cannot write to writer while writing to a segment");
		}
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

	public void writeNewLine() {
		writeInternal(LINE_SEPARATOR);
		this.beginningOfSentence = true;
		this.beginningOfParagraph = true;
	}

	private void writeInternal(String string) {
		
		if (currentBuffer != null) {
			currentBuffer.append(string);
		} else {
			try {
				writer.write(string);
				writer.flush();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public void write(String string) {
		this.beginningOfSentence = false;
		this.beginningOfParagraph = false;
		String stringToWrite = string != null ? string : StringUtils.EMPTY;
		writeInternal(stringToWrite);
	}
	
	public void writeIndent() {
		for (int i = indent; i > 0; i--) {
			writeInternal("    ");
		}
	}

	public void writeLine(String text) {
		writeIndent();
		write(text);
		writeNewLine();
	}

	public void writeSentence(String text, boolean withPeriod) {
		if (beginningOfParagraph) {
			writeIndent();
		} else if (!beginningOfParagraph && beginningOfSentence) {
			write(" ");
		}
		write(StringUtils.capitalize(text));
		if (withPeriod) {
			write(".");
		}
		this.beginningOfSentence = true;
	}

	
	
	public void closeWriter() {
		try {
			writer.close();
		} catch (IOException ex) {
			throw new RuntimeException("Couldn't close the renderer writer!");
		}
	}
	
}
