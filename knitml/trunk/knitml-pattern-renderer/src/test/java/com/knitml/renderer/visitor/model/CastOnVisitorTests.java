package com.knitml.renderer.visitor.model;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import test.support.AbstractRenderingContextTests;

public class CastOnVisitorTests extends AbstractRenderingContextTests {

	private static final String LINE_BREAK = System
			.getProperty("line.separator");
	private static final String INDENT = "    ";
	private static final String NEEDLES = "Needles:";
	private static final String NEEDLES_HEADER = NEEDLES + LINE_BREAK + INDENT;

	@Test
	public void placeholder() throws Exception {
	}
}
