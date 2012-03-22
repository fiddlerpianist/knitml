package com.knitml.renderer.handler.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.jibx.runtime.JiBXException
import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.pattern.Supplies;
import com.knitml.core.common.ValidationException

import test.support.AbstractRenderingContextTests

class SuppliesYarnVisitorTests extends AbstractRenderingContextTests {

	private static final LINE_BREAK = System.getProperty("line.separator");
	private static final INDENT = "    ";
	private static final YARN_HEADER = "Yarn:" + LINE_BREAK + INDENT;
	
	@Test
	void oneYarnWithBrandCategoryWithColorNameAndNumber() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common"><yarn-types>
			<yarn-type id="lornas" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering">
				<yarns>
					<common:yarn id="main-color">
						<common:total-weight unit="g">100</common:total-weight>
						<common:color name="watercolor" number="233"/>
					</common:yarn>
				</yarns>
			</yarn-type></yarn-types><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "100 g of Lorna's Laces Shepherd Sock (fingering weight) in watercolor (233)")
	}
	
	@Test
	void oneYarnWithBrandCategoryWithColorName() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common"><yarn-types>
			<yarn-type id="lornas" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering">
				<yarns>
					<common:yarn id="main-color">
						<common:total-weight unit="g">100</common:total-weight>
						<common:color name="watercolor"/>
					</common:yarn>
				</yarns>
			</yarn-type></yarn-types><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "100 g of Lorna's Laces Shepherd Sock (fingering weight) in watercolor")
	}
	
	@Test
	void oneYarnWithBrandWithColorName() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common"><yarn-types>
			<yarn-type id="lornas" brand="Lorna's Laces">
				<yarns>
					<common:yarn id="main-color">
						<common:total-weight unit="g">100</common:total-weight>
						<common:color name="watercolor"/>
					</common:yarn>
				</yarns>
			</yarn-type></yarn-types><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "100 g of Lorna's Laces in watercolor" + LINE_BREAK)
	}

	@Test
	void oneBrandYarnUnderOneYarnType() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common"><yarn-types>
			<yarn-type id="lornas" brand="Lorna's Laces">
				<yarns>
					<common:yarn id="main-color">
						<common:total-weight unit="g">50</common:total-weight>
					</common:yarn>
				</yarns>
			</yarn-type></yarn-types><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "50 g of Lorna's Laces")
	}
	
	@Test
	void oneYarnWithWeightAndColorName() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common"><yarn-types>
			<yarn-type id="lornas" weight="fingering">
				<yarns>
					<common:yarn id="main-color">
						<common:total-weight unit="g">100</common:total-weight>
						<common:color name="orange"/>
					</common:yarn>
				</yarns>
			</yarn-type></yarn-types><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "100 g of fingering weight yarn in orange" + LINE_BREAK)
	}
	
	@Test
	void oneYarnWithLengthAndYarnWeight() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common"><yarn-types>
			<yarn-type id="lornas" weight="fingering">
				<yarns>
					<common:yarn id="main-color">
						<common:total-weight unit="yd">1000</common:total-weight>
					</common:yarn>
				</yarns>
			</yarn-type></yarn-types><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "1000 yd of fingering weight yarn" + LINE_BREAK)
	}
	
	@Test
	void oneYarnWithLengthButNoYarnWeightOrBrand() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common"><yarn-types>
			<yarn-type id="yarn-type-1">
				<yarns>
					<common:yarn id="main-color">
						<common:total-weight unit="yd">1000</common:total-weight>
					</common:yarn>
				</yarns>
			</yarn-type></yarn-types><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "1000 yd of yarn" + LINE_BREAK)
	}
	
	@Test
	void oneYarnWithLengthWeightAndYarnWeight() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common"><yarn-types>
			<yarn-type id="yarn-type-1" weight="fingering">
				<yarns>
					<common:yarn id="main-color">
						<common:total-length unit="yd">1000</common:total-length>
						<common:total-weight unit="oz">2</common:total-weight>
					</common:yarn>
				</yarns>
			</yarn-type></yarn-types><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "2 oz (1000 yd) of fingering weight yarn" + LINE_BREAK)
	}
	
	@Test(expected=ValidationException)
	void twoFingeringWeightYarnsWithOnlyOneSymbolSupplied() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common"><yarn-types>
			<yarn-type id="wool" weight="fingering">
				<yarns>
					<common:yarn id="main-color" symbol="MC">
						<common:color name="orange"/>
					</common:yarn>
					<common:yarn id="contrasting-color">
                		<common:color name="blue"/>
					</common:yarn>
				</yarns></yarn-type></yarn-types><needle-types/><accessories/></supplies>''', Supplies
	}
	
	@Test
	void twoFingeringWeightYarnsUnderOneYarnType() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common"><yarn-types>
			<yarn-type id="wool" weight="fingering">
				<yarns>
					<common:yarn id="main-color">
						<common:total-length unit="yd">500</common:total-length>
						<common:total-weight unit="g">50</common:total-weight>
						<common:color name="orange"/>
            		</common:yarn>
					<common:yarn id="contrasting-color">
						<common:total-length unit="m">990</common:total-length>
						<common:total-weight unit="g">100</common:total-weight>
                		<common:color name="blue"/>
					</common:yarn>
				</yarns>
			</yarn-type></yarn-types><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "fingering weight" +
				LINE_BREAK + INDENT + INDENT + "Color A: orange (50 g / 500 yd)" +
				LINE_BREAK + INDENT + INDENT + "Color B: blue (100 g / 990 m)")
	}
	
	@Test
	void twoBrandYarnsUnderOneYarnType() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common"><yarn-types>
			<yarn-type id="lornas" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering">
				<yarns>
					<common:yarn id="main-color">
						<common:total-length unit="yd">500</common:total-length>
						<common:total-weight unit="g">50</common:total-weight>
						<common:color name="orange" number="201"/>
            		</common:yarn>
					<common:yarn id="contrasting-color">
						<common:total-weight unit="g">100</common:total-weight>
                		<common:color name="blue" number="999"/>
					</common:yarn>
				</yarns>
			</yarn-type></yarn-types><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "Lorna's Laces Shepherd Sock (fingering weight)" +
				LINE_BREAK + INDENT + INDENT + "Color A: orange 201 (50 g / 500 yd)" +
				LINE_BREAK + INDENT + INDENT + "Color B: blue 999 (100 g)")
	}
	
	@Test
	void twoBrandYarnsUnderTwoYarnTypes() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common"><yarn-types>
			<yarn-type id="wool-type" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering">
				<yarns>
					<common:yarn id="wool-yarn">
						<common:total-length unit="yd">500</common:total-length>
						<common:total-weight unit="g">50</common:total-weight>
						<common:color name="orange" number="201"/>
            		</common:yarn>
				</yarns>
			</yarn-type>
			<yarn-type id="cotton-type" brand="Cotton Classic" weight="worsted">
				<yarns>
					<common:yarn id="cotton-yarn">
						<common:total-length unit="yd">225</common:total-length>
						<common:total-weight unit="g">25</common:total-weight>
						<common:color name="blue"/>
            		</common:yarn>
				</yarns>
			</yarn-type>
			</yarn-types><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "A: 50 g (500 yd) of Lorna's Laces Shepherd Sock (fingering weight) in orange (201)" +
				LINE_BREAK + INDENT + "B: 25 g (225 yd) of Cotton Classic (worsted weight) in blue")
	}
	
	@Test
	void twoBrandYarnsUnderTwoYarnTypesWithCustomSymbols() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common"><yarn-types>
			<yarn-type id="wool-type" brand="Lorna's Laces" category="Shepherd Sock" weight="fingering">
				<yarns>
					<common:yarn id="wool-yarn" symbol="MC">
						<common:total-length unit="yd">500</common:total-length>
						<common:total-weight unit="g">50</common:total-weight>
						<common:color name="orange" number="201"/>
            		</common:yarn>
				</yarns>
			</yarn-type>
			<yarn-type id="cotton-type" brand="Cotton Classic" weight="worsted">
				<yarns>
					<common:yarn id="cotton-yarn" symbol="CC">
						<common:total-length unit="yd">225</common:total-length>
						<common:total-weight unit="g">25</common:total-weight>
						<common:color name="blue"/>
            		</common:yarn>
				</yarns>
			</yarn-type>
			</yarn-types><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, startsWith (YARN_HEADER + "MC: 50 g (500 yd) of Lorna's Laces Shepherd Sock (fingering weight) in orange (201)" +
				LINE_BREAK + INDENT + "CC: 25 g (225 yd) of Cotton Classic (worsted weight) in blue")
	}
	
}
