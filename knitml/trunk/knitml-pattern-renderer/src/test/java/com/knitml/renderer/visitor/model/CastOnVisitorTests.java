package com.knitml.renderer.visitor.model;

import static org.junit.Assert.assertThat;
import static org.hamcrest.text.StringStartsWith.startsWith;

import org.junit.Test;

import test.support.AbstractRenderingContextTests;

import com.knitml.core.model.header.Supplies;

public class CastOnVisitorTests extends AbstractRenderingContextTests {

	private static final String LINE_BREAK = System.getProperty("line.separator");
	private static final String INDENT = "    ";
	private static final String NEEDLES = "Needles:";
	private static final String NEEDLES_HEADER = NEEDLES + LINE_BREAK + INDENT;
	
	@Test
	public void twoRowsFromGlobalInstruction() throws Exception {
		processXml("" +
				"<supplies xmlns='http://www.knitml.com/schema/pattern'><yarns/><needles>" +
				"<needle-type id='size1circ' type='circular' brand='Addi Turbo'>" +
				"	<length unit='in'>24</length>" +
				"	<size unit='US'>1</size>" +
				"</needle-type>" +
				"<needle id='needle1' typeref='size1circ'/>" +
	            "</needles><accessories/></supplies>", Supplies.class);
			assertThat(getOutput(), startsWith (NEEDLES_HEADER + "1 circular needle size 1 US (2.25 mm): Addi Turbo, 24 in"));
	}
}
