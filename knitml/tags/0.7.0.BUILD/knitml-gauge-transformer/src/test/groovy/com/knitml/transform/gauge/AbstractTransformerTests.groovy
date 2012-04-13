/**
 * 
 */
package com.knitml.transform.gauge

import static com.knitml.core.units.Units.STITCHES_PER_INCH
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertThat

import javax.measure.unit.Unit

import org.apache.commons.lang.StringUtils
import org.junit.BeforeClass

import com.knitml.core.model.pattern.Parameters
import com.knitml.core.model.pattern.Pattern
import com.knitml.core.model.pattern.Version
import com.knitml.core.units.KnittingMeasure
import com.knitml.core.units.Units
import com.knitml.transform.gauge.event.TransformHandler
import com.knitml.transform.gauge.event.TransformPatternEventListener
import com.knitml.validation.ValidationProgram

abstract class AbstractTransformerTests {

	public static final String PATTERN_START_TAG = "<pattern:pattern xmlns:pattern=\"http://www.knitml.com/schema/pattern\" xmlns:common=\"http://www.knitml.com/schema/common\" xmlns=\"http://www.knitml.com/schema/operations\" version=\"" + Version.getCurrentVersionId() + "\">" //$NON-NLS-1$ //$NON-NLS-2$
	
	protected Pattern transform (String xml, String originalGauge, String targetGauge) {
		TransformHandler handler = new TransformHandler()
		handler.originalGauge = parseMeasurable(originalGauge)
		handler.targetGauge = parseMeasurable(targetGauge)
		def program = new ValidationProgram(new TransformPatternEventListener(handler))
		def params = new Parameters()
		params.reader = new StringReader(xml)
		return program.validate(params)
	}

	private parseMeasurable(String str) {
		String[] strArray = StringUtils.split(str)
		Unit<?> unit = Units.valueOf(strArray[1])
		return KnittingMeasure.valueOf(strArray[0], unit)
	}

}
