package com.knitml.transform.gauge

import static com.knitml.core.units.Units.STITCHES_PER_INCH
import static java.lang.Math.round
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.text.StringContains.containsString
import static org.junit.Assert.assertThat

import javax.measure.Measure

import com.knitml.core.model.operations.block.CastOn
import com.knitml.core.model.operations.block.Row
import com.knitml.transform.gauge.event.TransformHandler
class SmallerGaugeTransformerTests extends AbstractTransformerTests {
	
	void setUpGauges(TransformHandler handler) {
		handler.originalGauge = Measure.valueOf(4, STITCHES_PER_INCH)
		handler.targetGauge = Measure.valueOf(5, STITCHES_PER_INCH)
	}
	
	void validate (Row row) {
		assertThat row.information.details[0].label, is ('with 5 more st')
	}

	void validate (CastOn castOn) {
		assertThat castOn.numberOfStitches, is (20)
		assertThat castOn.annotation, containsString (25.toString())
	}

	void validateChange (Row row, int delta) {
		if (delta != 0) {
			assertThat row.information.details[1], not (null)
			int total = round ( (20 + delta) * 5/4 )
			String expected = "end with $total"
			assertThat row.information.details[1].label, is (expected)
		}
	}
	
}