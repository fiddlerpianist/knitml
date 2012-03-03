package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.apache.commons.lang.StringUtils

import org.jibx.runtime.JiBXException
import org.junit.Test
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.JUnitCore

import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.pattern.Supplies;
import com.knitml.core.common.ValidationException

import test.support.AbstractRenderingContextTests

class SuppliesNeedlesVisitorTests extends AbstractRenderingContextTests {

	private static final LINE_BREAK = System.getProperty("line.separator");
	private static final INDENT = "    ";
	private static final NEEDLES = "Needles:"
	private static final NEEDLES_HEADER = NEEDLES + LINE_BREAK + INDENT;
	
	@Test
	void noNeedles() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarn-types/><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, is (StringUtils.EMPTY)
	}
	
	@Test(expected=JiBXException)
	void noNeedlesButNeedleType() {
		processXml '''
		<supplies xmlns="http://www.knitml.com/schema/pattern">
			<yarn-types/>
			<needle-types>
				<needle-type id="size1circ" type="circular" brand="Addi Turbo">
					<length unit="in">24</length>
					<size unit="US">1</size>
				</needle-type>
			</needle-types>
			<accessories/>
		</supplies>''', Supplies
		assertThat output, is (NEEDLES + LINE_BREAK + LINE_BREAK)
	}
	
	@Test
	void oneNeedleWithBrandLengthAndStyle() {
		processXml '''
		<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common">
			<yarn-types/>
			<needle-types>
				<needle-type id="size1circ" type="circular" brand="Addi Turbo">
					<length unit="in">24</length>
					<size unit="US">1</size>
					<needles>
						<common:needle id="needle1"/>
					</needles>
				</needle-type>
			</needle-types>
            <accessories/>
		</supplies>''', Supplies
		assertThat output, startsWith (NEEDLES_HEADER + "1 circular needle size 1 US (2.25 mm): Addi Turbo, 24 in")
	}
	
	@Test
	void twoNeedlesWithBrandLengthAndStyle() {
		processXml '''
		<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common">
			<yarn-types/>
			<needle-types>
				<needle-type id="size1circ" type="circular" brand="Addi Turbo">
					<length unit="in">24</length>
					<size unit="US">1</size>
					<needles>
						<common:needle id="needle1"/>
						<common:needle id="needle2"/>
					</needles>
				</needle-type>
            </needle-types>
			<accessories/>
		</supplies>''', Supplies
		assertThat output, startsWith (NEEDLES_HEADER + "2 circular needles size 1 US (2.25 mm): Addi Turbo, 24 in")
	}
	
	@Test
	void oneDoublePointedNeedle() {
		processXml '''
		<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common">
			<yarn-types/>
			<needle-types>
				<needle-type id="dpn-type" type="dpn">
					<needles>
						<common:needle id="needle1"/>
					</needles>
				</needle-type>
            </needle-types>
			<accessories/>
		</supplies>''', Supplies
		assertThat output, startsWith (NEEDLES_HEADER + "1 double-pointed needle")
	}
	
}
