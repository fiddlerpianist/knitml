package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.jibx.runtime.JiBXException
import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.header.Yarn
import com.knitml.core.model.header.Supplies
import com.knitml.core.common.ValidationException

import test.support.AbstractRenderingContextTests

class SuppliesYarnVisitorTests extends AbstractRenderingContextTests {

	private static final LINE_BREAK = System.getProperty("line.separator");
	private static final INDENT = "    ";
	private static final YARN_HEADER = "Yarn:" + LINE_BREAK + INDENT;
	
	@Test
	void oneYarnWithBrandCategoryWithColorNameAndNumber() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="lornas" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering"/>
			<yarn id="main-color" typeref="lornas">
				<total-weight unit="g">100</total-weight>
				<color name="watercolor" number="233"/>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "100 g of Lorna's Laces Shepherd Sock (fingering weight) in watercolor (233)")
	}
	
	@Test
	void oneYarnWithBrandCategoryWithColorName() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="lornas" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering"/>
			<yarn id="main-color" typeref="lornas">
				<total-weight unit="g">100</total-weight>
				<color name="watercolor"/>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "100 g of Lorna's Laces Shepherd Sock (fingering weight) in watercolor")
	}
	
	@Test
	void oneYarnWithBrandWithColorName() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="lornas" brand="Lorna's Laces"/>
			<yarn id="main-color" typeref="lornas">
				<total-weight unit="g">100</total-weight>
				<color name="watercolor"/>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "100 g of Lorna's Laces in watercolor" + LINE_BREAK)
	}

	@Test
	void oneBrandYarnUnderOneYarnType() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="lornas" brand="Lorna's Laces"/>
			<yarn id="main-color" typeref="lornas">
				<total-weight unit="g">50</total-weight>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "50 g of Lorna's Laces")
	}
	
	@Test
	void oneYarnWithWeightAndColorName() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="lornas" weight="fingering"/>
			<yarn id="main-color" typeref="lornas">
				<total-weight unit="g">100</total-weight>
				<color name="orange"/>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "100 g of fingering weight yarn in orange" + LINE_BREAK)
	}
	
	@Test
	void oneYarnWithLengthAndYarnWeight() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="lornas" weight="fingering"/>
			<yarn id="main-color" typeref="lornas">
				<total-length unit="yd">1000</total-length>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "1000 yd of fingering weight yarn" + LINE_BREAK)
	}
	
	@Test
	void oneYarnWithLengthButNoYarnWeightOrBrand() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="yarn-type-1"/>
			<yarn id="main-color" typeref="yarn-type-1">
				<total-length unit="yd">1000</total-length>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "1000 yd of yarn" + LINE_BREAK)
	}
	
	@Test
	void oneYarnWithLengthWeightAndYarnWeight() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="lornas" weight="fingering"/>
			<yarn id="main-color" typeref="lornas">
				<total-length unit="yd">1000</total-length>
				<total-weight unit="oz">2</total-weight>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "2 oz (1000 yd) of fingering weight yarn" + LINE_BREAK)
	}
	
	@Test(expected=ValidationException)
	void oneYarnWithUnknownTypeRef() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="lornas" brand="Lorna's Laces"/>
			<yarn id="main-color" typeref="invalid-typeref">
				<color name="watercolor"/>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
	}
	
	@Test(expected=JiBXException)
	void oneYarnWithNoTypeRef() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="lornas" brand="Lorna's Laces"/>
			<yarn id="main-color">
				<color name="watercolor"/>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
	}
	
	@Test(expected=ValidationException)
	void twoFingeringWeightYarnsWithOnlyOneSymbolSupplied() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="wool" weight="fingering"/>
			<yarn id="main-color" typeref="wool" symbol="MC">
				<color name="orange"/>
            </yarn>
			<yarn id="contrasting-color" typeref="wool">
                <color name="blue"/>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
	}
	
	@Test
	void twoFingeringWeightYarnsUnderOneYarnType() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="wool" weight="fingering"/>
			<yarn id="main-color" typeref="wool">
				<total-length unit="yd">500</total-length>
				<total-weight unit="g">50</total-weight>
				<color name="orange"/>
            </yarn>
			<yarn id="contrasting-color" typeref="wool">
				<total-length unit="m">990</total-length>
				<total-weight unit="g">100</total-weight>
                <color name="blue"/>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "fingering weight" +
				LINE_BREAK + INDENT + INDENT + "Color A: orange (50 g / 500 yd)" +
				LINE_BREAK + INDENT + INDENT + "Color B: blue (100 g / 990 m)")
	}
	
	@Test
	void twoBrandYarnsUnderOneYarnType() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="lornas" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering"/>
			<yarn id="main-color" typeref="lornas">
				<total-length unit="yd">500</total-length>
				<total-weight unit="g">50</total-weight>
				<color name="orange" number="201"/>
            </yarn>
			<yarn id="contrasting-color" typeref="lornas">
				<total-weight unit="g">100</total-weight>
                <color name="blue" number="999"/>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "Lorna's Laces Shepherd Sock (fingering weight)" +
				LINE_BREAK + INDENT + INDENT + "Color A: orange 201 (50 g / 500 yd)" +
				LINE_BREAK + INDENT + INDENT + "Color B: blue 999 (100 g)")
	}
	
	@Test
	void twoBrandYarnsUnderTwoYarnTypes() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="wool-type" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering"/>
			<yarn-type id="cotton-type" brand="Cotton Classic" weight="worsted"/>
			<yarn id="wool-yarn" typeref="wool-type">
				<total-length unit="yd">500</total-length>
				<total-weight unit="g">50</total-weight>
				<color name="orange" number="201"/>
            </yarn>
			<yarn id="cotton-yarn" typeref="cotton-type">
				<total-length unit="yd">225</total-length>
				<total-weight unit="g">25</total-weight>
				<color name="blue"/>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "A: 50 g (500 yd) of Lorna's Laces Shepherd Sock (fingering weight) in orange (201)" +
				LINE_BREAK + INDENT + "B: 25 g (225 yd) of Cotton Classic (worsted weight) in blue")
	}
	
	@Test
	void twoBrandYarnsUnderTwoYarnTypesWithCustomSymbols() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarns>
			<yarn-type id="wool-type" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering"/>
			<yarn-type id="cotton-type" brand="Cotton Classic" weight="worsted"/>
			<yarn id="wool-yarn" typeref="wool-type" symbol="MC">
				<total-length unit="yd">500</total-length>
				<total-weight unit="g">50</total-weight>
				<color name="orange" number="201"/>
            </yarn>
			<yarn id="cotton-yarn" typeref="cotton-type"  symbol="CC">
				<total-length unit="yd">225</total-length>
				<total-weight unit="g">25</total-weight>
				<color name="blue"/>
			</yarn></yarns><needles/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "MC: 50 g (500 yd) of Lorna's Laces Shepherd Sock (fingering weight) in orange (201)" +
				LINE_BREAK + INDENT + "CC: 25 g (225 yd) of Cotton Classic (worsted weight) in blue")
	}
	
	static void main(args) {
		JUnitCore.main(SuppliesYarnVisitorTests.name)
	}
	
}
